(ns iPerture.albums.albums
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.neo :as neo]))

(defrecord Album [id title])

(defn album [id title]
  (Album. id title))

(defn create [title]
  (let [id (generate-unique-id)]
    (neo/create-child! :album (album id title))))

(defn find-by [id]
  (neo/find-by id :album))

(defn add-photo [id photo])
