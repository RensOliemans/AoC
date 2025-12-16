(ns aoc.2022.day04
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2022 04))

(defn- fully-overlap? [[s1 e1 :as r1] [s2 e2 :as r2]]
  (cond
    (= e1 e2) true
    (> e1 e2) (<= s1 s2)
    (< e1 e2) (>= s1 s2)))

(defn part1 [input]
  (->> (s/parse-lines input)
       (map #(str/split % #","))
       (map s/parse-ranges)
       (filter #(apply fully-overlap? %))
       count))

(defn- partly-overlap? [[s1 e1] [s2 e2]]
  (cond
    (= e1 e2) true
    (> e1 e2) (>= e2 s1)
    (< e1 e2) (>= e1 s2)))

(defn part2 [input]
  (->> (s/parse-lines input)
       (map #(str/split % #","))
       (map s/parse-ranges)
       (filter #(apply partly-overlap? %))
       count))

