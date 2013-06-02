(ns albums
  (:require [net :as net]
            [spin :as spin]
            [enfocus.core :as ef])
  (:require-macros [enfocus.macros :as em]))

(def ^:private edit-album "/html/edit-album.html")

(defn- background-photo [photo-url]
  (str "background-image:url('" photo-url "')"))

(defn- by-id [id]
  (goog.dom.getElement (str id)))

(em/defsnippet ^:private thumbnail-model edit-album [".photos > *:first-child"]
  [thumbnail-url]
  [:.photo] (em/set-attr :style (background-photo thumbnail-url)))

(em/defsnippet ^:private loading-indicator-model edit-album [".loading-placeholder"]
  [identifier]
  [:.loading-placeholder] (em/set-attr :id identifier))

(defn- display-placeholder [identifier]
  (em/at js/document [".photos > *:last-child"]
         (em/before (loading-indicator-model identifier)))
  (spin/create-and-append-spinner (by-id identifier) {:top "45"}))

(defn- upload-success-handler [data identifier]
  (js/console.log "FORM success: " data)
  (em/at js/document [(str "#" identifier)]
         (em/substitute (thumbnail-model (aget data "thumbnail-url")))))

(defn- upload-photos [form identifier]
  (net/ajax-form form {:on-success #(upload-success-handler % identifier)
                       :on-error (fn [& args] (js/console.log "FORM error: " args))}))

(defn- photo-added-handler [& args]
  (let [form (first (goog.dom.getElementsByTagNameAndClass "form"))
        uri  (aget form "action")
        identifier (.now js/Date)]
    (display-placeholder identifier)
    (js/console.log "Submitting form at url: " uri)
    (upload-photos form identifier)))


(em/defaction start [] 
  ["#edit-album input"] (em/listen :change photo-added-handler))



(start)

;; (goog.events.listen (goog.dom.getDocument) goog.events.EventType.LOAD #(js/console.log "Submitting form at url: " uri))


;; convert js data to cljs: (js->clj data)
