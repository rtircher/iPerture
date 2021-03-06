(defproject iPerture "0.1.0"
  :description "Personal website written to test random stuffs with Clojure, ClojureScript,..."
  :min-lein-version "2.1.1"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [borneo "0.4.0"]
                 [slingshot "0.10.3"]
                 [compojure "1.1.5"]
                 [lib-noir "0.4.9"]
                 [enlive "1.1.5"]
                 [enfocus "1.0.1"]
                 ;; [tailrecursion/javelin "1.0.0-SNAPSHOT"]
                 [rtircher.illusioniste "1.0.1"]
                 [org.apache.directory.studio/org.apache.commons.io "2.4"]
                 [com.cemerick/valip "0.3.2"]]

  :profiles {:dev {:plugins [[lein-cljsbuild "0.3.0"]
                             [lein-ring "0.8.3"]
                             [lein-haml-sass "0.2.7-SNAPSHOT"]]
                   :jvm-opts ["-DiPerture.env=development"
                              "-Dim4java.useGM=true" ; -> Use GraphicMagick instead of ImageMagick
                              ]
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

                   :haml {:src "src/iPerture/views/haml"
                          :output-directory "resources/public/html"
                          :output-extension "html"
                          }

                   :sass {:src "src/iPerture/views/sass"
                          :output-directory "resources/public/css"
                          :output-extension "css"
                          }

                   }

             :test {:dependencies [[speclj "2.1.2"]
                                   ;; [[ring-mock "0.1.2"]]
                                   [speclj-growl "1.0.1"]]
                   
                    :plugins [[speclj "2.5.0"]]
                    :test-paths ["spec/"]
                    :jvm-opts ["-DiPerture.env=test" "-Dim4java.useGM=true"]}

             :staging {:jvm-opts ["-DiPerture.env=staging" "-Dim4java.useGM=true"]}
             :production {:jvm-opts ["-DiPerture.env=production" "-Dim4java.useGM=true"]}}

  :ring {:handler iPerture.core/app
         :port 5000}
  )
