(ns aoc.2021.day05
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

(def input "0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
")
(def input (d/day-input 2021 5))


(defn- parse-ranges [input]
  (->> input
     s/parse-ints
     (partition 2)
     (partition 2)))

(defn- range-to-seq [[[x1 y1] [x2 y2]]]
  (let [xrange
        (cond
          (> x2 x1) (range x1 (inc x2) 1)
          (< x2 x1) (range x1 (dec x2) -1)
          :else (repeat x1))
        yrange
        (cond
          (> y2 y1) (range y1 (inc y2) 1)
          (< y2 y1) (range y1 (dec y2) -1)
          :else (repeat y1))]
    (v/transpose [xrange yrange])))

(defn part1 [input]
  (let [ranges
        (->> input
             parse-ranges
             (filter (fn [[[x1 y1] [x2 y2]]]
                       (or (= x1 x2)
                           (= y1 y2)))))
        vents
        (reduce (fn [nums range]
                  (into nums (range-to-seq range)))
                []
                ranges)]
    (->> vents
         frequencies
         (map val)
         (filter #(< 1 %))
         count)))

(defn part2 [input]
  (let [ranges
        (->> input
             parse-ranges)
        vents
        (reduce (fn [nums range]
                  (into nums (range-to-seq range)))
                []
                ranges)]
    (->> vents
         frequencies
         (map val)
         (filter #(< 1 %))
         count)))
