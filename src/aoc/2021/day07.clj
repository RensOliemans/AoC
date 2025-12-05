(ns aoc.2021.day07
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2021 7))

(defn- regular-cost [numbers target]
  (->> numbers
       (map #(abs (- % target)))
       (reduce +)))

(defn minimal-binary-search
  "Binary search across list until we found element with minimal value.
  Our binary search is a bit trickier here, since we don't know what
  value we should look for. So instead, for each value n, we check the
  values around it ((dec n) (inc n)) to see what direction we should
  move."
  [input costfn]
  (let [vals (sort (s/parse-ints input))
        cnt (count vals)
        cost (partial costfn vals)]
    (loop [a 0
           b cnt]
      (let [n (quot (+ a b) 2)
            val   (cost n)
            val-1 (cost (dec n))
            val+1 (cost (inc n))]
        (cond
          (= val (min val val-1 val+1)) val
          (> val-1 val) (recur n cnt)
          (> val+1 val) (recur a n))))))

(defn part1 [input] (minimal-binary-search input regular-cost))

(defn- increasing-cost [numbers target]
  (->> numbers
       (map #(abs (- % target)))
       (map #(/ (* % (inc %)) 2))
       (reduce +)))

(defn part2 [input] (minimal-binary-search input increasing-cost))
