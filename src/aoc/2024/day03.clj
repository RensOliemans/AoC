(ns aoc.2024.day03
  (:require [aoc.util.day :as d]
            [clojure.string :as str]))

(def input (d/day-input 2024 03))

(defn part1 [input]
  (->> (re-seq #"mul\((\d+),(\d+)\)" input)
       (map (fn [[_ a b]] (* (Long/parseLong a)
                             (Long/parseLong b))))
       (reduce +)))

(defn part2 [input]
  (let [inp (-> input
                (str/replace #"\n" "")
                (str/replace #"don't\(\).+?do\(\)" ""))]
    (part1 inp)))
