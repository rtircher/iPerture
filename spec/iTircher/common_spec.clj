(ns iTircher.common-spec
  (:use [speclj.core]))

(defn true-or-false []
  true)
  
(describe "common"
  (it "provides a example test"
      (should= 2 (+ 1 1)))
  (it "fails"
      (should-not (= 3 (- 3 1)))))

(run-specs)