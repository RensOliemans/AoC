(ns aoc.2024.day04
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

(def input (d/day-input 2024 04))

(defn is-xmas? "Does the grid `grid` contain the string \"XMAS\",
   starting at `start` and going in `direction`?"
  [grid start direction]
  (loop [location start
         chars (seq "XMAS")]
    (if (empty? chars)
      true
      (if (not= (get-in grid location) (first chars))
        false
        (recur 
         (mapv + location direction)
         (rest chars))))))

(defn count-xmases-at [grid start directions]
  (count (filter #(is-xmas? grid start %) directions)))

(defn part1 [input]
  (let [grid (g/to-matrix input)
        xs (g/locs-where grid #(= % \X))]
    (->> xs
         (map #(count-xmases-at grid % v/adjacent-dirs))
         (reduce +))))

(defn is-mas? [grid middle direction]
  (let [opposite-direction (mapv #(* -1 %) direction)]
    (and (= \M (get-in grid (mapv + middle direction)))
         (= \S (get-in grid (mapv + middle opposite-direction))))))

(defn count-mases-at [grid middle directions]
  (count (filter #(is-mas? grid middle %) directions)))

(defn part2 [input]
  (let [grid (g/to-matrix input)
        as (g/locs-where grid #(= % \A))]
    (->> as
         (map #(count-mases-at grid % v/diagonal-dirs))
         (filter #(= % 2))
         (count))))
