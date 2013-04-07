(ns iPerture.config)

(def default {:db-path "/var/www/aps/iPerture/data/iPerture_db"
              })

(def development
  (merge default {:db-path "target/iPerture_db"
                  }))

(def staging
  (merge default {
                  }))

(def production
  (merge default {
                  }))

(defn config [name]
  (let [env (System/getProperty "iPerture.env" "default")]
    (get (eval (symbol (str "iPerture.config/" env))) name)))
