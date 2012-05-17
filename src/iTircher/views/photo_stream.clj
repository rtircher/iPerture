(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common]
            [clojure.contrib.seq :as seq]
            [net.cgrand.enlive-html :as html])
  (:use [noir.core :only [defpage url-for]]
        [noir.request :only [ring-request]]))

(def dummy-images
  [{:id "1"
    :image-url "/img/1.png"
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


(def image-selector [:.images-list :> html/first-child])
(html/defsnippet image-model html-template image-selector
  [{:keys [image-url selected]}]

  [:li] (html/add-class (if-not selected "hidden"))
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
   #(assoc % :photo-url
           (url-for photo {:album-id album-id, :photo-id (:id %)}))
   images))


(defn- get-current-photo-index [images current-photo-id]
  (first (seq/positions #(= (:id %) current-photo-id) images)))

(defn- get-photo-url [images at-index]
  (:photo-url (get (vec images) at-index)))


(html/deftemplate render-page html-template
  [images previous-photo-url next-photo-url]
  [:.previous.nav-button] (html/set-attr :href previous-photo-url)
  [:.next.nav-button](html/set-attr :href next-photo-url)
  [:.images-list] (html/content (map image-model images))
  [:.thumbnails-list] (html/content (map thumbnail-model images)))


(defn- get-images-for-presentation [images album-id photo-id]
  (-> images
      (select-fullscreen-image photo-id)
      (add-system-url-of-photos album-id)))

(defpage photo "/photostream/:album-id/photo/:photo-id" {:keys [album-id photo-id]}
  (let
      [view-images         (get-images-for-presentation dummy-images album-id photo-id)
       current-photo-index (get-current-photo-index view-images photo-id)
       current-photo-url   (get-photo-url view-images current-photo-index)
       next-photo-url      (get-photo-url view-images (dec current-photo-index))
       previous-photo-url  (get-photo-url view-images (inc  current-photo-index))]

    (common/execute-based-on-accept
     (ring-request)

     :html #(render-page view-images previous-photo-url next-photo-url)

     :json #(str "json"))))