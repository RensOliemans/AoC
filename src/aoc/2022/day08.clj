(ns aoc.2022.day08
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.grid :as g]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2022 8))

(defn- visible? [grid [x y]]
  (let [row (g/row grid y)
        col (g/col grid x)
        z (g/cell grid [x y])
        all-lower? (fn [coll]
                     (every? #(> z %) coll))]
    (or (all-lower? (subvec col 0 y))
        (all-lower? (subvec col (inc y)))
        (all-lower? (subvec row 0 x))
        (all-lower? (subvec row (inc x))))))


(defn part1 [input]
  (let [g (g/to-matrix input #(parse-long (str %)))]
    (count 
     (for [y (range (g/height g))
           x (range (g/width g))
           :when (visible? g [x y])]
       [x y]))))

(defn- score [grid [x y]]
  (let [row (g/row grid y)
        col (g/col grid x)
        z (g/cell grid [x y])
        num-lower-trees
        (fn [coll]
          (let [res
                (->> coll
                     (take-while #(> z %))
                     count)]
            ;; if there is one, include the tree that's higher, since
            ;; we can see that.
            (if (= res (count coll))
              res
              (inc res))))]
    (* (num-lower-trees (reverse (subvec col 0 y)))
       (num-lower-trees (subvec col (inc y)))
       (num-lower-trees (reverse (subvec row 0 x)))
       (num-lower-trees (subvec row (inc x))))))

(defn part2 [input]
  (let [g (g/to-matrix input #(parse-long (str %)))]
    (->>
     (for [y (range (g/height g))
           x (range (g/width g))]
       [[x y] (score g [x y])])
     (map last)
     (apply max))))
