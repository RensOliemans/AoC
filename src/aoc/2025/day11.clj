(ns aoc.2025.day11
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2025 11))

(defn- parse-input [input]
  (->> input
       s/parse-lines
       (map #(re-seq #"\w+" %))
       (map (fn [[from & to]] [from to]))
       (into {})))

(defn- count-paths-to-goal-dac-fft
  ([m cur goal] (count-paths-to-goal-dac-fft m cur goal false false))
  ([m cur goal dac fft]
   (if (= cur goal)
     (if (and dac fft) 1 0)
     (let [neighbours (m cur)]
       (reduce + (map #(count-paths-to-goal-dac-fft m % goal
                                                    (or dac (= cur "dac"))
                                                    (or fft (= cur "fft")))
                      neighbours))))))
(def count-paths-to-goal-dac-fft (memoize count-paths-to-goal-dac-fft))

(defn part1 [input]
  (count-paths-to-goal-dac-fft (parse-input input) "you" "out" true true))

(defn part2 [input]
  (count-paths-to-goal-dac-fft (parse-input input) "svr" "out"))
