(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common])
  (:use [noir.core :only [defpage]]
        [net.cgrand.enlive-html :only [deftemplate defsnippet content]]))

(deftemplate photo-stream-page "public/html/index.html" [])

(defpage "/photostream" [] (photo-stream-page))
