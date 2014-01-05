(ns iPerture.albums.albums-view-spec
  (:use [speclj.core]
        [iPerture.spec-helper])
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.photos.photos :as photos]
            [iPerture.albums.albums-view :as view]))

(defn get-style [element]
  (html/attr-values element :style))

(describe "albums-view"

  (describe "fn render-index"
    (with albums [{:id "album1" :title "Album 1"}
                  {:id "album2" :title "Album 2"}
                  {:id "album3" :title "Album 3"}
                  {:id "album4" :title "Album 4"}])

    (it "should set the albums link"
      (let [result (view/render-index @albums)]
        (match-selector [[:.album (html/attr= :href "/albums/album1")]] result)
        (match-selector [[:.album (html/attr= :href "/albums/album2")]] result)
        (match-selector [[:.album (html/attr= :href "/albums/album3")]] result)
        (match-selector [[:.album (html/attr= :href "/albums/album4")]] result)))

    (it "should set the album title"
      (let [result (view/render-index @albums)
            albums (select [:.album-title] result)]
        (should= "Album 1" (html/text (nth albums 0)))
        (should= "Album 2" (html/text (nth albums 1)))
        (should= "Album 3" (html/text (nth albums 2)))
        (should= "Album 4" (html/text (nth albums 3)))))

    (it "should display the empty album picture when the album is empty"
      (let [result (view/render-index [{:photos nil}])
            thumbnail-style (get-style (first (select [:.album] result)))]
        (should= #{"background-image:url('/img/empty-album.jpg')"} thumbnail-style)))

    (it "should use the first photo as thumbnail"
      (let [result (view/render-index [(photos/->Album 123 "title" [(photos/->Photo 1 "/url" "/thumbnail-url")])])
            thumbnail-style (get-style (first (select [:.album] result)))]
        (should= #{"background-image:url('/thumbnail-url')"} thumbnail-style))))

  (describe "fn render-new-album"
    (it "should return a page with a form for creating a new album"
      (should-contain [:form] (view/render-new-album)))

    (it "should set the create form action to the create url"
      (match-selector [[:form (html/attr= :action "/albums")]] (view/render-new-album)))

    (it "should set the create form method to POST"
      (match-selector [[:form (html/attr= :method "POST")]] (view/render-new-album))))

  (describe "fn render-edit-album"
    (with album (photos/->Album 123 "the album title" []))

    (it "should display the album title"
      (should-contain "the album title" [:.page-title] (view/render-edit-album @album)))

    (it "should set the page title to contain the album title"
      (should-contain "Edit Album: the album title" [:title] (view/render-edit-album @album)))

    (it "should display a button to add photos"
      (should-contain [:.add-photo-button] (view/render-edit-album @album)))

    (it "should contain a form with an action to the add photo url"
      (match-selector [[:form (html/attr= :action "/albums/123/photos")]]
                      (view/render-edit-album @album)))

    (it "should contain a form with an method to POST"
      (match-selector [[:form (html/attr= :method "POST")]]
                      (view/render-edit-album @album)))

    (it "should contain a form with an enctype of multipart/form-data"
      (match-selector [[:form (html/attr= :enctype "multipart/form-data")]] (view/render-edit-album @album)))

    (context "when there are photos in the album"
      (it "should display all the photos"))

    (context "when the album is empty"
      (it "should not show any pictures"
        (should-not-contain [:.photo] (view/render-edit-album @album)))

      (it "should show a tutorial to expain what to do at that point"))))
