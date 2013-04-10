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

(def ^:private edit-album (html/html-resource "public/html/edit-album.html"))

(html/defsnippet thumbnail-model edit-album [[:.photo (html/nth-of-type 1)]]
  [{:keys [thumbnail-url]}]
  [:.photo] (html/do->
             (html/remove-class "template")
             (html/set-attr :style thumbnail-url)))

(html/deftemplate ^:private render-edit-album-template edit-album [album-title photos]
  [:title]         (html/content "Edit Album: " album-title)
  [:.album-title ] (html/content album-title)
  [[:.photo html/last-of-type]] (html/after (map thumbnail-model photos))
  ;; this is really dumb (I want to replace the photos from the
  ;; designed html by the the snippet result)
  [:.template]     (html/substitute nil))

(defn render-edit-album
  ([album-title] (render-edit-album album-title []))
  ([album-title photos]
     (render-edit-album-template album-title photos)))
