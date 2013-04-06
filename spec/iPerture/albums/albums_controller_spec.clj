(ns iPerture.albums.albums-controller-spec
  (:use [speclj.core]
        [noir.validation :only [wrap-noir-validation]])
  (:require [net.cgrand.enlive-html :as html]
            [iPerture.albums.albums-controller :as controller]
            [iPerture.albums.albums-view :as view]
            [iPerture.albums.albums :as albums])
  (:import [speclj SpecFailure]))

(defn page [enlive-html]
  (html/html-resource (java.io.StringReader. (apply str enlive-html))))

(defn select [selector enlive-html]
  (html/select (page enlive-html) selector))

(defn should-contain [selector enlive-html]
  (should-not= '() (select selector enlive-html)))

(defn match-selector [selector enlive-html]
  (should (select selector enlive-html)))

(defmacro has-been-called [function body test-fn error-message]
  `(let [function-has-been-called?# (atom false)]
     (with-redefs [~function
                   (fn [& _#] (reset! function-has-been-called?# true))]
       ~body
       (if-not (~test-fn @function-has-been-called?#)
         (throw (SpecFailure. (str ~error-message)))))))

(defmacro should-have-been-called [function body]
  `(has-been-called ~function ~body identity "Expected function to have been called"))

(defmacro should-not-have-been-called [function body]
  `(has-been-called ~function ~body not "Expected function not to have been called"))

(defmacro should-have-been-called-with [function expected-params body]
  `(let [params-received# (atom nil)]
     ;; First check if the function was called at all
     (should-have-been-called ~function ~body)
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
    ;; Need to setup the validation framework
    (around [it]
        ((wrap-noir-validation (fn [request] (it))) nil))

    (with-all params {:create-album-title "title"})

    (it "should create a new album with the provided title"
      (should-have-been-called-with albums/create
                                    ["title"]
                                    (controller/create @params)))

    (it "should ask the view to render an empty edit album page with the provided title"
      (should-have-been-called-with view/render-edit-album
                                    ["title"]
                                    (controller/create @params)))

    (context "when the album title is empty"
      (with-all params {:create-album-title ""})

      (it "should stay on the new page"
        (should-have-been-called view/render-new-album
                                 (controller/create @params)))

      (it "should ask the view to display an error message"
        (should-have-been-called-with view/render-new-album
                                      [{:create-album-title ["Please enter an album title"]}]
                                      (controller/create @params)))

      (it "should not create a new album"
        (should-not-have-been-called albums/create
                                     (controller/create @params))))))
