(ns iPerture.photos
  (:use clojure.pprint)
  (:require [borneo.core :as neo]))

(def dummy-images
  [{:id "1"
    :photo-url "/img/1.png"
    :thumbnail-url "/img/1.png"}
   {:id "2"
    :photo-url "/img/2.png"
    :thumbnail-url "/img/2.png"}
   {:id "3"
    :photo-url "/img/3.png"
    :thumbnail-url "/img/3.png"}
   {:id "4"
    :photo-url "/img/4.png"
    :thumbnail-url "/img/4.png"}
   {:id "5"
    :photo-url "/img/5.png"
    :thumbnail-url "/img/5.png"}
   {:id "6"
    :photo-url "/img/6.png"
    :thumbnail-url "/img/6.png"}
   {:id "7"
    :photo-url "/img/7.png"
    :thumbnail-url "/img/7.png"}])

(defn populate-db []
  (neo/with-db! "target/iPerture_db"
    ;; Clear DB
    (neo/purge!)
    (let [album (neo/create-child! :album {:id 1 :name "My first album"})]
      (doall (map #(neo/create-child! album :photo %) dummy-images)))))

(defn get-album [album-id]
  (neo/with-db! "target/iPerture_db"
    (when-let [album (first (neo/traverse (neo/root)
                                          {:id album-id}
                                          :album))]
      (when-let [photos (neo/traverse album :photo)]
        (sort-by :id (map neo/props photos))))))

(defn -main [& args]
  (populate-db)

  (neo/with-db! "target/iPerture_db"
    (println "All nodes")
    (pprint (map neo/props (neo/all-nodes)))

    (println "All Albums")
    (pprint (map neo/props (neo/traverse (neo/root) :album)))

    (println "Album with id 1")
    (pprint (neo/props (first (neo/traverse (neo/root)
                                            {:id 1}
                                            :album))))

    (println "All photos from first album")
    (let [first-ablum (first (neo/traverse (neo/root) :album))]
      (pprint (map neo/props (neo/traverse first-ablum :photo))))))
