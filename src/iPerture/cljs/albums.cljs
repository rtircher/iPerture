(ns albums
  (:require [net :as net]
            [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(def ^:private edit-album "/html/edit-album.html")

(defn- background-photo [photo-url]
  (str "background-image:url('" photo-url "')"))

(em/defsnippet ^:private thumbnail-model edit-album [".photos > *:first-child"]
  [thumbnail-url]
  [:.photo] (em/set-attr :style (background-photo thumbnail-url)))

(defn- upload-success-handler [data]
  (js/console.log "FORM success: " data)
  (em/at js/document [".photos > *:last-child"]
         (em/before (thumbnail-model (aget data "thumbnail-url")))))

(defn- upload-photos [form]
  (net/ajax-form form {:on-success upload-success-handler
                       :on-error (fn [& args] (js/console.log "FORM error: " args))}))

(defn- photo-added-handler [& args]
  (let [form (first (goog.dom.getElementsByTagNameAndClass "form"))
        uri  (aget form "action")]
    (js/console.log "Submitting form at url: " uri)
    (upload-photos form)))


(em/defaction start [] 
  ["#edit-album input"] (em/listen :change photo-added-handler))



(start)
