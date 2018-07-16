(ns first_project.core-spec
  (:require-macros [speclj.core :refer [describe it should= should-not= should should-not run-specs]])
  (:require [speclj.core]
            [first_project.core :as my-core]))

(describe "A ClojureScript test"
  (it "one equals one"
    (should= 1 1)))

(describe "Truth"
  (it "is true"
    (should true))
  (it "is not false"
    (should-not false)))

(describe "Counts average"
  (it "returns average"
    (should=
      15
      (my-core/average 20 10))))
