(defproject iPerture "0.1.0"
  :description "Personal website written to test random stuffs with Clojure, ClojureScript,..."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [borneo "0.3.0"]
                 [slingshot "0.10.3"]
                 [compojure "1.1.5"]
                 [lib-noir "0.4.9"]
                 [enlive "1.1.1"]
                 [com.cemerick/valip "0.3.2"]]

  :profiles {:dev {:dependencies [[speclj "2.1.2"]
                                  ;; [[ring-mock "0.1.2"]]
                                  [speclj-growl "1.0.1"]]
                   :plugins [[lein-cljsbuild "0.3.0"]
                             [lein-haml-sass "0.2.6"]
                             [lein-ring "0.8.3"]
                             [speclj "2.5.0"]]
                   :test-paths ["spec/"]
                   :jvm-opts ["-DiPerture.env=development"]
                   :cljsbuild {:builds [{
                                         ;; The path to the top-level ClojureScript source directory:
                                         :source-paths ["src/iPerture/cljs"],
                                         :compiler
                                         {:pretty-print true
                                          :output-to "resources/public/js/main.js"
                                          ;; :externs ["externs/jquery.js"]
                                          ;; The optimization level.  May be :whitespace, :simple, or :advanced
                                          ;; Defaults to :whitespace
                                          :optimizations :whitespace}}]}

                   :haml {:src "src/iPerture/views/haml/"
                          :output-directory "resources/public/html"
                          :output-extension "html"
                          :gem-version "4.0.1"}

                   :sass {:src "src/iPerture/views/sass/"
                          :output-directory "resources/public/css"
                          :output-extension "css"
                          :style :compressed
                          :gem-version "3.2.2"}
}
             :staging {:jvm-opts ["-DiPerture.env=staging"]}
             :production {:jvm-opts ["-DiPerture.env=production"]}}

  :ring {:handler iPerture.core/app
         :port 5000}
)
