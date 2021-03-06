(ns git-find.core-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [git-find.core :refer :all]))

(deftest test-repo-name
  (testing "Should return the repository name from the json response"
    (let [name "test-repo"
          github-response {:name name}]
      (is (= name (repo-name github-response)))))
  (testing "Should return null if name not present"
    (let [expected {:name nil}]
      (is (nil? (repo-name {}))))))

(deftest test-response-body
  (testing "Should return the body if status is 200"
    (let [response-body {:name "test-repo"}
          input {:status 200
                 :body (json/write-str response-body) }]
      (is (= response-body (github-response-body input)))))
  (testing "Should throw an error if status is not 200"
    (is (thrown? Exception (github-response-body {:status 501})))))

(deftest test-distance
  (testing "Should return 0 for empty strings"
    (is (= 0 (distance "" ""))))
  (testing "Should return 0 for equal strings"
    (is (= 0 (distance "testing" "testing"))))
  (testing "Should return the length of one string if the other is empty"
    (is (= 7 (distance "testing" ""))))
  (testing "Should return the correct value"
    (is (= 5 (distance "computer" "house")))))