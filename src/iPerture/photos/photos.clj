(ns iPerture.photos.photos
  (:use [iPerture.id-generator :only [generate-unique-id]])
  (:require [borneo.core :as neo]
            compojure.response))

(defrecord Photo [id photo-url thumbnail-url])

(extend-protocol compojure.response/Renderable
  Photo
  (render [this _] (into {} this)))

(defn create [photo-url thumbnail-url]
  (Photo. (generate-unique-id) photo-url thumbnail-url))

(defn get-album [album-id]
  (neo/with-db! "target/iPerture_db"
    (when-let [album (first (neo/traverse (neo/root)
                                          {:id album-id}
                                          :album))]
      (when-let [photos (neo/traverse album :photo)]
        (sort-by :id (map neo/props photos))))))
