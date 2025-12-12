(ns aoc.2025.day12
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2025 12))

(defn naive-fit? [recipe]
  (let [[grid presents] (str/split recipe #": ")
        gridsize (reduce * (s/parse-ints grid))
        presentsize (* 9 (reduce + (s/parse-ints presents)))]
    (>= gridsize presentsize)))

(defn part1 [input]
  (->> input
       s/parse-blocks
       last
       s/parse-lines
       (filter naive-fit?)
       count))

(defn part2 [input] "Today has no part 2")
