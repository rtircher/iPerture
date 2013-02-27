(ns middlewares.logger)

(defn wrap-request-logging-with-formatter [handler formatter]
  "Adds simple logging for requests.
The output shows the current time, the request method, uri, and total time spent processing the request."
  (fn [{:keys [request-method uri] :as req}]
    (let [start  (System/currentTimeMillis)
          resp   (handler req)
          status (:status resp)
          finish (System/currentTimeMillis)
          total  (- finish start)]
      (formatter request-method status uri total)
      resp)))

(defn- color-delim-start [color-code]
  (str "\033[" color-code "m"))
(defn- color-delim-end []
  "\033[m")
(defn- colorize [color-code string]
  (str (color-delim-start color-code) string (color-delim-end)))
(defn- bold-red    [string] (colorize "31;1" string))
(defn- bold-green  [string] (colorize "32;1" string))
(defn- bold-yellow [string] (colorize "33;1" string))
(defn- bold-blue   [string] (colorize "34;1" string))

(defn- colored-request-method [request-method]
  (case request-method
    :get    (bold-green request-method)
    :post   (bold-red request-method)
    :put    (bold-red request-method)
    :delete (bold-red request-method)
    (bold-yellow request-method)))

(defn- logformatter [reqmeth status uri totaltime]
  "Basic logformatter for logging middleware."
  (let [line (format "[%s] %s [Status: %s] %s (%dms)" (java.util.Date.)  (colored-request-method reqmeth) status uri totaltime)]
    (locking System/out (println line))))

(defn wrap-request-logging [handler]
  "Provide a default logger with a default logformatter"
  (wrap-request-logging-with-formatter handler logformatter))