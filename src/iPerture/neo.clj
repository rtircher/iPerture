(ns iPerture.neo
  (:use iPerture.config)
  (:require [borneo.core :as neo])
  (:import (org.neo4j.graphdb Transaction)))

(def ^{:dynamic true} *tx-autocommit* true)

(def database-path (config :db-path))

(def db-started? (ref false))

(defn ensure-db-started [path]
  (when (not @db-started?)
    (dosync
     (ref-set db-started? true))
    (neo/start! path)
    (let [^Runnable stopper neo/stop!]
      (.. Runtime (getRuntime) (addShutdownHook (new Thread stopper))))))

(defn new-tx []
  (dosync (ref-set neo/current-tx (.beginTx neo/*neo-db*))))

(defn ensure-db-started-no-stop [path]
  (when (not @db-started?)
    (dosync
     (ref-set db-started? true))
    (neo/start! path)
    (new-tx)))

(defn finish-current-tx []
  (when-let [^Transaction tx @neo/current-tx]
    (doto tx (.success) (.finish))))

(defn rollback-current-tx []
  (when-let [^Transaction tx @neo/current-tx]
    (doto tx (.failure) (.finish))))

(defmacro auto-tx [& body]
  `(do
     (when *tx-autocommit*
       (new-tx))
     (try
       (do ~@body)
       (finally
        (when *tx-autocommit*
          (finish-current-tx))))))

(defn stop-db []
  (when @db-started?
    (dosync
     (ref-set db-started? false))
    (alter-var-root #'neo/*neo-db* (fn [_] nil))))
