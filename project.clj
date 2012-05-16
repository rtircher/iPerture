(defproject iTircher "0.1.0"
  :description "Personal website written to test random stuffs with Clojure, ClojureScript,..."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [noir "1.2.1"]
                 [enlive "1.0.0"]]
  :dev-dependencies [[speclj "2.1.2"]]
  :test-path "spec/"
  :plugins [[lein-cljsbuild "0.1.8"]]
  :hooks [leiningen.cljsbuild]
  :main iTircher.server
  :cljsbuild {
    :builds [{
    ; The path to the top-level ClojureScript source directory:
    :source-path "src/iTircher/cljs"
    :compiler {
      :output-to "resources/public/js/main.js"
      ; The optimization level.  May be :whitespace, :simple, or :advanced.
      ; Defaults to :whitespace.
      :optimizations :whitespace
      ;; :externs ["externs/jquery.js"]
      :pretty-print true}}]})

