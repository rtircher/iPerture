(ns iPerture.core
  (:use compojure.core
        [slingshot.slingshot :only [try+]]
        [noir.response :only [json]])
  (:require [compojure.handler           :as handler]
            [compojure.route             :as route]
            [iPerture.views.photo-stream :as photo-stream]
            [middlewares.logger]))

(defroutes site-routes
  (context "/photostreams/:album-id" [album-id :as {headers :headers}]
    (GET "/" [] (photo-stream/render-stream-for album-id headers))
    (GET "/photos/:photo-id" [photo-id]
      (photo-stream/render-stream-for album-id photo-id headers)))

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
