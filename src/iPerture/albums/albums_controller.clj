(ns iPerture.albums.albums-controller
  (:require [iPerture.albums.albums-view :as view]))

(defn new []
  (view/render-new-album))

(defn create []
  "create")
