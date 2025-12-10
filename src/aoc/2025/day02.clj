(ns aoc.2025.day02
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2025 2))

(defn- to-ranges
  "Converts todays puzzle input to a seq of float-pairs.
  For example, \"11-22,95-115\" will be converted to ((11 22) (95
  115))."
  [input]
  (->> input
       s/parse-csvs
       s/parse-ranges))

(defn solve
  [input regexp]
  (->> input
       to-ranges
       (pmap (fn [[start end]]
               (filterv #(re-matches regexp (str %))
                        (range start (inc end)))))
       flatten
       (reduce +)))

(defn part1 [input]
  (solve input #"^(\d+)\1$"))

(defn part2 [input]
  (solve input #"^(\d+)\1+$"))
