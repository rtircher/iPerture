(ns iPerture.id-generator
  (:import java.util.UUID))

(defn generate-unique-id []
  (.toString (UUID/randomUUID)))
