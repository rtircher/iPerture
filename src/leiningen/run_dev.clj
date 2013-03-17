(ns leiningen.run-dev
  (:use [pallet.stevedore :only (script with-script-language)]
        [pallet.script :only (with-script-context)]
        [pallet.local.execute :only (local-script)]
        [leiningen.ring :only (ring)]
        [leiningen.haml-sass :only (haml-sass)])
  (:require pallet.stevedore.bash))

(defn- thread-for-task [task project & args]
  (Thread. (fn [] (apply task project args))))

(defn- compile-haml-sass [project]
  (thread-for-task haml-sass project "auto"))

(defn- run-server [project]
  (thread-for-task ring "server-headless" project))

(defn run-dev
  "Starts lein haml-sass, lein cljs-build, and lein run"
  [project & args]

  (println (local-script

             ;; (script)
             ("ls -la ."
              (println "Hello"))

             ;; (with-script-language :pallet.stevedore.bash/bash
             ;;   (with-script-context [:darwin]
             ;;     ))

             ))

  ;; (let [t (Thread. (fn [] (with-bindings* {#'*warn-on-reflection* true} (ring project "server-headless"))))]
  ;;   (.start t)
  ;;   (.join t))
  
  ;; (ring project "server-headless")

  ;; (let [threads [(compile-haml-sass project) (run-server project)]]
  ;;      (doseq [t threads] (.start t))
  ;;      (doseq [t threads] (.join t)))
  )
;;(sh "lein" "haml-sass" "auto")
;; cljs-build auto compile here
;; (sh "lein" "ring" "server-headless")
