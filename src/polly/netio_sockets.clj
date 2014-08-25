;;;; Control a NETIO power strip
;;; This has to match the lua code installed as the NETIO's CGI error-handler
;;; See "resources/netio-cgi.lua"

(ns polly.netio-sockets
   (:require [clj-http.client :as http]
             [clojure.string :as string]
             [taoensso.timbre :as timbre])
  (:gen-class))


(timbre/refer-timbre) ;pull in logging support


(defn trigger-socket-state! [netio-url  duration]
  "Trigger the socket to on for duration"
  (let [url-with-params (str netio-url
                             "duration=" duration)]
    (debug  "netio op: " url-with-params)
    (http/get url-with-params)))


