(ns iPerture.albums.albums-view
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private new-albums-template (html/html-resource "public/html/albums.html"))

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

(def ^:private edit-album (html/html-resource "public/html/edit-album.html"))

(html/defsnippet thumbnail-model edit-album [[:.photo (html/nth-of-type 1)]]
  [{:keys [thumbnail-url]}]
  [:.photo] (html/do->
             (html/remove-class "template")
             (html/set-attr :style thumbnail-url)))

(html/deftemplate ^:private render-edit-album-template edit-album [{:keys [id title photos]}]
  [:title]         (html/content "Edit Album: " title)
  [:.album-title ] (html/content title)
  [[:.photo html/last-of-type]] (html/after (map thumbnail-model photos))
  ;; this is really dumb (I want to replace the photos from the
  ;; designed html by the the snippet result)
  [:.template]     (html/substitute nil))

(def render-edit-album render-edit-album-template)
