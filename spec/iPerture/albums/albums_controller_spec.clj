(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [ring.util.response :only [redirect-after-post]])
  (:require [iPerture.albums.albums-controller :as controller]
            [iPerture.albums.albums-view :as view]
            [iPerture.albums.albums :as albums]
            [iPerture.photos.photo-store :as store]))

(describe "albums-controller"

  (describe "fn new"
    (it "should call the render new album view function"
      (should-have-been-called view/render-new-album
                               (controller/new))))

  (describe "create"
    (with-all params {:create-album-title "title"})

    (it "should create a new album with the provided title"
      (should-have-been-called-with albums/create
                                    ["title"]
                                    (controller/create @params)))

    (it "should ask the view to render an empty edit album page with the provided title"
      (with-redefs [albums/create (fn [title] (albums/album "id" title))]
        (should-have-been-called-with redirect-after-post
                                      ["/albums/id"]
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
        (should-not-have-been-called albums/create
                                     (controller/create @params)))))

  (describe "edit"
    (it "should find the album based on the provided id"
      (with-redefs [view/render-edit-album (fn [& _])]
        (should-have-been-called-with albums/find-by
                                      ["id"]
                                      (controller/edit "id"))))

    (it "should ask the view to render the page with the album found"
      (let [album (albums/album "id" "title")]
        (with-redefs [albums/find-by (fn [id] album)]
          (should-have-been-called-with view/render-edit-album
                                        [album]
                                        (controller/edit "id"))))))

  (describe "add-photo"
    (around [it]
        (with-redefs [albums/add-photo (fn [& _])
                      store/save! (fn [& _])]
          (it)))

    (it "saves the photo file in a permanent location"
      (should-have-been-called-with store/save!
                                    ["album-id" "the photo"]
                                    (controller/add-photo "album-id" {:photo "the photo"})))

    (it "asks the model to save the photo informations"
      (let [photo-info {:url "url"}]
        (with-redefs [store/save! (fn [& _] photo-info)]
          (should-have-been-called-with albums/add-photo
                                        ["album id" photo-info]
                                        (controller/add-photo "album id" {:tempfile "temp path"})))))))
