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

(defn- count-paths-to-goal
  [m cur goal]
  (if (= cur goal)
    1
    (let [neighbours (m cur)]
      (reduce + (map #(count-paths-to-goal m % goal)
                     neighbours)))))

(def count-paths-to-goal (memoize count-paths-to-goal))

(defn part1 [input]
  (count-paths-to-goal (parse-input input) "you" "out"))

(defn part2 [input]
  (let [g (parse-input input)
        [first second] (if (pos? (count-paths-to-goal g "fft" "dac"))
                         ["fft" "dac"]
                         ["dac" "fft"])]
    (* (count-paths-to-goal g "svr" first)
       (count-paths-to-goal g first second)
       (count-paths-to-goal g second "out"))))
