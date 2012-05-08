(ns iTircher.server
  (:require [noir.server :as server])
  (:use [iTircher.middlewares.logger :only [wrap-request-loging]]))

(server/load-views "src/iTircher/views/")

(server/add-middleware wrap-request-loging)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'iTircher})))

