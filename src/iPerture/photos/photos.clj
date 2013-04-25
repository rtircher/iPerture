(ns iPerture.photos.photos
  (:require [borneo.core :as neo]))

(defrecord Photo [id photo-url thumbnail-url])

(defn photo [id photo-url thumbnail-url]
  (Photo. id photo-url thumbnail-url))


(defn get-album [album-id]
  (neo/with-db! "target/iPerture_db"
    (when-let [album (first (neo/traverse (neo/root)
                                          {:id album-id}
                                          :album))]
      (when-let [photos (neo/traverse album :photo)]
        (sort-by :id (map neo/props photos))))))
