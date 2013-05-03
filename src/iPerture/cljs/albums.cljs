(ns albums
  (:require [net :as net]))

(def $ (js* "$"))

(defn- upload-photos [form]
  (net/ajax-form form {:on-success (fn [& args] (js/console.log "FORM success: " args))
                       :on-error (fn [& args] (js/console.log "FORM error: " args))}))

($ "ready"
   (fn []
     (-> ($ "input")
         (.on "change" #(let [form (first (.toArray ($ "form")))
                              uri  (aget form "action")]
                          (js/console.log "Submitting form at url: " uri)
                          (upload-photos form))))))
