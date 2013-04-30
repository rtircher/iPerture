(ns iPerture.photos.photo-store-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [iPerture.config :only [config]])
  (:require [iPerture.photos.photo-store :as store]
            [clojure.java.io :as io]
            [iPerture.file-utils :as fu]))

(describe "photo-store"
  (with file-info {:tempfile "temp path"
                   :filename "photo filename"})

  (describe "save!"
    (it "should ask the config for the storage strategy"
      (should-have-been-called-with config
                                    [:photo-storage-strategy]
                                    (store/save! "album-id" @file-info)))

    (it "should call the copy-local! function when the storage strategy is defined as file"
      (with-redefs [config (fn [_] iPerture.photos.photo-store/copy-local!)]
        (should-have-been-called-with store/copy-local!
                                      ["album-id" @file-info]
                                      (store/save! "album-id" @file-info)))))

  (describe "copy-local!"
    (around [it]
        (with-redefs [io/make-parents (fn [& _])
                      io/copy (fn [_ _])
                      fu/create (fn [_])]
          (it)))

    (it "should create the file descriptor using 'target/data/album-id/photo file' path"
      (should-have-been-called-with io/file
                                    ["target/data/album-id/photo filename"]
                                    (store/copy-local! "album-id" @file-info)))

    (it "should create the parent directory"
      (should-have-been-called-with io/make-parents
                                    [(io/file "target/data/album-id/photo filename")]
                                    (store/copy-local! "album-id" @file-info)))

    (it "should create the file"
      (with-redefs [io/file (fn [_] "the file")]
        (should-have-been-called-with fu/create
                                      ["the file"]
                                      (store/copy-local! "album-id" @file-info))))

    (it "should copy the temp file to the permanent location"
      (with-redefs [io/file (fn [_] "permanent file")]
        (should-have-been-called-with io/copy
                                      ["temp path" "permanent file"]
                                      (store/copy-local! "album-id" @file-info))))

    (it "should return the permanent location file data"
      (should= "target/data/album-id/photo filename"
               (:url (store/copy-local! "album-id" @file-info))))
    
    (it "should append a version if the filename is already found in the permanent location")

    (it "should increment the version if there are multiple files with the same name")))
