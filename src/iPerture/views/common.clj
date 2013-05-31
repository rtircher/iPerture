(ns iPerture.views.common)

(defn- conj-before-last [vector value]
  (concat (butlast vector) [value (last vector)]))

(defn- create-accept-map-from [vector]
  (apply hash-map (if (even? (count vector))
             vector
             (conj-before-last vector :default))))

(defn- convert-to-accept-headers [accept]
  (case accept
    :json      "application/json"
    :html      "text/html"
    :form-data "application/x-www-form-urlencode"
    :default   ""
    accept))

(defn execute-based-on-accept
  "Execute fn based on request accept headers"
  [headers & clauses]

  (let [accept          (get headers "accept")
        accept?         #(re-find (re-pattern %) accept)
        accept-map      (into {} (for [[k,v] (create-accept-map-from clauses)]
                                   [(convert-to-accept-headers k) v]))
        first-accept-fn (first (vals (filter #(accept? (first %)) accept-map)))]
    (if first-accept-fn (first-accept-fn))))

(defn background-photo [photo-url]
  (str "background-image:url('" photo-url "')"))
