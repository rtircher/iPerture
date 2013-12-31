(ns iPerture.config)

(def default {:db-path "/etc/iPerture/data/iPerture_db"
              :photo-storage-strategy :local})

(def development
  (merge default {:db-path "target/iPerture_db"
                  }))

(def test
  (merge default {:db-path "target/iPerture_db_test"
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
