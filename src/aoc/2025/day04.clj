(ns aoc.2025.day04
  (:require [aoc.util.day :as d]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]
            ;; [aoc.util.string :as s]
	    ))

(def input (d/day-input 2025 4))

(defn- removable-rolls [grid]
  (->> (g/locs-where grid #(= \@ %))       ; all roll locations
       (map (fn [point]
              (let [neighs (v/adjacent-to point)
                    cnt (count (filter #(= \@ (get grid %)) neighs))]
                {:point point :neighbours neighs :count cnt})))
       ;; a roll is `removable` if it has less than 4 rolls as neighb
       (filter #(< (:count %) 4))))

(defn part1 [input]
  (->> input
       g/parse-grid
       removable-rolls
       count))

(defn part2 [input]
  (let [grid (g/parse-grid input)]
    (loop [grid* grid
           removed 0]
      (let [to-remove (removable-rolls grid*)]
        (if (empty? to-remove)
          removed
          (recur
           (reduce (fn [g p] (assoc g (:point p) \.))
                   grid*
                   to-remove)
           (+ removed (count to-remove))))))))

(part2 input)
