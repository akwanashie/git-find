(ns git-find.core
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn github-response-body [response]
  (if (= 200 (:status response))
    (json/read-str (:body response) :key-fn keyword)
    (throw (Exception. "Error"))))

(defn repo-details [repo]
  {:name (:name repo)})
