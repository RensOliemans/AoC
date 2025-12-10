(ns aoc.2025.day03
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2025 3))

(defn- result [line n]
  (last
   (reduce
    (fn [[current val] k]
      (let [maxim (->> current
                       (drop-last (dec k))
                       (apply max))
            new-bank (->> current
                          (drop-while #(< % maxim))
                          rest)]
        [new-bank (+ maxim (* 10 val))]))
    [line 0]
    (range n 0 -1))))

(defn- solve [input n]
  (->> input
       s/parse-lines
       (map s/->digits)
       (pmap #(result % n))
       (reduce +)))

(defn part1 [input] (solve input 2))

(defn part2 [input] (solve input 12))
