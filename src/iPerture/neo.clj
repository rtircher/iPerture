(ns iPerture.neo
  (:use iPerture.config)
  (:require [borneo.core :as neo]))

(defmacro ^:private with-local-db! [body]
  `(neo/with-db! (config :db-path)
     ~body))

(defn- do-create-child! [node type props]
  (if node
    (neo/create-child! node type props)
    (neo/create-child! type props))
  props)

(defn create-child!
  ([type props]
      (create-child! nil type props))
  ([node type props]
     (with-local-db! (do-create-child! node type props))))

(defn- find-node [id type]
  (when-let [data (first (neo/traverse (neo/root)
                                       #(= id (:id (neo/props (:node %))))
                                       {:id id}
                                       type))]
    data))

(defn- do-add-relationship! [parent-id parent-type child rel-type]
  (when-let [parent (find-node parent-id parent-type)]
    (neo/create-child! parent rel-type child)))

(defn add-relationship! [parent-id parent-type child rel-type]
  (with-local-db! (do-add-relationship! parent-id parent-type child rel-type)))

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

(defn find-all [type]
  (with-local-db!
    (doall (map neo/props (neo/traverse (neo/root) type)))))
