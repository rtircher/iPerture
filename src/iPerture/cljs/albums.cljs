(ns albums
  (:require [net :as net]
            [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(def ^:private edit-album "public/html/edit-album.html")

(defn- upload-photos [form]
  (net/ajax-form form {:on-success (fn [data] (js/console.log "FORM success: " data))
                       :on-error (fn [& args] (js/console.log "FORM error: " args))}))

(em/defaction start [] 
  ["input"] (em/listen :change
                       #(let [form (first (goog.dom.getElementsByTagNameAndClass "form"))
                              uri  (aget form "action")]
                          (js/console.log "Submitting form at url: " uri)
                          (upload-photos form))))

(start)
