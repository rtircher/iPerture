(ns iPerture.image-optimizer
  (:require [illusioniste.core :as illusioniste])
  (:use [clojure.java.io :only [file]]) ; -> move to require
  (:import org.apache.commons.io.FileUtils
           org.im4java.core.Info))

(defn- load-photo [filename]
  (FileUtils/readFileToByteArray (file filename)))

(defn- save-photo [filename converted-photo]
  (FileUtils/writeByteArrayToFile (file filename) converted-photo))

(defn optimize-photo! [{filename :tempfile :as photo}]
  (save-photo filename
              (illusioniste/transform-image
               (load-photo filename)
               (resize (int 1600))))
  photo)

(defn optimize-thumbnail! [{filename :tempfile :as photo}]
  (save-photo filename
              (illusioniste/transform-image
               (load-photo filename)
               (resize (int 150))
               ;; remove exif data
               ))
  photo)
