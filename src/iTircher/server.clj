(ns iTircher.server
  (:require [noir.server :as server]
            [iTircher.middlewares.logger :as logger]))

(server/load-views "src/iTircher/views/")

(server/add-middleware logger/wrap-request-logging)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "5000"))]

    (server/start port {:mode mode
                        :ns 'iTircher})))

