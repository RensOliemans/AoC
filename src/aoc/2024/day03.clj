(ns aoc.2024.day03
  (:require [aoc.util.day :as d]
            [clojure.string :as str]))

(def input (d/day-input 2024 03))

(defn part1 [input]
  (let [matches (re-seq #"mul\((\d+),(\d+)\)" input)]
    (->> matches
         (map #(list (Integer/parseInt (nth % 1)) (Integer/parseInt (nth % 2))))
         (map #(apply * %))
         (reduce +))))

(defn part2 [input]
  (let [inp (-> input
                (str/replace #"\n" "")
                (str/replace #"don't\(\).+?do\(\)" ""))]
    (part1 inp)))
