(ns logger)

(defn- log [& messages]
  (when js/IPERTURE_LOGGER_ENABLED
    ;; console.log expects this to be the console object console
    (js/console.log.apply js/console (clj->js messages))))
