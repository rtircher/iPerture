(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [ring.util.response :only [redirect-after-post]]
        [noir.response :only [json]])
  (:require [iPerture.albums.albums-controller :as controller]
            [iPerture.albums.albums-view       :as view]
            [iPerture.photos.photos            :as photos]
            [iPerture.image-optimizer          :as optimizer]
            [iPerture.delayed-job              :as dj]
            [iPerture.photos.photo-store       :as store]))

(describe "albums-controller"

  (describe "/index"
    (it "should ask the view to render the albums"
      (let [albums [{:id "album1"}, {:id "album2"}]]
        (with-redefs [photos/find-all-albums (fn []  albums)]
          (should-have-been-called-with view/render-index
                                        [albums]
                                        (controller/index))))))
  
  (describe "/new"
    (it "should call the render new album view function"
      (should-have-been-called view/render-new-album
                               (controller/new))))

  (describe "/create"
    (with-all params {:create-album-title "title"})

    (it "should create a new album with the provided title"
      (should-have-been-called-with photos/create-album
                                    ["title"]
                                    (controller/create @params)))

    (it "should ask the view to render an empty edit album page with the provided title"
      (with-redefs [photos/create-album (fn [title] (photos/->Album "id" title []))]
        (should-have-been-called-with redirect-after-post
                                      ["/albums/id/edit"]
                                      (controller/create @params))))

    (context "when the album title is empty"
      (with-all params {:create-album-title ""})

      (it "should stay on the new page"
        (should-have-been-called view/render-new-album
                                 (controller/create @params)))

      (it "should return a 422 error"
        (should= 422 (:status (controller/create @params))))

      (it "should ask the view to display an error message"
        (should-have-been-called-with view/render-new-album
                                      [{:create-album-title ["Please enter an album title"]}]
                                      (controller/create @params)))

      (it "should not create a new album"
        (should-not-have-been-called photos/create-album
                                     (controller/create @params)))))

  (describe "edit"
    (it "should find the album based on the provided id"
      (with-redefs [view/render-edit-album (fn [& _])]
        (should-have-been-called-with photos/find-album-by
                                      ["id"]
                                      (controller/edit "id"))))

    (it "should ask the view to render the page with the album found"
      (let [album (photos/->Album "id" "title" [])]
        (with-redefs [photos/find-album-by (fn [id] album)]
          (should-have-been-called-with view/render-edit-album
                                        [album]
                                        (controller/edit "id"))))))

  (describe "#do-add-photo"
    (around [it]
        (with-redefs [photos/add-photo (fn [_ _ _])
                      store/save! (fn [& _])
                      store/save-thumbnail! (fn [& _])
                      optimizer/optimize-photo! (fn [photo] photo)
                      optimizer/optimize-thumbnail! (fn [photo] photo)]
          (it)))

    (with the-photo "the photo")
    (with do-add-photo #'iPerture.albums.albums-controller/do-add-photo)

    (it "asks the optimizer to optimize the photo"
      (should-have-been-called-with optimizer/optimize-photo!
                                    ["the photo"]
                                    (@do-add-photo "album-id" @the-photo)))

    (it "asks the optimizer to optimize the thumbnail"
      (should-have-been-called-with optimizer/optimize-thumbnail!
                                    ["the photo"]
                                    (@do-add-photo "album-id" @the-photo)))

    (it "saves the photo file in a permanent location"
      (should-have-been-called-with store/save!
                                    ["album-id" "the photo"]
                                    (@do-add-photo "album-id" @the-photo)))

    (it "saves the thumbnail file in a permanent location"
      (should-have-been-called-with store/save-thumbnail!
                                    ["album-id" "the photo"]
                                    (@do-add-photo "album-id" @the-photo)))

    (it "asks the model to save the photo informations"
      (let [photo-info {:url "url"}
            thumbnail-info {:url "thumbnail-url"}]
        (with-redefs [store/save! (fn [& _] photo-info)
                      store/save-thumbnail! (fn [& _] thumbnail-info)]
          (should-have-been-called-with photos/add-photo
                                        ["album id" photo-info thumbnail-info]
                                        (@do-add-photo "album id" {:photos {:tempfile "temp path"}}))))))

  (describe "#add-photos"
    (around [it]
        (with-redefs [dj/submit-job (fn [_] 123)]
          (it)))

    (it "asks the delayed-job to handle the operations on the image"
      (should-have-been-called dj/submit-job
                               (controller/add-photos "album-id" {"photos" ["p1"]})))

    (it "should accept more than one photo"
      (with-redefs [photos/add-photo (fn [_ _ _] "added")]
        (should= {:status 201
                  :headers {"Location" "/albums/album-id/add-photos-status/123"}
                  :body "/albums/album-id/add-photos-status/123"}
                 (controller/add-photos "album-id" {"photos" ["photo1" "photo2"]}))))))
