(ns iPerture.image-optimizer
  (:require [illusioniste.core :as illusioniste])
;  (:use [clojure.java.io :only [file]]) ; -> move to require
  (:import org.apache.commons.io.FileUtils
           org.im4java.core.Info))

(def ^:private min-dimension 150)

(defn- load-photo [file]
  (FileUtils/readFileToByteArray file))

(defn- save-photo [file converted-photo]
  (FileUtils/writeByteArrayToFile file converted-photo))

(defn- image-info [file]
  (let [info (Info. (.getCanonicalPath file))]
    {:height (.getImageHeight info)
     :width  (.getImageWidth info)}))

(defn- keep-aspect-ration [min other]
  (Math/ceil (* other (/ min-dimension min))))

(defn- calculate-dimension [size1 size2]
  (if (< size1 size2)
    min-dimension
    (keep-aspect-ration size2 size1)))

(defn- calculate-thumbnail-dimensions [width height]
  (if (or (< width min-dimension) (< height min-dimension))
    {:width width
     :height height}

    {:width  (calculate-dimension width height)
     :height (calculate-dimension height width)}))

(defn optimize-photo! [{file :tempfile :as photo}]
  (save-photo file
              (illusioniste/transform-image
               (load-photo file)
               (resize (int 1600))))
  photo)

(defn optimize-thumbnail! [{file :tempfile :as photo}]
  (let [{:keys [width height]} (image-info file)
        {:keys [width height]} (calculate-thumbnail-dimensions width height)]
    (save-photo file
                (illusioniste/transform-image
                 (load-photo file)
                 (resize (int width) (int height))
                 ;; remove exif data
                 )))
  photo)
