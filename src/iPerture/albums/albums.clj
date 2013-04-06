(ns iPerture.albums.albums
  (:require [borneo.core :as neo]))

(defn create [title]
  (neo/with-db! "target/iPerture_db"
    (neo/create-child! :album {:id "2" :title title})))
