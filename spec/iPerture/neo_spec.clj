(ns iPerture.neo-spec
  (:use [speclj.core]
        [iPerture.spec-helper])
  (:require [iPerture.neo :as neo]
            [borneo.core :as borneo]))

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
          (should= expected (@do-find "id" :type :rel1 :rel2)))))))
