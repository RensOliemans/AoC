(ns aoc.2022.day03
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.set :as set]
            [clojure.string :as str]))

(def input (d/day-input 2022 03))

(defn- split-in-two [v]
  (let [c (count v)
        h (quot c 2)]
    [(subvec v 0 h) (subvec v h)]))

(defn- common-items [& vecs]
  (apply set/intersection (map set vecs)))

(defn- char-to-num [c]
  (if (Character/isUpperCase c)
    (- (int c) 38)
    (- (int c) 96)))

(defn part1 [input]
  (->> (s/parse-lines input)
       (map vec)
       (map split-in-two)
       (mapcat #(apply common-items %))
       (map char-to-num)
       (reduce +)))

(defn part2 [input]
  (->> (s/parse-lines input)
       (map vec)
       (partition 3)
       (mapcat #(apply common-items %))
       (map char-to-num)
       (reduce +)))
