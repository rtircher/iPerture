(ns iPerture.albums.albums-controller
  (:use [valip.predicates :only [present?]]
        [ring.util.response :only [redirect-after-post]]
        [iPerture.util.response :only [post-error-response]])
  (:require [iPerture.albums.albums-view :as view]
            [iPerture.albums.albums :as albums]
            [valip.core :as valip]))

(defn new []
  (view/render-new-album))

(defn- invalid [params]
  (valip/validate params
                  [:create-album-title present? "Please enter an album title"]))

(defn create [{title :create-album-title :as params}]
  (if-let [errors (invalid params)]
    (post-error-response (view/render-new-album errors))
    (let [album (albums/create title)]
      (redirect-after-post (str "/albums/" (:id album))))))

(defn edit [album-id]
  (when-let [album (albums/find-by album-id)]
    (view/render-edit-album album)))

(defn add-photo [album-id params]
  (albums/add-photo album-id (:photo params)))
