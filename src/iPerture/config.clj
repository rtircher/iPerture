(ns iPerture.config)

(def symbol-to-property
  {:db-path {:external-name "db_path" :default "/var/iPerture/satyr_db"}
   })

(defn config [name]
  (when-let [{:keys [external-name default]} (symbol-to-property name)]
    (System/getProperty external-name default)))
