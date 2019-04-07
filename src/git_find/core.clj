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

(defn levenshtein-distance [str1 len1 str2 len2]
  (cond (= 0 len1) len2
        (= 0 len2) len1
        :else (let [cost (if (=
                              (nth str1 (- len1 1))
                              (nth str2 (- len2 1)))
                           0 1)]
                (min
                 (+ 1 (levenshtein-distance str1 (- len1 1) str2 len2))
                 (+ 1 (levenshtein-distance str1 len1 str2 (- len2 1)))
                 (+ cost (levenshtein-distance str1 (- len1 1) str2 (- len2 1)))))))

(defn distance [string1 string2]
  (levenshtein-distance
   (str/lower-case string1)
   (count string1) 
   (str/lower-case string2)
   (count string2)))

(defn file-details [file]
  {:name (:name file)
   :type (:type file)})

(defn append-distance [project keyword]
  (assoc project :distance (fuzzy/levenshtein keyword (:name project))))

(defn append-distances [projects keyword]
  (map #(append-distance % keyword) projects))