(ns aoc.util.string
  (:require [clojure.string :as str]))

(def ^:private digits {\0 0, \1 1, \2 2, \3 3, \4 4, \5 5, \6 6, \7 7, \8 8
                       \9 9})

(defn ->digits
  "Convert a string of digits to a list of individual numbers"
  [string]
  (map digits string))

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

