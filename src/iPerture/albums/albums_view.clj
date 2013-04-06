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

(def ^:private edit-album-template (html/html-resource "public/html/edit-album.html"))

(html/deftemplate ^:private render-edit-album-template edit-album-template [album-title photos]
  [:title]        (html/content "Edit Album: " album-title)
  [:.album-title] (html/content album-title))

(defn render-edit-album
  ([album-title] (render-edit-album album-title []))
  ([album-title photos]
     (render-edit-album-template album-title photos)))
