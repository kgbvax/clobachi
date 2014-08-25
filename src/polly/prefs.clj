(ns polly.prefs
  (:gen-class))

;;config
(def my-watering-schedule-minutes (* 60 1)) ;perform check every xx minutes
(def water-for-time (* 8 60)) ;in seconds

;koubachi
(def koubachi-credentials "9uAjtXagR1oU18djLlb")
(def koubachi-api-key "KLABXQUJRWMR95ZO0URD9FAS")
(def koubachi-device-id "00066655de06") ;MAC of Koubachi
(def my-plant-id "196055")
(def base-url "https://api.koubachi.com/v2")
(def credentials-snippet (str "?user_credentials=" koubachi-credentials "&app_key=" koubachi-api-key))
(def koubachi-status-url (str base-url "/plants" credentials-snippet))
(def koubachi-device-details-url (str base-url "/user/smart_devices/" koubachi-device-id credentials-snippet))

;netio
(def netio-url "http://pump-netio/event?")

;;couch
;;(def couch-measurements-db-url "http://admin:ooJ4cohb@kgbvax.iriscloud.com/jassid")
(def couch-measurements-db-url "http://wandboard:5984/jassid")
