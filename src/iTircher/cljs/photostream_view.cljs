(ns photostream-view)

(def jquery (js* "$"))

(jquery
   (fn []
     (-> (jquery ".previous.nav-button")
         (.on "click" #(js/alert "test")))))