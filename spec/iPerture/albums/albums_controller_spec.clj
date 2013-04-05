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
      (with-redefs [view/render-new-album (fn [] (reset! have-called-render-new-album true))]
        (controller/new)
        (should @have-called-render-new-album))))

;; action="html_form_action.asp" method="get"
  (describe "create"
    ))
