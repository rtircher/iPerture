(ns iPerture.albums.albums-view
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.views.common :as common]))

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
  [:.album-title ] (html/content title)
  [:.photos]       (html/content (map thumbnail-model photos))
  [:.photos]       (html/append (add-photo-button id)))

(def render-edit-album render-edit-album-template)
