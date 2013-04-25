(ns iPerture.photos.photos-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.photos.photos :as photos]
            [iPerture.neo :as neo]))

(describe "photos"
  (describe "photo"
    (it "should create a Photo record"
      (should= iPerture.photos.photos.Photo
               (class (photos/photo "id" "full size url" "thumbnail url"))))

    (it "should contain the id key"
      (should= "id" (:id (photos/photo "id" "full size url" "thumbnail url"))))

    (it "should contain the photo-url key"
      (should= "full size url" (:photo-url (photos/photo "id" "full size url" "thumbnail url"))))

    (it "should contain the thumbnail-url key"
      (should= "thumbnail url" (:thumbnail-url (photos/photo "id" "full size url" "thumbnail url"))))))
