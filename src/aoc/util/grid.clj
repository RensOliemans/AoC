(ns aoc.util.grid
  (:require [clojure.string :as str]))

(defn parse-grid-map
  "Parse a grid, optionally changing values"
  ([input] (parse-grid input identity))
  ([input value-fn]
   (let [lines (str/split-lines input)]
     (into {}
           (for [x (range (count (first lines)))
                 y (range (count lines))]
             [[x y] (value-fn (get-in lines [y x]))])))))

(defn to-matrix
  "Turn a blob (or block) into a vector of vectors, possibly substituting vals"
  ([input] (to-matrix input identity))
  ([input value-fn]
   (let [lines (str/split-lines input)]
     (let [with-subs (fn [line] (mapv #(value-fn %) line))]
       (mapv with-subs lines)))))

(defn height [grid] (count grid))
(defn width [grid] (count (first grid)))


(defn col [grid x] (mapv #(nth % x) grid))

(defn row [grid y] (nth grid y))

(defn cell
  ([grid [x y]] (nth (nth grid y) x))
  ([grid [x y] not-found] (or (nth (nth grid y) x) not-found)))

(defn locs-where
  "Finds the [row col] locations where pred holds in a grid."
  [grid pred]
  (for [y (range (height grid))
        x (range (width grid))
        :when (pred (cell grid [x y]))]
    [x y]))
