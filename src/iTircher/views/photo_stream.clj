(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common]
            [net.cgrand.enlive-html :as html])
  (:use [noir.core :only [defpage]]))

(html/deftemplate photo-stream-page "public/html/index.html" [])

(defpage "/photostream" [] (photo-stream-page))
