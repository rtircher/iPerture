(ns iPerture.image-optimizer-spec
  (:use [speclj.core]
        [iPerture.spec-helper])
  (:require [iPerture.image-optimizer :as optimizer]))

(describe "#calculate-thumbnail-dimensions"
  (with calculate-thumbnail-dimensions #'iPerture.image-optimizer/calculate-thumbnail-dimensions)

  (it "should return the original dimension if width is smaller than 150 px"
    (should= {:width 100
              :height 300}
             (@calculate-thumbnail-dimensions 100 300)))

  (it "should return the original dimension if height is smaller than 150 px"
    (should= {:width 200
              :height 100}
             (@calculate-thumbnail-dimensions 200 100)))

  (context "when width is smaller than height"
    (it "should have a width of 150"
      (should= 150
               (:width (@calculate-thumbnail-dimensions 200 300))))

    (it "should keep the aspect ratio for height"
      (should= 225
               (:height (@calculate-thumbnail-dimensions 200 300)))))

  (context "when height is smaller than width"
    (it "should have a height of 150"
      (should= 150
               (:height (@calculate-thumbnail-dimensions 400 200))))

    (it "should keep the aspect ratio for height"
      (should= 300
               (:width (@calculate-thumbnail-dimensions 400 200))))))
