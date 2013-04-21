(ns iPerture.neo
  (:use iPerture.config)
  (:require [borneo.core :as neo]))

(defmacro ^:private with-local-db! [body]
  `(neo/with-local-db! (config :db-path)
     ~body))

(defn create-child!
  ([type props] (create-child! nil type props))
  ([node type props]
     (with-local-db!
       (if node
         (neo/create-child! node type props)
         (neo/create-child! type props)))
     props))

(defn find-by [id type]
  (with-local-db!
    (if-let [data (first (neo/traverse (neo/root)
                                       #(= id (:id (neo/props (:node %))))
                                       {:id id}
                                       type))]
      (neo/props data))))

(defn find-rels [node relationship]
  (when-let [children (neo/traverse node relationship)]
    (map neo/props children)))
