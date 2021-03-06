(ns iPerture.core
  (:use compojure.core
        [slingshot.slingshot  :only [try+]]
        [noir.response        :only [json]]
        [noir.util.middleware :only [wrap-strip-trailing-slash]])
  (:require [compojure.handler                 :as handler]
            [compojure.route                   :as route]
            [iPerture.photos.photos-view       :as photo-view]
            [iPerture.albums.albums-controller :as albums-controller]
            [middlewares.logger]))

(defroutes site-routes
  (context "/albums" {headers :headers}
    (GET "/" [] (albums-controller/index))
    (GET "/new" [] (albums-controller/new))
    (POST "/" {params :params} (albums-controller/create params))

    (context "/:album-id" [album-id]
      (GET "/" []
        (photo-view/render-stream-for album-id headers))
      (GET "/photos/:photo-id" [photo-id]
        (photo-view/render-stream-for album-id photo-id headers))
      (GET "/edit" []
        (albums-controller/edit album-id))
      (POST "/photos" [:as {params :multipart-params}]
        (albums-controller/add-photos album-id params))
      (GET "/add-photos-status/:job-id" [job-id]
        (albums-controller/add-photos-status job-id))))

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
      wrap-strip-trailing-slash
      (middlewares.logger/wrap-request-logging)
      wrap-error-handling))
