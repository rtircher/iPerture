(ns albums
  (:require [net :as net]))

(def $ (js* "$"))

($ "ready"
   (fn []
     (-> ($ "#edit-album")
         (.on "submit" #(do
                          (.preventDefault %)
                          (js/console.log "Submitting form at url: " js/location.href)
                          (net/put js/location.href))))))
