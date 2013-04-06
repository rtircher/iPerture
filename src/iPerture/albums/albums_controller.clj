(ns iPerture.albums.albums-controller
;  (:use [valip.predicates :only [present]])
  (:require [iPerture.albums.albums-view :as view]
            [iPerture.albums.albums :as albums]
            [valip.core :as valip]
            [valip.predicates :as pred]))

(defn new []
  (view/render-new-album))

(defn- invalid [params]
  ;; (vali/rule (vali/has-value? title)
  ;;            [:create-album-title "Please enter an album title"])
  ;; (not (vali/errors? :create-album-title))

  (valip/validate params
                  [:create-album-title pred/present? "Please enter an album title"]))

(defn create [{title :create-album-title :as params}]
  (if-let [errors (invalid params)]
    (view/render-new-album errors)
    (do
      (albums/create title)
      (view/render-edit-album title))))
