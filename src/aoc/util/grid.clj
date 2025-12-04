(ns aoc.util.grid
  (:require [clojure.string :as str]))

(defn parse-grid
  "Parse a grid, optionally changing values"
  ([input] (parse-grid input identity))
  ([input value-fn]
   (let [lines (str/split-lines input)]
     (into {}
           (for [x (range (count (first lines)))
                 y (range (count lines))]
             [[x y] (value-fn (get-in lines [y x]))])))))


(defn locs-where [grid pred]
  (map key (filter #(pred (val %)) grid)))
