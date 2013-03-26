(ns iPerture.core
  (:use compojure.core
        [slingshot.slingshot :only [try+]]
        [noir.response :only [json]])
  (:require [compojure.handler                 :as handler]
            [compojure.route                   :as route]
            [iPerture.photos.photos-view       :as photo-view]
            [iPerture.albums.albums-controller :as albums-controller]
            [middlewares.logger]))

(defroutes site-routes
  (context "/photostreams/:album-id" [album-id :as {headers :headers}]
    (GET "/" [] (photo-view/render-stream-for album-id headers))
    (GET "/photos/:photo-id" [photo-id]
      (photo-view/render-stream-for album-id photo-id headers)))

  (context "/albums/" [:as {headers :headers}]
    (GET "/" [] (albums-controller/new))
    (POST "/" [] (albums-controller/create)))

  (route/resources "/")

  (route/not-found (slurp "resources/public/html/404.html")))

(defn- wrap-error-handling [handler]
  (fn [req]
    (try+
      (handler req)
      (catch [:type :not-found] {:keys [message]}
        (json {:error message} 404))
      (catch [:type :invalid] {:keys [message]}
        (json {:error message} 400)))))

(def app
  (-> (handler/site site-routes)
      (middlewares.logger/wrap-request-logging)
      wrap-error-handling))
