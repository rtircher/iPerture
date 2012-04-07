(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common]
            [net.cgrand.enlive-html :as html])
  (:use [noir.core :only [defpage]]))

(def dummy-images
  [{:image-url "/img/1.png"
    :thumbnail-url "/img/1.png"}
   {:image-url "/img/2.png"
    :thumbnail-url "/img/2.png"}
   {:image-url "/img/3.png"
    :thumbnail-url "/img/3.png"}
   {:image-url "/img/4.png"
    :thumbnail-url "/img/4.png"}
   {:image-url "/img/5.png"
    :thumbnail-url "/img/5.png"}
   {:image-url "/img/6.png"
    :thumbnail-url "/img/6.png"}
   {:image-url "/img/7.png"
    :thumbnail-url "img/7.png"}])

(def html-template (html/html-resource "public/html/index.html"))

(defn create-image-from [url]
  (str "background-image:url('" url "')"))

(def image-sel [:.images-list :> html/first-child])
(html/defsnippet image-model html-template image-sel
  [{:keys [image-url]}]
  [:.image] (html/set-attr :style (create-image-from image-url)))

(def thumbnails-sel [:.thumbnails-list :> html/first-child])
(html/defsnippet thumbnail-model html-template thumbnails-sel
  [{:keys [thumbnail-url]}]
  [:.image] (html/set-attr :style (create-image-from thumbnail-url)))

(html/deftemplate photo-stream-page html-template
  [images]
  [:.images-list] (html/content (map image-model images))
  [:.thumbnails-list] (html/content (map thumbnail-model images)))

(defpage "/photostream" [] (photo-stream-page dummy-images))
