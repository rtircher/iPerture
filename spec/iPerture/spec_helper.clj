(ns iPerture.spec-helper
  (:use [speclj.core])
  (:require [net.cgrand.enlive-html :as html])
  (:import [speclj SpecFailure]))

(defn page [enlive-html]
  (html/html-resource (java.io.StringReader. (apply str enlive-html))))

(defn select [selector enlive-html]
  (html/select (page enlive-html) selector))

(defn should-contain
  ([selector enlive-html]
     (should-not= '() (select selector enlive-html)))

  ([content selector enlive-html]
     (let [to-compare (if (coll? content) content [content])]
       (should= to-compare
                (map html/text (select selector enlive-html))))))

(defn should-not-contain
  ([selector enlive-html]
     (should= '() (select selector enlive-html)))

  ([content selector enlive-html]
     (let [to-compare (if (coll? content) content [content])]
       (should-not= to-compare
                    (map html/text (select selector enlive-html))))))

(defn match-selector [selector enlive-html]
  (should (not (empty? (select selector enlive-html)))))

(defmacro has-been-called
  ([function body test-fn error-message]
     `(has-been-called ~function ~body ~test-fn ~error-message {}))
  ([function body test-fn error-message {:keys [and-return]}]
     `(let [function-has-been-called?# (atom false)]
        (with-redefs [~function
                      (fn [& _#]
                        (reset! function-has-been-called?# true)
                        (or ~and-return (fn [& _#] nil)))]
          ~body
          (if-not (~test-fn @function-has-been-called?#)
            (throw (SpecFailure. (str ~error-message))))))))

(defmacro should-have-been-called
  ([function body]
     `(should-have-been-called ~function ~body {}))
  ([function body opts]
     `(has-been-called ~function ~body identity "Expected function to have been called" ~opts)))

(defmacro should-not-have-been-called
  ([function body]
     `(should-not-have-been-called ~function ~body {}))
  ([function body opts]
     `(has-been-called ~function ~body not "Expected function not to have been called" ~opts)))

(defmacro should-have-been-called-with
  ([function expected-params body]
     `(should-have-been-called-with ~function ~expected-params ~body {}))
  ([function expected-params body {:keys [and-return] :as opts}]
     `(let [params-received# (atom nil)]
        ;; First check if the function was called at all
        (should-have-been-called ~function ~body ~opts)
        (with-redefs [~function
                      (fn [& params#] (reset! params-received# params#)
                        (or ~and-return (fn [& _#] nil)))]
          ~body
          (should= ~expected-params @params-received#)))))
