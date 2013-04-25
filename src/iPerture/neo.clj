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

(defn add-relationship! [node-id rel-type child]
  )

(defn- find-node [id type]
  (when-let [data (first (neo/traverse (neo/root)
                                       #(= id (:id (neo/props (:node %))))
                                       {:id id}
                                       type))]
    data))

(defn- find-rels [node relationship]
  (when-let [children (neo/traverse node relationship)]
    (doall (map neo/props children))))

(defn- do-find [id type & relationships]
  (when-let [node (find-node id type)]
    (->> relationships
         (map (fn [rel-name] {rel-name (find-rels node rel-name)}))
         (into {})
         (merge (neo/props node)))))

(defn find [id type & relationships]
  (with-local-db!
    (apply do-find id type relationships)))
