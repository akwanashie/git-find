(def project 'git-find)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[adzerk/boot-test "RELEASE" :scope "test"]
                            [clj-http "3.9.1"]
                            [clj-fuzzy "0.4.1"]
                            [environ "0.5.0"]
                            [org.clojure/clojure "RELEASE"]
                            [org.clojure/data.json "0.2.6"]])

(task-options!
 aot {:namespace   #{'git-find.shell}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/git-find"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 repl {:init-ns    'git-find.shell}
 jar {:main        'git-find.shell
      :file        (str "git-find-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (with-pass-thru fs
    (require '[git-find.shell :as app])
    (apply (resolve 'app/-main) args)))

(require '[adzerk.boot-test :refer [test]])
