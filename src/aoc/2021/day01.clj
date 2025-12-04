(ns aoc.2021.day01
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2021 1))

(defn part1 [input]
  (->> input
       s/parse-ints
       (partition 2 1)
       (filter (fn [[a b]] (> b a)))
       count))

(defn part2 [input]
  (->> input
       s/parse-ints
       (partition 3 1)
       (map #(reduce + %))
       (partition 2 1)
       (filter (fn [[a b]] (> b a)))
       count))
