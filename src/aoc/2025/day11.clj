(ns aoc.2025.day11
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]
            [clojure.string :as str]))

(def input (d/day-input 2025 11))

(defn- parse-input [input]
  (->> input
       s/parse-lines
       (map #(str/split % #":? "))
       (map (fn [[from & to]] {from (set to)}))
       (into {})
       v/invert-map-of-sets))

(defn- count-paths-to-goal-dac-fft
  ([m goal cur] (count-paths-to-goal-dac-fft m goal cur false false))
  ([m goal cur dac fft]
   (if (= cur goal)
     (if (and dac fft) 1 0)
     (let [neighbours (m cur)]
       (reduce + (map #(count-paths-to-goal-dac-fft m goal %
                                                    (or dac (= cur "dac"))
                                                    (or fft (= cur "fft")))
                      neighbours))))))
(def count-paths-to-goal-dac-fft (memoize count-paths-to-goal-dac-fft))

(defn part1 [input]
  (count-paths-to-goal-dac-fft (parse-input input) "you" "out" true true))

(defn part2 [input]
  (count-paths-to-goal-dac-fft (parse-input input) "svr" "out"))
