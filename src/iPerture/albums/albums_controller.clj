(ns iPerture.albums.albums-controller
  (:use [valip.predicates       :only [present?]]
        [ring.util.response     :only [redirect-after-post]]
        [iPerture.util.response :only [post-error-response]]
        [noir.response          :only [json]])
  (:require [iPerture.albums.albums-view :as view]
            [iPerture.photos.photos      :as photos]
            [iPerture.image-optimizer    :as optimizer]
            [iPerture.photos.photo-store :as store]
            [iPerture.delayed-job        :as dj]
            [valip.core                  :as valip]))

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

(defn- do-add-photo [album-id photo]
  (let [optimized-photo (->> photo
                             optimizer/optimize-photo!
                             (store/save! album-id))
        thumbnail (->> photo
                       optimizer/optimize-thumbnail!
                       (store/save-thumbnail! album-id))]
    (photos/add-photo album-id optimized-photo thumbnail)))

(defn- ensure-coll [coll]
  (if (vector? coll) coll [coll]))

(defn add-photos [album-id params]
  (let [photos (ensure-coll (get params "photos"))
        job-id (dj/submit-job
                ;; -> can we parallelize this?
                #(map (partial do-add-photo album-id) photos))
        status-url (str "/albums/" album-id "/add-photos-status/" job-id)]
    {:status 201
     :headers {"Location" status-url}
     :body status-url}))

(defn add-photos-status [job-id]
  (when-let [job-result (dj/result job-id)]
    (if-not (= :processing job-result) (dj/remove-job job-id))
    (json job-result)))
