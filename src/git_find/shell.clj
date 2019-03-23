(ns git-find.shell
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [environ.core :refer [env]])
  (:gen-class))

(def github-url "https://api.github.com")
(def github-token (env :github-token))

(defn github-request [endpoint & [query-params]]
  (let [full-url (str github-url endpoint)
        token (str "token " github-token)]
    (client/get full-url {:headers {:Authorization token}
                          :query-params query-params})))

(defn github-response-body [response]
  (if (= 200 (:status response))
    (json/read-str (:body response) :key-fn keyword)
    (throw (Exception. "Error"))))

(defn repo-details [repo]
  {:name (:name repo)})

(defn github-repos [team-name]
  (-> "/user/repos"
      (github-request {:type "owner"})
      (github-response-body)
      ((fn [repos] (map repo-details repos)))))

(defn -main []
  (-> "connections"
      (github-repos)
      (clojure.pprint/pprint)))