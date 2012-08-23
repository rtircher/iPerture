(ns iTircher.photos
  )


;; (neo/with-db! "matrix-db"

  

;; )

(def dummy-images
  [{:id "1"
    :photo-url "/img/1.png" ;;-> Rename to photo-url
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

(defn get-album [album-id]
  
  dummy-images)

