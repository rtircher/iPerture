(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core])
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.albums.albums-controller :as controller]))

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
    (it "should return a page with a form for creating a new album"
      (should-contain [:form] (controller/new)))

    (it "should set the create form action to the create url"
      (match-selector [:form (html/attr= :action "/albums")] (controller/new)))

    (it "should set the create form method to POST"
      (match-selector [:form (html/attr= :method "POST")] (controller/new))))

;; action="html_form_action.asp" method="get"
  (describe "create"
    ))
