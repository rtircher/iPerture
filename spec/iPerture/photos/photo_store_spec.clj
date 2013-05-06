(ns iPerture.photos.photo-store-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [iPerture.config :only [config]])
  (:require [iPerture.photos.photo-store :as store]
            [clojure.java.io :as io]
            [iPerture.file-utils :as fu]))

(describe "photo-store"
  (with file-info {:tempfile "temp path"
                   :filename "photo filename"
                   :content-type "image/jpeg"
                   :size 1234})

  (describe "save!"
    (it "should resolve the strategy-function using the config storage strategy"
      (with-redefs [config (fn [_] :strategy)]
        (should-have-been-called-with resolve
                                      [(symbol "iPerture.photos.photo-store/copy-strategy!")]
                                      (store/save! "album-id" @file-info)
                                      {:and-return (fn [& _])})))

    (context "local"
      (around [it]
          (with-redefs [io/make-parents (fn [& _])
                        io/copy (fn [_ _])
                        fu/create (fn [_])
                        config (fn [_] :local)]
            (it)))

      (it "should call the copy-local! function when the storage strategy is defined as file"
        (should-have-been-called-with store/copy-local!
                                      ["album-id" @file-info]
                                      (store/save! "album-id" @file-info)))

      (it "should create the file descriptor using 'target/data/album-id/photo file' path"
        (should-have-been-called-with io/file
                                      ["resources/public/img/local/album-id/photo filename"]
                                      (store/save! "album-id" @file-info)))

      (it "should create the parent directory"
        (should-have-been-called-with io/make-parents
                                      [(io/file "resources/public/img/local/album-id/photo filename")]
                                      (store/save! "album-id" @file-info)))

      (it "should create the file"
        (with-redefs [io/file (fn [_] "the file")]
          (should-have-been-called-with fu/create
                                        ["the file"]
                                        (store/save! "album-id" @file-info))))

      (it "should copy the temp file to the permanent location"
        (with-redefs [io/file (fn [_] "permanent file")]
          (should-have-been-called-with io/copy
                                        ["temp path" "permanent file"]
                                        (store/save! "album-id" @file-info))))

      (it "should return the permanent location file data"
        (should= {:url "/img/local/album-id/photo filename"
                  :size 1234
                  :content-type "image/jpeg"
                  :filename "photo filename"}
                 (store/save! "album-id" @file-info)))

      (it "should append a version if the filename is already found in the permanent location")

      (it "should increment the version if there are multiple files with the same name"))

    (context "s3"
      (around [it]
          (with-redefs [config (fn [_] :s3)]
            (it)))

      (it "should call the copy-s3! function"
        (should-have-been-called-with store/copy-s3!
                                      ["album-id" @file-info]
                                      (store/save! "album-id" @file-info))))))
