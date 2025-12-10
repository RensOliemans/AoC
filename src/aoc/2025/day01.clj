(ns aoc.2025.day01
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2025 1))

(defn- convert-rotation "Converts a rotation string into a number.
  For example, L68 would be -68, and R48 would be 48."
  [rot]
  (let [direction (first rot)
        number (Integer/parseInt (subs rot 1))]
    (condp = direction
      \L (- number)
      \R number)))

(defn part1 [input]
  (->> input
       s/parse-lines
       (map convert-rotation)
       (reductions + 50)
       (map #(mod % 100))
       (filter zero?)
       count))

(defn- zero-counters
  "When moving the dial from `from` to `to`, how often does the dial
  point at `0`, during or after the rotation?"
  [from to]
  (let [divver (if (< from to) #(Math/floorDiv %1 %2) #(Math/ceilDiv %1 %2))]
    (let [a (divver from 100)
          b (divver to   100)]
      (Math/abs (- b a)))))

(defn part2 [input]
  (->> input
       s/parse-lines
       (map convert-rotation)
       (reductions + 50)
       (partition 2 1)
       (pmap (fn [[x y]] (zero-counters x y)))
       (reduce +)))
