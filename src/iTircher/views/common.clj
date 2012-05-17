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


(defn- create-accept-map-from [vector]
  (if (even? (count vector))

    (apply hash-map vector)

    (let [but-last    (butlast vector)
          last-item (last vector)]
      (apply hash-map (conj but-last last-item :default)))))

(defn- convert-to-accept-headers [accept]
  (case accept
    :json      "application/json"
    :html      "text/html"
    :form-data "application/x-www-form-urlencode"
    :default   ""
    accept))

(defmacro execute-based-on-accept
  "Execute fn based on request accept headers"

  [req & clauses]

  `(let [accept#          (get (:headers ~req) "accept")
         accept?#         #(re-find (re-pattern %) accept#)
         accept-map#      (~create-accept-map-from '~clauses)
         accept-map#      (zipmap (map ~convert-to-accept-headers (keys accept-map#)) (vals accept-map#))
         first-accept-fn# (first (vals (filter #(accept?# (first %)) accept-map#)))]

     (eval first-accept-fn#)))
