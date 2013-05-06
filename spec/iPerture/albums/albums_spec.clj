(ns iPerture.albums.albums-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [iPerture.id-generator :only [generate-unique-id]])
  (:require [iPerture.albums.albums :as albums]
            [iPerture.neo :as neo]))

(describe "albums"
  (describe "create"
    (around [it]
        (with-redefs [neo/create-child! (fn [& args])
                      generate-unique-id (fn [] "id")]
          (it)))

    (it "should generate a new album id when created"
      (should-have-been-called-with albums/->Album
                                    ["id" "title" []]
                                    (albums/create "title")))

    (it "should ask neo to create a new album child"
      (should-have-been-called-with neo/create-child!
                                    [:album (albums/->Album "id" "title" [])]
                                    (albums/create "title"))))

  (describe "find-by"
    (it "should ask neo to find the album with the photos relationships"
      (should-have-been-called-with neo/find
                                    ["id" :album :photos]
                                    (albums/find-by "id")))

    (it "should return an album record"
      (with-redefs [neo/find (fn [& _] {:id "id" :title "title" :photos []})]
        (should= iPerture.albums.albums.Album (class (albums/find-by "id"))))))

  (describe "add-photo"
    (with photo-info {:id "id" :photo-url "photo-url" :thumbnail-url "thumbnail url"})
    (around [it]
        (with-redefs [neo/add-relationship! (fn [& args])
                      generate-unique-id (fn [] "id")]
          (it)))

    (it "should ask neo to add a photo relation"
      (should-have-been-called-with neo/add-relationship!
                                    ["album id" :album @photo-info :photos]
                                    (albums/add-photo "album id" @photo-info)))

    (it "should return the updated photo info"
      (should= @photo-info (albums/add-photo "album id" @photo-info)))))
