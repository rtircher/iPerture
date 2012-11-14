(ns iPerture.server
  (:require [noir.server :as server]
            [iPerture.middlewares.logger :as logger]))

(server/load-views "src/iPerture/views/")

(server/add-middleware logger/wrap-request-logging)

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "5000"))]

    (server/start port {:mode mode
                        :ns 'iPerture})))

