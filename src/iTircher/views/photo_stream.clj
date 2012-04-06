(ns iTircher.views.photo-stream
  (:require [iTircher.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [net.cgrand.enlive-html :only [deftemplate]]))

(defpage "/photostream" []
  (deftemplate photo-stream-page "html/index.html" [] ()))