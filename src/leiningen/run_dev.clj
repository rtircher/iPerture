(ns leiningen.run-dev
  (:use [clojure.java.shell :only [sh]]))

(defn run-dev [& args]
  "Starts lein haml-sass, lein cljs-build, and lein run"
  (sh "lein" "haml-sass" "auto")
  ;; cljs-build auto compile here
  (sh "lein" "run"))