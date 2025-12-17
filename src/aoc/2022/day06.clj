(ns aoc.2022.day06
  (:require [aoc.util.day :as d]))

(def input (d/day-input 2022 06))

(defn- first-unique-group [input n]
  (->> input
       (partition n 1)
       (take-while #(not= (count %) (count (set %))))
       count
       (+ n)))

(defn part1 [input] (first-unique-group input 4))

(defn part2 [input] (first-unique-group input 14))
