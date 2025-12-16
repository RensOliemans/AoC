(ns aoc.2022.day01
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2022 01))

(defn part1 [input]
  (->> (s/parse-blocks input)
       (map s/parse-ints)
       (map #(reduce + %))
       (apply max)))

(defn part2 [input]
  (->> (s/parse-blocks input)
       (map s/parse-ints)
       (map #(reduce + %))
       (sort >)
       (take 3)
       (reduce +)))
