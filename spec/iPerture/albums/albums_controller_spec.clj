(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core])
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.albums.albums-controller :as controller]
            [iPerture.albums.albums-view :as view]))

(defn page [enlive-html]
  (html/html-resource (java.io.StringReader. (apply str enlive-html))))

(defn select [selector enlive-html]
  (html/select (page enlive-html) selector))

(defn should-contain [selector enlive-html]
  (should-not= '() (select selector enlive-html)))

(defn match-selector [selector enlive-html]
  (should (select selector enlive-html)))

(describe "albums-controller"

  (describe "fn new"
    (it "should call the render new album view function"
      (def have-called-render-new-album (atom false))
      (with-redefs [view/render-new-album
                    (fn [& error-message] (reset! have-called-render-new-album true))]
        (controller/new)
        (should @have-called-render-new-album))))

  (describe "create"
    (context "when the album title is empty"
      (it "should stay on the new page"
        (def have-called-render-new-album (atom false))
        (with-redefs [view/render-new-album
                      (fn [& error-message] (reset! have-called-render-new-album true))]
          (controller/create [""])
          (should @have-called-render-new-album)))

      (it "should ask the view to display an error message"
        (def message-received (atom nil))
        (with-redefs [view/render-new-album
                      (fn [error-message] (reset! message-received error-message))]
          (controller/create [""])
          (should= "Please enter an album title" @message-received)))

      (it "should not create a new album"
        ))

    (it "should create a new album with the provided title"
      )

    (it "should render an empty edit album page with the provided title")))
