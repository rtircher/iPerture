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

  (describe "find"
    (it "should ask neo to find the node using the id and the :album node type"
      (should-have-been-called-with neo/find
                                    ["id" :album]
                                    (albums/find "id")))))
