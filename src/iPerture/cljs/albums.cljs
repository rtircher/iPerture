(ns albums
  (:require [net :as net]
            [spin :as spin]
            [enfocus.core :as ef]
            [domina :as dom])
  (:use [logger :only [log]])
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
  (em/at (em/select [".photos > *:last-child"])
         (em/before (loading-indicator-model identifier)))
  (spin/create-and-append-spinner (by-id identifier) {:top "45"}))

(defn- reset-add-photo-input []
  (aset (dom/single-node (em/select [".photo-file-input"])) "value" ""))

(defn- display-thumbnail [data identifier]
  (em/at (em/select [(str "#" identifier)])
         (em/do->
          (em/after (map #(thumbnail-model (aget % "thumbnail-url")) data))
          (em/substitute ""))))

(defn- upload-success-handler [status-url identifier]
  (log "FORM success: " status-url)
  (js/setTimeout
   #(net/get status-url
             {:on-success (fn [data]
                            (log "status success: " data)
                            (if (= data "processing")
                              (upload-success-handler status-url identifier)
                              (display-thumbnail data identifier)))
              :on-error (fn [& args] (log "Add photo status error: " args))})
   1500))

(defn- upload-photos [form identifier]
  (net/ajax-form form {:on-success (fn [data iFormIO]
                                     (upload-success-handler (.getResponseText iFormIO) identifier))
                       :on-error (fn [& args] (log "FORM error: " args))}))

(defn- photo-added-handler [& args]
  (let [form (dom/single-node (em/select ["#add-photo"]))
        uri  (.-action form)
        identifier (.now js/Date)]
    (log "Submitting form at url: " uri)
    (display-placeholder identifier)
    (upload-photos form identifier)
    (reset-add-photo-input)))


(em/defaction start [] 
  ["#edit-album input"] (em/listen :change photo-added-handler))



(start)

;; (goog.events.listen (goog.dom.getDocument) goog.events.EventType.LOAD #(js/console.log "Submitting form at url: " uri))


;; convert js data to cljs: (js->clj data)
