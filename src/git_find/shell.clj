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

(defn github-repos [team-name]
  (-> "/user/repos"
      (github-get {:type "owner"})
      (core/github-response-body)
      (#(map core/repo-details %))))

(defn -main []
  (-> "connections"
      (github-repos)
      (clojure.pprint/pprint)))