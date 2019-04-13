(ns git-find.shell
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]
            [git-find.core :as core])
  (:gen-class))

(def github-url "https://api.github.com")
(def github-token (env :github-token))

(defn github-get [endpoint & [query-params]]
  (let [full-url (str github-url endpoint)
        token (str "token " github-token)]
    (client/get full-url {:headers {:Authorization token}
                          :query-params query-params})))

(defn github-content [owner repo]
  (-> (str "/repos/" owner "/" repo "/contents/")
      (github-get {})
      (core/github-response-body)
      (#(map core/file-details %))
      (#(map (fn [file] (assoc file :repo repo)) %))))

(defn top-level-files [repo owner]
  (let [repo-name (core/repo-name repo)]
    (-> repo-name
        (#(github-content owner %))
        (conj {:name repo-name :repo repo-name}))))

(defn github-projects [owner]
  (-> "/user/repos"
      (github-get {:type "owner"})
      (core/github-response-body)
      (#(map (fn [repo] (top-level-files repo owner)) %))
      (flatten)))

(defn -main [& args]
  (let [user "akwanashie"]
    (->> user
         (github-projects)
         (core/findMatch (first args))
         (#(doseq [x %] (println (str "http://www.github.com/" user "/" x)))))))