(ns aoc.2022.day09
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2022 9))

(def directions
  {"U" [0 -1] "D" [0 1] "L" [-1 0] "R" [1 0]})

(defn- parse-move [move]
  (let [[dir amount] (str/split move #" ")]
    (repeat (Integer/parseInt amount) (directions dir))))

(defn- distance [v1 v2]
  (apply max (map abs (map - v1 v2))))

(defn- clamp [n minim maxim]
  (cond
    (> n maxim) maxim
    (< n minim) minim
    :else n))

(defn- clamp-around-1 [n]
  (clamp n -1 1))

(defn- move-tail
  "Based on a head and tail, computes the new tail"
  [head tail]
  (if (> (distance head tail) 1)
    (mapv + tail (clamp-around-1 (map - head tail)))
    tail))

(defn- move
  "For a given rope and a direction, computes the new rope."
  [[head & knots] dir]
  (let [next-head (mapv + head dir)]
    (reductions move-tail next-head knots)))

(defn solve [input length]
  (->> (str/split-lines input)
       (mapcat parse-move)
       (reductions move (repeat length [0 0]))
       (map last)
       distinct
       count))

(defn part1 [input] (solve input 2))

(defn part2 [input] (solve input 10))
