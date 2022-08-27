(ns fakeflix-schema.schema-test
  (:require [clojure.test :refer [deftest is testing]]
            [fakeflix-schema.schema :as schema]
            [matcher-combinators.test :refer [match?]]
            [schema.core :as s]))

(deftest loose-schema-test
  (testing "Should create the correct loose schema for required fields"
    (is (match? {:test s/Str
                 s/Any s/Any}
                (schema/loose-schema {:test {:schema s/Str :required true}})))

    (is (match? {:test  s/Str
                 :test2 s/Num
                 s/Any  s/Any}
                (schema/loose-schema {:test  {:schema s/Str :required true}
                                      :test2 {:schema s/Num :required true}}))))

  (testing "Should create the correct loose schema for optional fields"
    (is (match? {(s/optional-key :test) s/Str
                 s/Any                  s/Any}
                (schema/loose-schema {:test {:schema s/Str :required false}})))

    (is (match? {:test                   s/Str
                 (s/optional-key :test2) s/Num
                 s/Any                   s/Any}
                (schema/loose-schema {:test  {:schema s/Str :required true}
                                      :test2 {:schema s/Num :required false}})))))

(deftest strict-schema-test
  (testing "Should create the correct strict schema for required fields"
    (is (match? {:test s/Str}
                (schema/strict-schema {:test {:schema s/Str :required true}})))

    (is (match? {:test  s/Str
                 :test2 s/Num}
                (schema/strict-schema {:test  {:schema s/Str :required true}
                                       :test2 {:schema s/Num :required true}}))))

  (testing "Should create the correct strict schema for optional fields"
    (is (match? {(s/optional-key :test) s/Str}
                (schema/strict-schema {:test {:schema s/Str :required false}})))

    (is (match? {:test                   s/Str
                 (s/optional-key :test2) s/Num}
                (schema/strict-schema {:test  {:schema s/Str :required true}
                                       :test2 {:schema s/Num :required false}})))))

(deftest keyword-skeleton->keyword-schema-test
  (testing "Should return a map containing the keyword and the schema type"
    (is (match? {:test s/Str}
                (schema/keyword-skeleton->keyword-schema [:test {:schema s/Str :required true}])))

    (is (match? {(s/optional-key :test) s/Str}
                (schema/keyword-skeleton->keyword-schema [:test {:schema s/Str :required false}])))))