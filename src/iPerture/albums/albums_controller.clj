(ns iPerture.albums.albums-controller
  (:require [iPerture.albums.albums-view :as view]))

(defn new []
  (view/render-new-album))

(defn create [params]
  (let [title (:create-album-title params)]
    (view/render-new-album "Please enter an album title")))
