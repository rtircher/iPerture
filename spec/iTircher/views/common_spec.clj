(ns iTircher.views.common-spec
  (:use [speclj.core] [iTircher.views.common]))

(describe "views.common"

  (let [create-accept-map-from #'iTircher.views.common/create-accept-map-from] ;; Binding private method to a local reference

    (describe "fn create-map-from-vector"

      (it "returns an empty map for an empty vector"
          (should= {}  (create-accept-map-from [])))

      (it "returns a map with 2 key values for an even vector of 4 items"
          (should= {:a :b, :c :d} (create-accept-map-from [:a :b :c :d])))

      (it "returns a map with 2 key values for an odd vector of 3 items"
          (should= 2 (count (create-accept-map-from [:a :b :c]))))

      (it "sets the key to be :default if the vector provided has an odd count"
          (def converted-map (create-accept-map-from [:a :b :c]))
          (should (contains? converted-map :default))
          (should= :c (:default converted-map)))))

  (let [convert-to-accept-headers #'iTircher.views.common/convert-to-accept-headers]
    (describe "fn convert-to-accept-headers"

      (it "maps :json to application/json"
          (should= "application/json" (convert-to-accept-headers :json)))

      (it "maps :html to text/html"
          (should= "text/html" (convert-to-accept-headers :html)))

      (it "maps :form-data to application/x-www-form-urlencode"
          (should=
           "application/x-www-form-urlencode"
           (convert-to-accept-headers :form-data)))

      (it "returns itself if the value is not found"
          (should= "myself" (convert-to-accept-headers "myself")))

      (it "maps :default to an empty string"
          (should= "" (convert-to-accept-headers :default)))
      ))

  (let [request (fn [accept] {:headers {"accept" accept}})]
    (describe "macro dispatch"

      (it "calls the :json function when the accept header contains application/json"
          (def result (dispatch
                       (request "foo/bar,application/json,foo/bar2")
                       :json (str "json fn")))
          (should= "json fn" result))

      (it "calls the :html function when the accept header contains text/html"
          (def result (dispatch
                       (request "foo/bar,text/html,foo/bar2")
                       :html (str "html fn")))
          (should= "html fn" result))

      (it "calls the :form-data function when the accept header contains application/x-www-form-urlencoded"
          (def result (dispatch
                       (request "application/x-www-form-urlencoded")
                       :form-data (str "form data fn")))
          (should= "form data fn" result))

      (it "calls the default function if the request headers do not match any of the other option provided"
          (def result
            (dispatch (request "application/something,text/html")
                      :json    (str "json")
                      (str "default fn")))
          (should= "default fn" result))

      (it "allows to define additional accept header handlers"
          (def result (dispatch (request "foo/bar") "foo/bar" (str "random accept fn")))
          (should= "random accept fn" result))

      ;; TODO: Want to do that one day but too complicated for now
      ;; (it "handles the accept handlers by priority order"
      ;;     (def result
      ;;       (dispatch (request "first,second")
      ;;                 "second" (str "second")
      ;;                 "first"  (str "first")))
      ;;     (should= "first" result))

      )))

(run-specs)