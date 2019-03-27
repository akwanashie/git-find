(ns git-find.shell-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [git-find.shell :refer :all]))

(deftest test-github-get
  (testing "Should call github with the correct url"
    (let [expected-url "https://api.github.com/user/repos"
          error-message #(str "Wrong Url: expected " expected-url " but found " %)
          mock-fn (fn [url options] (if (= expected-url url)
                                      {:status 200}
                                      (throw (Exception. (error-message url)))))]
      (with-redefs-fn {#'client/get mock-fn}
        #(is (github-get "/user/repos" {:type "owner"}))))))
