(ns aoc.2024.day06
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

(def input (d/day-input 2024 6))

(defn guard-route
  "Takes a `grid` as input returns a vector of 2d coordinates: the route
  of the guard, starting at `start` and turning clockwise at \"#\"
  characters. "
  [grid start]
  (let [size (count grid)
        grid (assoc-in grid start \.)]
    (loop [location start
           directions (cycle v/cardinal-dirs)
           route []]
      (let [[y x] location
            [x' y'] (first directions)
            next-location [(+ y y') (+ x x')]
            next-object (get-in grid next-location)
            route (conj route location)]
        (condp = next-object
          nil route
          \. (recur next-location
                    directions
                    route)
          \# (recur location
                    (next directions)
                    route))))))

(defn part1 [input]
  (let [grid (g/to-matrix input)
        start (first (g/locs-where grid #(= % \^)))
        route (guard-route grid start)]
    (count (distinct route))))


(defn route-has-loop? [grid start]
  (let [size (count grid)
        grid (assoc-in grid start \.)]
    (loop [location start
           directions (cycle v/cardinal-dirs)
           visited #{}]
      (let [[y x] location
            [x' y'] (first directions)
            next-location [(+ y y') (+ x x')]
            next-object (get-in grid next-location)
            pair [next-location [x' y']]]
        (if (contains? visited pair)
          true ;; we have a loop!
          (condp = next-object
            nil false ;; we exited the puzzle
            \. (recur next-location
                      directions
                      visited)
            \# (recur location
                      (next directions)
                      (conj visited pair))))))))

(defn part2 [input]
  (let [grid (g/to-matrix input)
        start (first (g/locs-where grid #(= % \^)))
        route (disj (set (guard-route grid start)) start)]
    (->> route
         (pmap (fn [new-obstacle]
                 (route-has-loop? (assoc-in grid new-obstacle \#) start)))
         (filter true?)
         (count))))
