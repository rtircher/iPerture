(ns iPerture.albums.albums
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]
            [iPerture.photos.photos :as photos]
            compojure.response))

(defrecord Album [id title photos])

(extend-protocol compojure.response/Renderable
  Album
  (render [this _] (into {} this)))

(defn create [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (->Album id title []))))

(defn find-by [id]
  (when-let [album-data (neo/find id :album :photos)]
    (map->Album album-data)))

(defn add-photo [id photo]
  (neo/add-relationship! id :album photo :photos)
  photo)
