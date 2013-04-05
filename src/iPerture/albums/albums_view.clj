(ns iPerture.albums.albums-view
  (:require [net.cgrand.enlive-html :as html]))

(def ^:private new-albums-template (html/html-resource "public/html/albums.html"))

(html/deftemplate ^:private render-new-album-template new-albums-template []
  [:.create-album] (html/do->
                    (html/set-attr :method "POST")
                    (html/set-attr :action "/albums")))

(defn render-new-album
  ([] (render-new-album nil))
  ([error-message]
     (render-new-album-template)))