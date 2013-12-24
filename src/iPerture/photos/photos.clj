(ns iPerture.photos.photos
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]
            compojure.response))

(defrecord Photo [id photo-url thumbnail-url])
(defrecord Album [id title photos])

(extend-protocol compojure.response/Renderable
  Photo
  (render [this _] (into {} this)))

(extend-protocol compojure.response/Renderable
  Album
  (render [this _] (into {} this)))

(defn create-photo [photo-url thumbnail-url]
  (Photo. (generate-unique-id) photo-url thumbnail-url))

(defn find-all-albums []
  (map map->Album (neo/find-all :album)))

(defn find-album-by [album-id]
  (when-let [{:keys [title photos]} (neo/find album-id :album :photos)]
    (->Album album-id title (reverse photos))))

(defn photos-from [album-id]
  (:photos (find-album-by album-id)))

(defn create-album [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (->Album id title nil))))

(defn add-photo [album-id photo thumbnail]
  (let [photo-record (create-photo (:url photo) (:url thumbnail))]
    (neo/add-relationship! album-id :album photo-record :photos)
    photo-record))
