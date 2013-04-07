(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core]
        [iPerture.spec-helper]
        [ring.util.response :only [redirect-after-post]])
  (:require [iPerture.albums.albums-controller :as controller]
            [iPerture.albums.albums-view :as view]
            [iPerture.albums.albums :as albums]))

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
                                     (controller/create @params))))))
