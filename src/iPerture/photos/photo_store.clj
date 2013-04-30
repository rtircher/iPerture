(ns iPerture.photos.photo-store
  (:use [iPerture.config :only [config]])
  (:require [clojure.java.io :as io]
            [iPerture.file-utils :as fu]))

(defn copy-local! [album-id file-info]
  (let [permanent-url (str "target/data/" album-id "/" (:filename file-info))
        output-file (io/file permanent-url)]
    (io/make-parents output-file)
    (fu/create output-file)
    (io/copy (:tempfile file-info) output-file)
    (assoc file-info :url permanent-url)))

(defn save! [album-id file-info]
  ((config :photo-storage-strategy) album-id file-info))
