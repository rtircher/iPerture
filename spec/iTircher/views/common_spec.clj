(ns iTircher.views.common-spec
  (:use [speclj.core] [iTircher.views.common]))

(describe "views.common"

  (let [create-map-from-vector #'iTircher.views.common/create-map-from-vector] ;; Binding private method to a local reference

    (describe "fn create-map-from-vector"

      (it "returns an empty map for an empty vector"
          (should= {}  (create-map-from-vector [])))

      (it "returns a map with 2 key values for an even vector of 4 items"
          (should= {:a :b, :c :d} (create-map-from-vector [:a :b :c :d])))

      (it "returns a map with 2 key values for an odd vector of 3 items"
          (should= 2 (count (create-map-from-vector [:a :b :c]))))

      (it "sets the key to be :default if the vector provided has an odd count"
          (def converted-map (create-map-from-vector [:a :b :c]))
          (should (contains? converted-map :default))
          (should= :c (:default converted-map)))))

  (let [request (fn [accept] {:headers {"accept" accept}})]
    (describe "macro dispatch"

      (it "calls the :json function when the accept header contains application/json"
          (def result (dispatch (request "application/json") :json (fn [] "json fn")))
          (should= "json fn" result))

      (it "calls the :html function when the accept header contains text/html"
          (def result (dispatch (request "text/html") :html "html fn"))
          (should= "html fn" result))

      )))

(run-specs)