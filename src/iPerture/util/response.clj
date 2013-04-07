(ns iPerture.util.response)

(defn post-error-response [body]
  {:status  422
   :headers {}
   :body    body})
