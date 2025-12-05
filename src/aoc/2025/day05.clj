(ns aoc.2025.day05
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2025 5))

(defn- parse-ranges [block]
  (->> block
       (re-seq #"\d+")
       (map parse-long)
       (partition 2)))

(defn- fresh? [ingredient ranges]
  (some (fn [[start end]]
          (and (>= ingredient start)
               (<= ingredient end)))
        ranges))

(defn part1 [input]
  (let [[ranges ingredients] (s/parse-blocks input)
        ranges (parse-ranges ranges)
        ingredients (s/parse-ints ingredients)]
    (->> ingredients
         (filter #(fresh? % ranges))
         count)))

(defn part2 [input]
  (let [ranges
        (->> input
             s/parse-blocks
             first
             parse-ranges
             (sort #(< (first %1) (first %2))))]
    (first
     (reduce (fn [[current m] [from to]]
               (if (> from m)
                 [(+ current (- (inc to) from)) (max m to)]
                 [(+ current (max 0 (- to m))) (max m to)]))
             [0 -1]
             ranges))))
