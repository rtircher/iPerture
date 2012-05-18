(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common]
            [clojure.contrib.seq :as seq]
            [net.cgrand.enlive-html :as html])
  (:use [noir.core :only [defpage url-for]]
        [noir.request :only [ring-request]]
        [noir.response :only [json]]))

(def dummy-images
  [{:id "1"
    :image-url "/img/1.png" ;;-> Rename to photo-url
    :thumbnail-url "/img/1.png"}
   {:id "2"
    :image-url "/img/2.png"
    :thumbnail-url "/img/2.png"}
   {:id "3"
    :image-url "/img/3.png"
    :thumbnail-url "/img/3.png"}
   {:id "4"
    :image-url "/img/4.png"
    :thumbnail-url "/img/4.png"}
   {:id "5"
    :image-url "/img/5.png"
    :thumbnail-url "/img/5.png"}
   {:id "6"
    :image-url "/img/6.png"
    :thumbnail-url "/img/6.png"}
   {:id "7"
    :image-url "/img/7.png"
    :thumbnail-url "/img/7.png"}])

(def html-template (html/html-resource "public/html/photo_stream.html"))


(defn- create-image-from [url]
  (str "background-image:url('" url "')"))


(def image-selector [:.main-image :> html/first-child])
(html/defsnippet image-model html-template image-selector
  [{:keys [image-url]}]

  [:.image] (html/set-attr :style (create-image-from image-url)))


(def thumbnails-selector [:.thumbnails-list :> html/first-child])
(html/defsnippet thumbnail-model html-template thumbnails-selector
  [{:keys [photo-url thumbnail-url selected]}]

  [:.image] (html/do->
             (html/add-class (if selected "selected"))
             (html/set-attr :style (create-image-from thumbnail-url))
             (html/set-attr :href photo-url)))

(defn- select-fullscreen-image [images photo-id]
  (map #(assoc % :selected (= (:id %) photo-id)) images))

(defn- add-system-url-of-photos [images album-id]
  (map
   #(assoc % :photo-url ;; -> rename to system-url
           (url-for photo {:album-id album-id, :photo-id (:id %)}))
   images))


(defn- get-current-photo-index [images current-photo-id]
  (first (seq/positions #(= (:id %) current-photo-id) images)))

(defn- get-photo-at [images index]
  (get (vec images) index))

(defn- get-photo-url [images photo-index]
  (:photo-url (get-photo-at images photo-index)))


(html/deftemplate render-page html-template
  [images current-photo previous-photo-url next-photo-url]
  [:.previous.nav-button] (html/set-attr :href previous-photo-url)
  [:.next.nav-button](html/set-attr :href next-photo-url)
  [:.main-image] (html/content (image-model current-photo))
  [:.thumbnails-list] (html/content (map thumbnail-model images)))


(defn- get-images-for-presentation [album-id photo-id]
  (-> dummy-images ;; Will need to be replaced by fetching the db or
      ;; something similar
      (select-fullscreen-image photo-id)
      (add-system-url-of-photos album-id)))

(defpage photo "/photostream/:album-id/photo/:photo-id" {:keys [album-id photo-id]}
  (let
      [view-images         (get-images-for-presentation album-id photo-id)
       current-photo-index (get-current-photo-index view-images photo-id)
       current-photo       (get-photo-at view-images current-photo-index)
       next-photo-url      (get-photo-url view-images (inc current-photo-index))
       previous-photo-url  (get-photo-url view-images (dec current-photo-index))]

    (common/execute-based-on-accept
     (ring-request)

     :html #(render-page view-images current-photo previous-photo-url next-photo-url)

     :json #(json view-images))))