(ns aoc.2025.day09
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            ))

(def input (d/day-input 2025 9))

(defn- pairs [xs]
  (for [i (range (count xs))
        j (range i)]
    [(nth xs i) (nth xs j)]))

(defn- size [[x y] [x' y']]
  (* (inc (abs (- x x')))
     (inc (abs (- y y')))))

(defn part1 [input]
  (->> input
       s/parse-lines
       (mapv s/parse-ints)
       pairs
       (map (fn [[a b]] [(size a b) a b]))
       sort
       last
       first))
