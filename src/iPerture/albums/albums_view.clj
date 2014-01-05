(ns iPerture.albums.albums-view
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.views.common :as common]))

;; Index page

(def ^:private index-albums-template (html/html-resource "public/html/albums/index.html"))

(def ^:private album-selector [:.albums :> html/first-child])
(html/defsnippet ^:private album-vm index-albums-template album-selector
  [{:keys [id title photos] :as album}]

  [:a] (html/do->
        (html/set-attr :href (str "/albums/" id))
        (if-let [thumbnail-url (:thumbnail-url (first photos))]
          (html/set-attr :style (common/background-photo thumbnail-url))
          identity))
  [:.album-title] (html/content title))

(html/deftemplate ^:private render-index-albums-template index-albums-template [albums]
  [:.albums] (html/content (map album-vm albums)))

(defn render-index [albums]
  (render-index-albums-template albums))

;; New page

(def ^:private new-albums-template (html/html-resource "public/html/albums/new.html"))

(html/deftemplate ^:private render-new-album-template new-albums-template [errors]
  [:.create-album] (html/do->
                    (html/set-attr :method "POST")
                    (html/set-attr :action "/albums"))
  [:.create-album-title-input.error] (html/content (:create-album-title errors)))

(defn render-new-album
  ([] (render-new-album nil))
  ([errors]
     (render-new-album-template errors)))


;; Edit page

(def ^:private edit-album (html/html-resource "public/html/albums/edit.html"))

(html/defsnippet thumbnail-model edit-album [[:.photo (html/nth-of-type 1)]]
  [{:keys [thumbnail-url]}]
  [:.photo] (html/set-attr :style (common/background-photo thumbnail-url)))

(html/defsnippet ^:private add-photo-button edit-album [:.add-photo-button] [id]
  [:form] (html/do->
           (html/set-attr :method "POST")
           (html/set-attr :action (str "/albums/" id "/photos"))
           (html/set-attr :enctype "multipart/form-data")))

(html/deftemplate ^:private render-edit-album-template edit-album [{:keys [id title photos]}]
  [:title]         (html/content "Edit Album: " title)
  [:.page-title ] (html/content title)
  [:.photos]       (html/content (map thumbnail-model photos))
  [:.photos]       (html/append (add-photo-button id)))

(def render-edit-album render-edit-album-template)
