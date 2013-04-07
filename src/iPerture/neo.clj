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
