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
                               (photos/create-photo "photo url" "thumbnail photo")))
    
    (it "should generate a new photo id when created"
      (should= (photos/->Photo "id" "photo url" "thumbnail url")
               (photos/create-photo "photo url" "thumbnail url")))))

(describe "albums"
  (describe "create"
    (around [it]
        (with-redefs [neo/create-child! (fn [& args])
                      generate-unique-id (fn [] "id")]
          (it)))

    (it "should generate a new album id when created"
      (should-have-been-called-with photos/->Album
                                    ["id" "title" nil]
                                    (photos/create-album "title")))

    (it "should ask neo to create a new album child"
      (should-have-been-called-with neo/create-child!
                                    [:album (photos/->Album "id" "title" nil)]
                                    (photos/create-album "title"))))

  (describe "find-by"
    (it "should ask neo to find the album with the photos relationships"
      (should-have-been-called-with neo/find
                                    ["id" :album :photos]
                                    (photos/find-album-by "id")))

    (it "should return an album record"
      (with-redefs [neo/find (fn [& _] {:id "id" :title "title" :photos []})]
        (should= iPerture.photos.photos.Album (class (photos/find-album-by "id"))))))

  (describe "add-photo"
    (with photo-info {:url "photo-url"})
    (with thumbnail-info {:url "thumbnail-url"})

    (around [it]
        (with-redefs [neo/add-relationship! (fn [& args])
                      generate-unique-id (fn [] "id")]
          (it)))

    (it "should ask neo to add a photo relation"
      (let [expected-photo (photos/->Photo "id" "photo-url" "thumbnail-url")]
        (should-have-been-called-with neo/add-relationship!
                                      ["album id" :album expected-photo :photos]
                                      (photos/add-photo "album id" @photo-info @thumbnail-info))))

    (it "should return the updated photo record"
      (should= (photos/->Photo "id" "photo-url" "thumbnail-url")
               (photos/add-photo "album id" @photo-info @thumbnail-info)))))
