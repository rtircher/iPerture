(ns net
  (:require goog.net.XhrIo
            goog.net.XhrManager
            goog.events.listen
            goog.net.IframeIo
            [goog.net.EventType :as event-types]))

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

(defn- get-response [xhr]
  (let [xml  (try (.getResponseXml xhr) (catch js/Error e))
        json (try (.getResponseJson xhr) (catch js/Error e))
        text (.getResponseText xhr)]
    (or json xml text)))

(defn- listen-to-event [xhr event-type callback]
  (when callback
    (goog.events.listen xhr
                        event-type
                        (fn [event]
                          (let [xhr    (aget event "target")
                                data   (get-response xhr)]
                            (callback data xhr))))))

(defn- listen-to-all-events [xhr events-callbacks]
  (doseq [[event-type callback] events-callbacks]
    (listen-to-event xhr event-type callback)))

(defn- map-callback-to-event-type [{:keys [on-ready-state-change on-complete on-success on-error on-ready on-timeout on-abort]}]
  {event-types/READY_STATE_CHANGE on-ready-state-change
   event-types/COMPLETE           on-complete
   event-types/SUCCESS            on-success
   event-types/ERROR              on-error
   event-types/READY              on-ready
   event-types/TIMEOUT            on-timeout
   event-types/ABORT              on-abort})

(defn- send-request [method uri {:keys [content headers timeout] :as options} xhr]
  (assert uri)
  (assert method)

  (listen-to-all-events xhr (map-callback-to-event-type options))

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

- on-ready-state-changed: function called to any browser readystatechange event
- on-complete: function called when request has been resolved, whether successfully or not
- on-success: function called when the request succeeds
- on-error: function called when the request fails
- on-ready: function called when an xhr request is ready to be used
- on-timeout: function called when the request does not complete before the timeout interval has elapsed
- on-abort: function called when the request is aborded

For all the event handler the function called is expected to accept the following argumens:
- data: the data returned by the server (either xml, json, or plain text)
- request-object: the underlying goog.net.XhrIo object that executed the request
"
  (get-xhr (partial send-request method uri options) priority))

;; Helper functions for common REST operations
(def get    (partial ajax "GET"))
(def post   (partial ajax "POST"))
(def put    (partial ajax "PUT"))
(def delete (partial ajax "DELETE"))


;; form ajax

(defn ajax-form [form {:keys [uri no-cache timeout] :as options}]
  "
Sends the data stored in an existing form to the server. The HTTP method should be specified on the form, the action can also be specified but can be overridden by the optional URI param. This can be used in conjunction with a file-upload input to upload a file in the background without affecting history. Example form:
<form action='/server/' enctype='multipart/form-data' method='POST'>
<input name='userfile' type='file'>
</form>

Arguments:
form : HTMLFormElement - Form element used to send the request to the server.

options:
- uri: string - Uri to set for the destination of the request, by default the uri will come from the form.
- no-cache: boolean - Append a timestamp to the request to avoid caching.  False by default.
- timeout: number of milliseconds after which an incomplete request will be aborted.

- on-ready-state-changed: function called to any browser readystatechange event
- on-complete: function called when request has been resolved, whether successfully or not
- on-success: function called when the request succeeds
- on-error: function called when the request fails
- on-ready: function called when an xhr request is ready to be used
- on-timeout: function called when the request does not complete before the timeout interval has elapsed
- on-abort: function called when the request is aborded

For all the event handler the function called is expected to accept the following argumens:
- data: the data returned by the server (either xml, json, or plain text)
- request-object: the underlying goog.net.IframeIo object that executed the request
"
  (assert form)
  (let [frame-io (goog.net.IframeIo.)]
    (listen-to-all-events frame-io (map-callback-to-event-type options))
    (.setTimeoutInterval frame-io (or timeout default-interval))
    (.sendFromForm frame-io form uri (or no-cache false))))
