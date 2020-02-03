(ns persistents.core-test
  (:require [clojure.test :refer :all]
            [persistents.core :refer :all]
            [clojure.java.io :as io])
  (:import (java.util UUID)))

(def test-string "hello world")
(def test-filename "test-file")

(defn add-test-data-file-fixture [f]
  (->> test-string
    pr-str
    (spit test-filename))
  (f)
  (.delete (io/file test-filename)))

(use-fixtures :once add-test-data-file-fixture)

(deftest test-utils
  (testing "exists"
    (is (exists (io/file test-filename)))))

(deftest disk-operations
  (testing "->disk & <-disk"
    (doseq [object ["hello world"
                    {1 2 3 4}
                    #{1 2 3}
                    [12 "af"]
                    (sorted-map :z :a :k :l)]]
      (let [file-name (io/file (str (UUID/randomUUID)))]
        (testing (str "object " object " type of " (type object))
          (->> object
            (->disk file-name))
          (println "saved file content is" (-> file-name slurp))
          (let [loaded-object (<-disk file-name)]
            (is (= object loaded-object))
            (is (= (type object) (type loaded-object)))
            (.delete file-name)))))))


;(deftest)
