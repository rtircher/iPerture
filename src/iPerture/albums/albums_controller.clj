(ns iPerture.albums.albums-controller
  (:use [valip.predicates :only [present?]]
        [ring.util.response :only [redirect-after-post]]
        [iPerture.util.response :only [post-error-response]]
        [noir.response :only [json]])
  (:require [iPerture.albums.albums-view :as view]
            [iPerture.photos.photos :as photos]
            [iPerture.image-optimizer :as optimizer]
            [iPerture.photos.photo-store :as store]
            [valip.core :as valip]))

(defn new []
  (view/render-new-album))

(defn- invalid [params]
  (valip/validate params
                  [:create-album-title present? "Please enter an album title"]))

(defn create [{title :create-album-title :as params}]
  (if-let [errors (invalid params)]
    (post-error-response (view/render-new-album errors))
    (let [album (photos/create-album title)]
      (redirect-after-post (str "/albums/" (:id album) "/edit")))))

(defn edit [album-id]
  (when-let [album (photos/find-album-by album-id)]
    (view/render-edit-album album)))

(defn add-photo [album-id {:keys [photo]}]
  (let [optimized-photo (->> photo
                             optimizer/optimize-photo!
                             (store/save! album-id))
        thumbnail (->> photo
                       optimizer/optimize-thumbnail!
                       (store/save-thumbnail! album-id))]
    (json (photos/add-photo album-id optimized-photo thumbnail))))
