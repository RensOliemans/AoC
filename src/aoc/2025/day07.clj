(ns aoc.2025.day07
  (:require [aoc.util.day :as d]
            [aoc.util.grid :as g]))

(def input (d/day-input 2025 7))

(defn is-beam?
  "Returns true when a point in a grid is a beam, nil otherwise.
  A point is a beam if and only if one of the following holds:
  - 'n' (spot north) is a beam AND NOT a splitter (`^`)
  - 'ne' is a beam AND 'e' is a splitter
  - 'nw' is a beam AND 'w' is a splitter
  - the point points to `S`"
  [grid [x y]]
  (if (= 0 y)
    (= \S (get grid [x y]))
    (or (and (is-beam? grid [x (dec y)])
             (not (= \^ (get grid [x (dec y)]))))
        (and (is-beam? grid [(inc x) (dec y)])
             (= \^ (get grid [(inc x) y])))
        (and (is-beam? grid [(dec x) (dec y)])
             (= \^ (get grid [(dec x) y]))))))

(def is-beam? (memoize is-beam?))

(defn part1 [input]
  (let [grid (g/parse-grid input)
        splitters (g/locs-where grid #(= \^ %))]
    (->> splitters
         (filter (fn [[x y]] (is-beam? grid [x (dec y)])))
         count)))

(defn timelines
  "Returns the amount of timelines possible in a grid, start [x y]."
  [grid [x y]]
  (if (nil? (get grid [x y]))
    1
    (if (= \^ (get grid [x (inc y)]))
      (+ (timelines grid [(dec x) (inc y)])
         (timelines grid [(inc x) (inc y)]))
      (timelines grid [x (inc y)]))))

(def timelines (memoize timelines))

(defn part2 [input]
  (let [grid (g/parse-grid input)
        start (first (g/locs-where grid #(= \S %)))]
    (timelines grid start)))
