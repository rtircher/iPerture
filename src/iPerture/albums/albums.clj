(ns iPerture.albums.albums
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]))

(defrecord Album [id title photos])

(defn album
  ([id title] (album id title {}))
  ([id title photos] (Album. id title photos)))

(defn create [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (album id title))))

(defn find-by [id]
  (when-let [album-data (neo/find-by id :album)]
    (let [photos (neo/find-rels album-data :photo)]
      (album id (:title album-data) photos))))

(defn add-photo [id photo])
