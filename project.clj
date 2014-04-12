(defproject cfg "0.1.0-SNAPSHOT"
  :description "Simple configuration management for Clojure and ClojureScript."
  :url "http://github.com/lgastako/cfg"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  ;; :pedantic? :abort
  ;; :pedantic? :warn
  :cljsbuild {:builds {:dev {:source-paths ["src" "gen/src"]
                             :compiler {:output-to "resources/public/js/main.js"
                                        :output-dir "resources/public/js"
                                        :optimizations :whitespace
                                        :pretty-print true
                                        :source-map "resources/public/js/main.js.map"}}
                       :test {:source-paths ["src"
                                             "test"
                                             "gen/src"
                                             "gen/test"]
                              :compiler {:output-to "gen/test/js/testable.js"
                                         ;; :optimizations :advanced
                                         :optimizations :simple
                                         :pretty-print true}}}
              :test-commands {"phantomjs" ["phantomjs"
                                           :runner
                                           "resources/public/js/bind-polyfill.js"
                                           "gen/test/js/testable.js"]}}
  :cljx {:builds [{:source-paths ["src"]  :output-path "gen/src"  :rules :clj}
                  {:source-paths ["src"]  :output-path "gen/src"  :rules :cljs}
                  {:source-paths ["test"] :output-path "gen/test" :rules :clj}
                  {:source-paths ["test"] :output-path "gen/test" :rules :cljs}]}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2197"]]
  :hooks [leiningen.cljsbuild cljx.hooks]
  :jar-exclusions [#"\.cljx|\.swp|\.swo|\.DS_Store"]
  :min-lein-version "2.0.0"
  :plugins [[com.keminglabs/cljx "0.3.2" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.0.3"]
            [com.cemerick/clojurescript.test "0.3.0"]]
  :profiles {:dev  {:source-paths ["env/dev"]}
             :test {:source-paths ["env/test"]}
             :prod {:source-paths ["env/prod"]}}
  :source-paths ["src" "gen/src"]
  :test-paths ["test" "gen/test"])
