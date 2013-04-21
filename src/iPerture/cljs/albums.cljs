(ns albums
  (:require [net :as net]))

(def $ (js* "$"))

(defn- upload-photos [form]
  (net/ajax-form form {:on-success (fn [& args] (js/console.log "FORM success: " args))
                       :on-error (fn [& args] (js/console.log "FORM error: " args))}))

($ "ready"
   (fn []
     (-> ($ "#add-photo")
         (.on "submit" #(let [form (aget % "target")
                              uri  (aget form "action")]
                          (.preventDefault %)
                          (js/console.log "Submitting form at url: " uri)
                          (upload-photos form))))))
