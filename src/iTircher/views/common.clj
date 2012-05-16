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

(defmacro dispatch
  "Execute fn based on request accept headers"

  [req & clauses]
  
  `(let [create-map-from-vector# ~#'iTircher.views.common/create-map-from-vector
         accept#  (get (:headers ~req) "accept")
         accept?# #(re-find (re-pattern (str "^" %)) accept#)
         key#     (cond
                   (accept?# "application/json") :json
                   (accept?# "text/html")        :html
                   (accept?# "")                 :form-data
                   :else                         :default)
         accept-fn# (key# (create-map-from-vector# '~clauses))]

     accept-fn#))


(let [brol #(print "blah")] (brol))


(defmacro tm [arg] `(let [brol# (:test ~arg)] brol#))
(tm {:test (print "blah")})

;;(pprint  (macroexpand-1 (dispatch {:headers {"accept" "text/html"}} :html  (print "test"))))