;;;; Poll a plant sensor (Koubachi) via API and switch on a
;;;; power socket for a while if water levels are below a certain threshold
;; Watering should occur at most once every 24h as the sensor only reports data
;; once per day.
;;
;; Thoughts on implementations:
;;
;;  - remember the date of the last watering. Interesting as it involves persistence.
;;    oportunity to record additional data (history) - but all this is superflous
;;
;;  + transient: Upon start, if water low -> water plants & record :time
;;    then poll water levels and only water if water low & we have a new reading.
;;
;; In order to prevent flooding caused from a dropped "turn of water" network message,
;; the disabeling of water is handled in the power strip internally


(ns polly.core
  (:require [clj-http.client :as http]
            [cheshire.core :refer :all]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.calendar-interval :as cal-interval]
            [clojurewerkz.quartzite.schedule.simple :as simple-interval]
            [clojurewerkz.quartzite.conversion :as qconversion]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.jobs :as jobs]
            [taoensso.timbre :as timbre]
            [com.ashafa.clutch :as clutch]
            [clj-time.core :as time]
            [clj-time.format :as time-format]
            [polly.netio-sockets :refer [trigger-socket-state!]]
            [polly.prefs :refer :all])
  (:gen-class))


(timbre/refer-timbre) ;pull in logging support


(def my-watering-schedule (cal-interval/schedule (cal-interval/with-interval-in-minutes my-watering-schedule-minutes)))

(def last-update (atom nil))
(defn update-last-update [newVal]
  (reset! last-update newVal))

;;unused but useful
(defn in? [coll x]
  (if (some #{x} coll) true false))


(def measurements-db (clutch/couch couch-measurements-db-url))


(defn http-fetch-json [url]
  "fetch the url via http get, return the parsed json body"
  (first
   (decode ;parse  as json
    ((http/get url
               {:accept :json})
     :body)))) ;fetch status via http get and return the body


(defstruct jassi-measurement :water-needed? :updated-at :water-level)


;;convert the keys of 'map' to proper keywords
(defn keys-as-keywords [map]
  (reduce-kv (fn [initial k v]
               (assoc initial
                 (if-not (keyword? k)
                   (keyword k)
                   k)
                 v))
             {} map))



;@TODO -> read update time from koubachi-device-details-url
(defn- extract-status [status]
  "extract fields of interest from Koubachi status response. Returns a map with:
  :water-needed? which is true if Koubachi thinks that water is required and
  :updated-at which is the time the last sensor reading was transmitted"
  (let [plant (status "plant")]
    (struct-map jassi-measurement
      :water-needed? (plant "vdm_water_pending")
      :updated-at   (plant "updated_at")
      :water-level (plant "vdm_water_level"))))



(defn- start-pump []
  (info "Starting  pump & water.")
  (trigger-socket-state! netio-url water-for-time))




;; poll the plant sensor and check if it thinks it needs water
;; if so, check that we have not watered the plant in the last 24h
;; as the sensor only transmits data every 24h
(defjob water-if-needed [ctx]
  (debug "polling water need. last-update is" @last-update)
  (try
    (let [plant-state (extract-status (http-fetch-json koubachi-status-url))
          plant-details (keys-as-keywords (fnext (http-fetch-json koubachi-device-details-url)))]
      (debug "plant state: " plant-state)
      (clutch/put-document measurements-db (assoc plant-state :type :state))
      (clutch/put-document measurements-db (assoc plant-details :type :details))
      (if (and
           (plant-state :water-needed?) ;water needed?
           (or (nil? @last-update); and we have newer sensor data or never seen an update?
               (time/after? @last-update  (plant-state :updated-at))))
        (start-pump)))
    (catch Exception e (error e))))



(defn -main [& args]
  ;(set-db! (open-document-db! "local:./resources/polly.db" )) ;open datastore
  ;init& start scheduler
  (qs/initialize)
  (qs/start)
  (let [job (jobs/build
             (jobs/of-type water-if-needed))
        trigger (triggers/build
                 (triggers/start-now)
                 (triggers/with-schedule  my-watering-schedule))]
    (qs/schedule job trigger)
    (info "Polly started.")))

(defn- init-repl []
  (debug "nothing has happened yet"))
