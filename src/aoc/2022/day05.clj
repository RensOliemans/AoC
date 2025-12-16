(ns aoc.2022.day05
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.grid :as g]
            [aoc.util.vec :as v]))

(def input (d/day-input 2022 05))

(defn- parse-crates
  "Takes a block and returns the columns of crate letters."
  [block]
  (let [g (s/parse-lines block)
        crates (butlast g)
        all-stacks (v/transpose crates)]
    (->> all-stacks
         (keep-indexed (fn [idx itm]
                         (when (and (= 1 (mod idx 2))
                                    (not (= 0 (mod (inc idx) 4))))
                           (filter #(not= \space %) itm)))))))

(defn- parse-procedure [line]
  (let [groups
        (->> line
             (re-seq #"move (\d+) from (\d+) to (\d+)")
             first
             rest)]
    {:amount (parse-long (nth groups 0))
     :from (dec (parse-long (nth groups 1)))
     :to (dec (parse-long (nth groups 2)))}))

(defn- parse-procedures [block]
  (map parse-procedure (s/parse-lines block)))

(defn- apply-procedure
  "Applies a procedure to a list of stacks."
  [cratemover stacks procedure]
  (let [tomove (take (:amount procedure) (nth stacks (:from procedure)))
        tomove (case cratemover
                 :CrateMover9000 tomove
                 :CrateMover9001 (reverse tomove)
                 tomove)]
    (map-indexed
     (fn [idx stack]
       (cond
         (= idx (:from procedure)) (drop (:amount procedure) stack)
         (= idx (:to procedure)) (apply conj stack tomove)
         :else stack
         ))
     stacks)))


(defn- result [input cratemover]
  (let [blocks (s/parse-blocks input)
        crates (parse-crates (first blocks))
        procedures (parse-procedures (last blocks))
        apply-procedure (partial apply-procedure cratemover)]
    (->> 
     (reduce apply-procedure crates procedures)
     (map first)
     (apply str))))

(defn part1 [input] (result input :CrateMover9000))

(defn part2 [input] (result input :CrateMover9001))
