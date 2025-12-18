(ns aoc.2024.day07
  (:require [aoc.util.day :as d]
            [clojure.string :as str] 
            [aoc.util.string :as s]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2024 07))

(defn- is-correct? [[result & numbers] operators]
  (let [possibilities
        (reduce (fn [acc i]
                  (mapcat (fn [n] (map #(% n i) operators)) acc))
                [(first numbers)]
                (rest numbers))]
    (some #(= result %) possibilities)))

(defn- sum-correct-equations [input operators]
  (->> (s/parse-lines input)
       (map s/parse-ints)
       (filter #(is-correct? % operators))
       (map first)
       (reduce +)))

(defn part1 [input]
  (let [operators [#(* %1 %2)
                   #(+ %1 %2)]]
    (sum-correct-equations input operators)))

(defn part2 [input]
  (let [operators [#(* %1 %2)
                   #(+ %1 %2)
                   #(Long/parseLong (str %1 %2))]]
    (sum-correct-equations input operators)))
