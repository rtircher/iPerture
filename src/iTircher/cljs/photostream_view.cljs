(ns photostream-view)

(def $ (js* "$"))

($ "ready"
   (fn []
     ;; (-> "a"
     ;;     $
     ;;     (.attr "href" "javascript:void(0);"))

     ;; (-> ($ ".previous.nav-button")
     ;;     (.on "click" #(js/alert "previous")))

     ;; (-> ($ ".next.nav-button")
     ;;     (.on "click"
     ;;          (fn []
     ;;            (-> ($ ".full.image")
     ;;                (.animate {"left" "-=50"} 3000))
     ;;            true)))

     (.log js/console "ready")))