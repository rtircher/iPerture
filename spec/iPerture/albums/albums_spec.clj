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
      (should-have-been-called-with albums/album
                                    ["id" "title"]
                                    (albums/create "title")))

    (it "should ask neo to create a new album child"
      (should-have-been-called-with neo/create-child!
                                    [:album (albums/album "id" "title")]
                                    (albums/create "title"))))

  (describe "find-by"
    (with album {:title "title"})
    (with photos ["photo1" "photo2"])

    (around [it]
        (with-redefs [neo/find-by (fn [& _] @album)
                      neo/find-rels (fn [& _] @photos)]
          (it)))

    (it "should include the album id"
      (should= "id" (:id (albums/find-by "id"))))

    (it "should include the album title"
      (should= "title" (:title (albums/find-by "id"))))

    (it "should return the photo contained in an album"
      (should= @photos (:photos (albums/find-by "id"))))))
