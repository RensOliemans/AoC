(ns aoc.2021.day06
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2021 6))

(defn- to-freqs [input]
  (let [f 
        (->> input
             s/parse-ints
             frequencies)]
    (->> (range 9)
         (map #(get f % 0))
         vec)))

(defn part1
  ([input] (part1 input 80))
  ([input t]
   (loop [fish (to-freqs input)
          t* t]
     (if (zero? t*)
       (reduce + fish)
       (recur
        (-> fish
            (subvec 1)
            (update 6 + (first fish))
            (conj (first fish)))
        (dec t*))))))

(defn part2 [input] (part1 input 256))
