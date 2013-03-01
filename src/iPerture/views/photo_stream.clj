(ns iPerture.views.photo-stream
  (:use [noir.response :only [json]])
  (:require [iPerture.views.common :as common]
            [iPerture.photos :as photos]
            [net.cgrand.enlive-html :as html]))

(def html-template (html/html-resource "public/html/photo_stream.html"))


(defn- create-photo-from [url]
  (str "background-image:url('" url "')"))


(def photo-selector [:.main-photo :> html/first-child])
(html/defsnippet photo-model html-template photo-selector
  [{:keys [photo-url]}]

  [:.photo] (html/set-attr :style (create-photo-from photo-url)))


(def thumbnails-selector [:.thumbnails-list :> html/first-child])
(html/defsnippet thumbnail-model html-template thumbnails-selector
  [{:keys [page-url thumbnail-url selected]}]

  [:.photo] (html/do->
             (html/add-class (if selected "selected"))
             (html/set-attr :style (create-photo-from thumbnail-url))
             (html/set-attr :href page-url)))

(defn- select-fullscreen-photo [photos photo-id]
  (map #(assoc % :selected (= (:id %) photo-id)) photos))

(defn- photostream-url [album-id photo-id]
  (str "/photostream/" album-id "/photo/" photo-id))

(defn- add-page-url-of-photos [photos album-id]
  (map #(assoc % :page-url ;; -> rename to system-url
               (photostream-url album-id (:id %))) photos))

(defn- positions [f coll]
  (keep-indexed (fn [idx elt] (when (f elt) idx)) coll))

(defn- get-current-photo-index [photos current-photo-id]
  (first (positions #(= current-photo-id (:id %)) photos)))

(defn- get-photo-at [photos index]
  (get (vec photos) index))

(defn- get-page-url [photos photo-index]
  (:page-url (get-photo-at photos photo-index)))


(html/deftemplate render-page html-template
  [photos current-photo previous-page-url next-page-url]
  [:.previous.nav-button] (html/set-attr :href previous-page-url)
  [:.next.nav-button]     (html/set-attr :href next-page-url)
  [:.main-photo]          (html/content (photo-model current-photo))
  [:.thumbnails-list]     (html/content (map thumbnail-model photos)))

(html/deftemplate render-empty-page html-template []
  [:.slider-wrapper] (html/do->
                      (html/add-class "empty-album")
                      (html/content "This album does not exist")))

(html/deftemplate render-photo-not-found html-template []
  [:.slider-wrapper] (html/do->
                      (html/add-class "empty-album")
                      (html/content "Photo not found")))

(defn- get-photos-for-presentation [album-id displayed-photo-id]
  (when-let [photos (photos/get-album album-id)]
    (-> photos
        (select-fullscreen-photo displayed-photo-id)
        (add-page-url-of-photos album-id))))

(defn- render-photo-fn [photo-id view-photos]
  (if view-photos
    (if-let [current-photo-index (get-current-photo-index view-photos photo-id)]
      (let [current-photo     (get-photo-at view-photos current-photo-index)
            next-page-url     (get-page-url view-photos (inc current-photo-index))
            previous-page-url (get-page-url view-photos (dec current-photo-index))]
        #(render-page view-photos current-photo previous-page-url next-page-url))
      #(render-photo-not-found))
    #(render-empty-page)))

(defn render-stream-for
  ([album-id headers] (render-stream-for album-id "1" headers))
  ([album-id photo-id headers]
     (let [view-photos (get-photos-for-presentation album-id photo-id)]
       (common/execute-based-on-accept headers
                                       :html (render-photo-fn photo-id view-photos)
                                       :json #(json view-photos)))))
