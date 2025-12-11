(ns aoc.2024.day01
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]))

(def input (d/day-input 2024 01))

(defn part1 [input]
  (->> (s/parse-lines input)
       (map s/parse-ints)
       v/transpose
       (map sort)
       v/transpose
       (map #(abs (- (first %) (second %))))
       (reduce +)))

(defn part2 [input]
  (let [input (v/transpose (map s/parse-ints (s/parse-lines input)))
        one (first input)
        freqs (frequencies (second input))]
    (->> one
         (map #(* % (freqs % 0)))
         (reduce +))))
