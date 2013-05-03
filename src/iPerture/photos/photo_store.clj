(ns iPerture.photos.photo-store
  (:use [iPerture.config :only [config]])
  (:require [clojure.java.io :as io]
            [iPerture.file-utils :as fu]))

(defn- select-photo-meta [file-info permanent-url]
  (-> file-info
      (select-keys [:filename :size :content-type])
      (assoc :url permanent-url)))

(defn- copy-local! [album-id file-info]
  (let [permanent-url (str "target/data/" album-id "/" (:filename file-info))
        output-file (io/file permanent-url)]
    (io/make-parents output-file)
    (fu/create output-file)
    (io/copy (:tempfile file-info) output-file)
    (select-photo-meta file-info permanent-url)))

(defn- copy-s3! [album-id file-info])

(defn- save-using-strategy []
  (let [strategy (name (config :photo-storage-strategy))]
    (resolve (symbol (str "iPerture.photos.photo-store/copy-" strategy "!")))))

(defn save! [album-id file-info]
  ((save-using-strategy) album-id file-info))
