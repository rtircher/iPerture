(ns iPerture.albums.albums
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]
            compojure.response))

(defrecord Album [id title photos])

(extend-protocol compojure.response/Renderable
  Album
  (render [this _] (into {} this)))

(defn create [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (->Album id title nil))))

(defn find-by [id]
  (when-let [{:keys [id title photos]} (neo/find id :album :photos)]
    (->Album id title (reverse photos))))

(defn add-photo [id photo thumbnail]
  (let [photo-record (photos/create (:url photo) (:url thumbnail))]
    (neo/add-relationship! id :album photo-record :photos)
    photo-record))
