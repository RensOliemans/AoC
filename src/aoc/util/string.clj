(ns aoc.util.string
  (:require [clojure.string :as str]))

(defn parse-blocks [input]
  (str/split input #"\n\n"))

(defn parse-lines [input]
  (str/split-lines input))

(defn parse-csvs [input]
  (str/split input #","))

(defn parse-ints [str]
  (mapv parse-long (re-seq #"-?\d+" str)))

(defn parse-ranges
  "Parse each string of input as a range in the form 'M-N'"
  [ranges]
  (->> ranges
       (map #(str/replace % "-" " "))
       (map parse-ints)))

