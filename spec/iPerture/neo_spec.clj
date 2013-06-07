(ns iPerture.neo-spec
  (:use [speclj.core]
        [iPerture.spec-helper])
  (:require [iPerture.neo :as neo]
            [kalimantan.core :as borneo]))

(describe "neo"
  (around [it]
      (with-redefs [borneo/traverse #()
                    borneo/root #()
                    borneo/props identity]
        (it)))

  (describe "do-find"
    (with do-find #'iPerture.neo/do-find)

    (it "should ask neo for the object "
      (let [expected {:id "id" :other "stuff"}]
        (with-redefs [borneo/traverse (fn [& _] [expected])]
          (should= expected (@do-find "id" :type)))))

    (it "should add the relationships in the returned map"
      (let [expected {:id "id"
                      :rel1 [{:id "rel-id"}]
                      :rel2 [{:id "rel-id"}]}]
        (with-redefs [borneo/traverse (fn
                                        ([_ _ _ _] [{:id "id"}])
                                        ([_ _] [{:id "rel-id"}]))]
          (should= expected (@do-find "id" :type :rel1 :rel2))))))

  (describe "do-add-relationship!"
    (with do-add-relationship! #'iPerture.neo/do-add-relationship!)

    (it "should return nil if the parent was not found"
      (with-redefs [borneo/traverse (fn [_ _ _ _] [])]
        (should-be-nil (@do-add-relationship! "id" :parent-type {:id "child id"} :type))))

    (context "when parent was found"
      (with parent {:id "parent-id"})
      (around [it]
          (with-redefs [borneo/traverse (fn ([_ _ _ _] [@parent]))]
            (it)))

      (it "should ask neo to create a child of the parent"
        (let [child {:id "child id"}]
          (should-have-been-called-with borneo/create-child!
                                        [@parent :type child]
                                        (@do-add-relationship! "parent-id"
                                                               :parent-type
                                                               child
                                                               :type)))))))
