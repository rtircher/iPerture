(ns iPerture.delayed-job
  (:import [java.util.concurrent Callable Executors]))

(def ^:private job-executor
  (Executors/newFixedThreadPool 10)) ;; Allow customization of this number

(def ^:private jobs (atom {}))

(defn submit-job [func]
  (let [job-id   (str (java.util.UUID/randomUUID))
        callable (reify Callable (call [_] (func)))]
    (swap! jobs assoc job-id (.submit job-executor callable))
    job-id))

(defn result [job-id]
  (if-let [job (@jobs job-id)]
    (if (.isDone job)
      (.get job)
      :processing)))

(defn remove-job [job-id]
  (swap! jobs dissoc job-id))
