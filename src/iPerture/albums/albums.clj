(ns iPerture.albums.albums
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]))

(defrecord Album [id title photos])

(defn album
  ([{:keys [id title photos]}] (album id title photos))
  ([id title] (album id title []))
  ([id title photos] (Album. id title photos)))

(defn create [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (album id title))))

(defn find-by [id]
  (when-let [album-data (neo/find id :album :photos)]
    (album album-data)))

(defn add-photo [id photo]
  (neo/add-relationship! id :photos photo))
