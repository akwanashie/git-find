(ns git-find.core
  (:require [clojure.data.json :as json]
            [clojure.string :as str]
            [clj-fuzzy.metrics :as fuzzy])
  (:gen-class))

(defn github-response-body [response]
  (if (= 200 (:status response))
    (json/read-str (:body response) :key-fn keyword)
    (throw (Exception. "Error"))))

(defn repo-name [repo]
  (:name repo))

(defn distance [string1 string2]
  (fuzzy/levenshtein
   (str/lower-case string1)
   (str/lower-case string2)))

(defn file-details [file]
  {:name (:name file)
   :type (:type file)})

(defn append-distance [keyword project]
  (assoc project :distance (distance keyword (:name project))))

(defn append-distances [keyword projects]
  (map #(append-distance keyword %) projects))

(defn findMatch [keyword projects]
  (->> projects
       (append-distances keyword)
       (#(sort-by (fn [x] (:distance x)) %))
       (#(map (fn [x] (:repo x)) %))
       (distinct)
       (take 5)))