(ns git-find.shell-test
  (:require [clojure.test :refer :all]
            [git-find.shell :refer :all]))

; (deftest test-get-repos
;   (testing "Should return all repos owned by a given team"
;     (let [expected {:name "things"}]
;       (is (= expected (repos-by-team "my-team"))))))