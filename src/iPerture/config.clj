(ns iPerture.config)

(def default-config {:db-path "/etc/iPerture/data/iPerture_db"
              :photo-storage-strategy :local})

(def development-config
  (merge default-config {:db-path "target/iPerture_db"
                         }))

(def test-config
  (merge default-config {:db-path "target/iPerture_db_test"
                         }))
 
(def staging-config
  (merge default-config {
                         }))

(def production-config
  (merge default-config {
                         }))

(defn config [name]
  (let [env (System/getProperty "iPerture.env" "default")]
    (get (eval (symbol (str "iPerture.config/" env "-config"))) name)))
