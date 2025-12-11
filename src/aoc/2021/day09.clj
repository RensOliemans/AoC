(ns aoc.2021.day09
  (:require [aoc.util.day :as d]
            [aoc.util.grid :as g]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]))

(def input (d/day-input 2021 9))

(defn low-point? [grid [point val]]
  (every? (fn [neighbour]
            (> (get grid neighbour Long/MAX_VALUE) val))
          (v/adjacent-to point)))

(defn part1 [input]
  (let [g (g/parse-grid input s/digits)]
    (->> g
         (filter #(low-point? g %))
         (map val)
         (map inc)
         (reduce +))))

(defn queue
  ([] clojure.lang.PersistentQueue/EMPTY)
  ([coll] (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(defn flood-fill [start barrier? neighbours-fn]
  (loop [seen #{}
         q (queue [start])]
    (if (empty? q)
      seen
      (let [p (peek q)
            q (pop q)]
        (if (seen p)
          (recur seen q)
          (let [neighbours (remove barrier? (neighbours-fn p))]
            (recur (conj seen p) (into q neighbours))))))))


(defn part2 [inp]
  (let [g (g/parse-grid inp s/digits)
        barrier? #(let [v (g % 10)] (>= v 9))]
    (->> g
         (filter #(low-point? g %))
         (map (fn [[point _]] (flood-fill point barrier? v/cardinal-to)))
         (map count)
         (sort >)
         (take 3)
         (reduce *))))

