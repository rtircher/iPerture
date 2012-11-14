(ns iPerture.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "iPerture"]
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

(defn execute-based-on-accept
  "Execute fn based on request accept headers"
  [req & clauses]

  (let [accept          (get (:headers req) "accept")
        accept?         #(re-find (re-pattern %) accept)
        accept-map      (into {} (for [[k,v] (create-accept-map-from clauses)]
                                   [(convert-to-accept-headers k) v]))
        first-accept-fn (first (vals (filter #(accept? (first %)) accept-map)))]
    (if first-accept-fn (first-accept-fn))))
