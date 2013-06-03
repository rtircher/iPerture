(ns iPerture.photos.photos
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.albums.albums :as albums]
            compojure.response))

(defrecord Photo [id photo-url thumbnail-url])

(extend-protocol compojure.response/Renderable
  Photo
  (render [this _] (into {} this)))

(defn create [photo-url thumbnail-url]
  (Photo. (generate-unique-id) photo-url thumbnail-url))

(defn photos-from [album-id]
  (:photos (albums/find-by album-id)))
