(ns iPerture.albums.albums-view-spec
  (:use [speclj.core]
        [iPerture.spec-helper])
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.albums.albums-view :as view]))

(describe "albums-view"

  (describe "fn render-new-album"
    (it "should return a page with a form for creating a new album"
      (should-contain [:form] (view/render-new-album)))

    (it "should set the create form action to the create url"
      (match-selector [:form (html/attr= :action "/albums")] (view/render-new-album)))

    (it "should set the create form method to POST"
      (match-selector [:form (html/attr= :method "POST")] (view/render-new-album)))))
