(ns spin
  (:use [logger :only [log]]))

(def default-opts {
           :lines 11
           :length 8
           :width 2
           :radius 7
           :corners 0
           :rotate 11
           :direction 1
           :color "#fff"
           :speed 0.8
           :trail 29
           :shadow false
           :hwaccel true
           :className "spinner"
           :zIndex 2e9
           :top "auto"
           :left "auto"
           })

(defn create-and-append-spinner [target & opts]
  (log "adding spinner to " target)
  (let [options (clj->js (merge default-opts opts))
        spinner (js/Spinner. options)]
    (.spin spinner target)
    spinner))
