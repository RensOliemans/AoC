(ns aoc.util.string
  (:require [clojure.string :as str]))

(defn parse-blocks [input]
  (str/split input #"\n\n"))

(defn parse-lines [input]
  (str/split-lines input))

(defn parse-ints [str]
  (mapv parse-long (re-seq #"-?\d+" str)))
