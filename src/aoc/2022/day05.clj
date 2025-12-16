(ns aoc.2022.day05
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]))

(def input (d/day-input 2022 05))

(defn- parse-crates
  "Takes a block and returns the columns of crate letters."
  [block]
  (let [rows (-> block s/parse-lines butlast)
        cols (v/transpose rows)]
    (keep-indexed
     (fn [i col]
       (when (= 1 (mod i 4))
         (remove #{\space} col)))
     cols)))

(defn- parse-procedure [line]
  (let [[_ a f t] (re-find #"move (\d+) from (\d+) to (\d+)" line)]
    {:amount (parse-long a)
     :from   (dec (parse-long f))
     :to     (dec (parse-long t))}))

(defn- parse-procedures [block]
  (map parse-procedure (s/parse-lines block)))

(defn- apply-procedure
  "Applies a procedure to a list of stacks."
  [crane stacks {:keys [amount from to]}]
  (let [src (nth stacks from)
        tomove (take amount src)
        tomove (if (= crane :CrateMover9001) (reverse tomove) tomove)]
    (-> stacks
        vec
        (assoc from (drop amount src))
        (update to #(apply conj % tomove)))))

(defn- result [input crane]
  (let [[crates-block procedures-block] (s/parse-blocks input)]
    (->> (parse-procedures procedures-block)
         (reduce (partial apply-procedure crane)
                 (parse-crates crates-block))
     (map first)
     (apply str))))

(defn part1 [input] (result input :CrateMover9000))

(defn part2 [input] (result input :CrateMover9001))
