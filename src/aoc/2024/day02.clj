(ns aoc.2024.day02
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2024 02))

(defn diffs [record]
  (->> record
       (partition 2 1)
       (mapv (fn [[a b]] (- b a)))))

(defn is-safe? [record]
  (let [differences (diffs record)]
    (and (every? #(<= 1 (abs %) 3) differences)
         (apply = (map pos? differences)))))

(defn part1 [input]
  (->> (s/parse-lines input s/parse-ints)
       (filter is-safe?)
       (count)))

(defn drop-nth [coll n]
  (keep-indexed #(if (not= %1 n) %2) coll))

(defn dampened-is-safe? [record]
  (some is-safe? (map #(drop-nth record %)
                      (range (count record)))))

(defn part2 [input]
  (->> (s/parse-lines input s/parse-ints)
       (filter dampened-is-safe?)
       (count)))
