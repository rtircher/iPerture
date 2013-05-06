(ns iPerture.photos.photos-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.photos.photos :as photos]
            [iPerture.neo :as neo]))

(describe "photos"
  (describe "create"
    (around [it]
        (with-redefs [generate-unique-id (fn [] "id")]
          (it)))

    (it "should call the generate-unique-id function"
      (should-have-been-called generate-unique-id
                               (photos/create "photo url" "thumbnail photo")))
    
    (it "should generate a new photo id when created"
      (should= (photos/->Photo "id" "photo url" "thumbnail url")
               (photos/create "photo url" "thumbnail url")))))
