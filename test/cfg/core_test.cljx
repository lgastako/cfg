(ns cfg.core-test
  #+cljs (:require-macros [cemerick.cljs.test :refer [deftest is testing]])
  (:require #+clj [clojure.test :refer [deftest is testing run-tests]]
            #+cljs [cemerick.cljs.test :as t]
            [cfg.core :as cfg :refer [ensure-ks dissoc-in]]))

(deftest test-ensure-ks
  (is (= [:a] (ensure-ks :a)))
  (is (= [:b] (ensure-ks :b)))
  (is (= [:c] (ensure-ks :c)))
  (is (= [:a] (ensure-ks [:a])))
  (is (= [:a :b] (ensure-ks [:a :b])))
  (is (= [:a :b :c] (ensure-ks [:a :b :c]))))

(deftest test-dissoc-in
  (is (= {} (dissoc-in {:k 1} :k)))
  (is (= {:a {:b {:d 2}}} (dissoc-in {:a {:b {:c 1 :d 2}}} [:a :b :c])))
  (is (= {} (dissoc-in {:a {:b {:c 1 :d 2}}} :a)))
  (is (= {:a {}} (dissoc-in {:a {:b {:c 1 :d 2}}} [:a :b])))
  (is (= {:a {:b {:c 1}}} (dissoc-in {:a {:b {:c 1 :d 2}}} [:a :b :d]))))

(deftest test-get-set
  (testing "Basic get/set"
    (cfg/del :foo)
    (cfg/del :bar)
    (cfg/del :baz)
    (is (nil? (cfg/get :foo)))
    (is (nil? (cfg/get :bar)))
    (is (nil? (cfg/get :baz)))
    (cfg/set :foo :bar)
    (cfg/set :bar :baz)
    (cfg/set :baz :bif)
    ;; Out of order
    (is (= :bif (cfg/get :baz)))
    (is (= :baz (cfg/get :bar)))
    (is (= :bar (cfg/get :foo)))
    ;; Same key still good
    (is (= :bif (cfg/get :baz))))

  (testing "Defaults"
    (cfg/del :foo)
    (is (= 5 (cfg/get :foo 5)))
    (cfg/del [:a :b :c])
    (is (= 6 (cfg/get [:a :b :c] 6))))

  (testing "del nested paths that don't exist"
    (cfg/del [:a :Q 5 "zz" :k 6 2]))

  (testing "Nested paths"
    (cfg/del [:foo :bar :baz])
    (is (nil? (cfg/get [:foo :bar :baz])))
    (cfg/set [:foo :bar :bif] :bam)
    (is (nil? (cfg/get [:foo :bar :baz])))
    (cfg/set [:foo :bar :baz] :boom)
   (is (= :boom (cfg/get [:foo :bar :baz])))))

;; TODO: auto convert the dangling value to a map and proceed to ... etc.
(deftest test-some-keys-cause-problems
  (testing "oh...."
    (cfg/set :bar {}) ;; if it's not a map it'll explode.
    (cfg/set [:bar :baz] 5)
    (cfg/del [:bar :baz :bam])))

(deftest test-del
  (testing "k"
    (cfg/set :foo :bar)
    (is (= :bar (cfg/get :foo)))
    (cfg/del :foo)
    (is (nil? (cfg/get :foo))))

  (testing "ks"
    (cfg/set [:foo :bar :baz] :bif)
    (is (= :bif (cfg/get [:foo :bar :baz])))
    (cfg/del [:foo :bar :baz])
    (is (nil? (cfg/get [:foo :bar :baz])))))

;;(run-tests)

