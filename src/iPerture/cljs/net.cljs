(ns net
  (:require [goog.net.XhrIo]
            [goog.net.XhrManager]))

(def headers nil)
(def min-count 1)
(def max-count 10)

(def ^:private xhr-pool (goog.net.XhrIoPool. headers
                                                min-count
                                                max-count))

(def ^:private default-interval (* 30 1000))
(def ^:private default-priority 100)

(defn- get-xhr [callback priority]
  (.getObject xhr-pool callback (or priority default-priority)))

(defn- send-request [method uri {:keys [content headers timeout on-success on-error]} xhr]
  (assert uri)
  (assert method)
  (.setTimeoutInterval xhr (or timeout default-interval))
  (.send xhr uri method content headers))

(defn ajax [method uri {:keys [priority] :as options}]
  "
Send ajax request
using method (e.g. Get, POST, PUT...) By default GET
to uri
the following options can be provided:
- content: the data to be sent with the request
- headers: the headers to be sent with the request
- timeout: number of milliseconds after which an incomplete request will be aborted.
- on-success: function called when the request succeeds
- on-error: function called when the request fails
"
  (get-xhr (partial send-request method uri options) priority))

(def get    (partial ajax "GET"))
(def post   (partial ajax "POST"))
(def put    (partial ajax "PUT"))
(def delete (partial ajax "DELETE"))
