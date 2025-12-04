(ns aoc.2021.day02
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2021 2))

(defn part1 [input]
  (->> input
       s/parse-lines
       (map #(str/split % #" "))
       (reduce (fn [[horizontal depth] [direction amount]]
                 (condp = direction
                   "forward" [(+ horizontal (parse-long amount)) depth]
                   "down" [horizontal (+ depth (parse-long amount))]
                   "up" [horizontal (- depth (parse-long amount))]))
               [0 0])
       (reduce * 1)))

(defn part2 [input]
  (->> input
       s/parse-lines
       (map #(str/split % #" "))
       (reduce (fn [[horizontal depth aim] [direction amount]]
                 (condp = direction
                   "forward" [(+ horizontal (parse-long amount))
                              (+ depth (* aim (parse-long amount)))
                              aim]
                   "down" [horizontal depth (+ aim (parse-long amount))]
                   "up" [horizontal depth (- aim (parse-long amount))]))
               [0 0 0])
       butlast
       (reduce *)))
