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

(defmacro should-have-been-called [function body]
  `(let [function-has-been-called?# (atom false)]
     (with-redefs [~function
                   (fn [& _#] (reset! function-has-been-called?# true))]
       ~body
       (should @function-has-been-called?#))))

(defmacro should-have-been-called-with [function expected-params body]
  `(let [params-received# (atom nil)]
     (with-redefs [~function
                   (fn [& params#] (reset! params-received# params#))]
       ~body
       (should= ~expected-params @params-received#))))

(describe "albums-controller"

  (describe "fn new"
    (it "should call the render new album view function"
      (should-have-been-called view/render-new-album
                               (controller/new))))

  (describe "create"
    (it "should create a new album with the provided title"
      )

    (it "should render an empty edit album page with the provided title")

    (context "when the album title is empty"
      (it "should stay on the new page"
        (should-have-been-called view/render-new-album
                                           (controller/create [""])))

      (it "should ask the view to display an error message"
        (should-have-been-called-with view/render-new-album
                                      ["Please enter an album title"]
                                      (controller/create [""])))

      (it "should not create a new album"
        ))))
