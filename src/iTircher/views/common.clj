(ns iTircher.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "iTircher"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))


(defn- create-map-from-vector [vector]
  (if (even? (count vector))

    (apply hash-map vector)

    (let [but-last    (butlast vector)
          last-item (last vector)]
      (apply hash-map (conj but-last last-item :default)))))
