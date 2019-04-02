(ns git-find.core
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn github-response-body [response]
  (if (= 200 (:status response))
    (json/read-str (:body response) :key-fn keyword)
    (throw (Exception. "Error"))))

(defn repo-name [repo]
  (:name repo))

(defn file-details [file]
  {:name (:name file)
   :type (:type file)})