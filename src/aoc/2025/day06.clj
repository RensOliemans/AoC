(ns aoc.2025.day06
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]
            [clojure.string :as str]))

(def input (d/day-input 2025 6))

(defn- result
  [cols ops]
  (reduce
   (fn [acc [col op]]
     (+ acc
        (condp = op
          "*" (reduce * col)
          "+" (reduce + col)
          ))
     )
   0
   (zipmap cols ops)))

(defn part1 [input]
  (let [inp (s/parse-lines input)
        nums (drop-last 1 inp)
        cols
        (->> nums
             (map s/parse-ints)
             v/transpose)
        ops (str/split (last inp) #" +")]
    (result cols ops)))

(defn group-while
  "Groups a collection into subgroups as long as `pred` holds."
  [pred coll]
  (loop [res [[]]
         i 0]
    (if (= i (count coll))
      res
      (let [x (nth coll i)]
        (if (pred x)
          (recur (assoc res (dec (count res)) (conj (last res) x)) (inc i))
          (recur (conj res []) (inc i)))))))


(defn part2 [input]
  (let [inp (->> (s/parse-lines input)
                 v/transpose
                 (group-while (fn [x] (not (every? #(= \space %) x)))))]
    (reduce (fn [total row]
              (let [op (last (first row))]
                (+ total
                   (condp = op
                     \* (reduce * (map #(first (s/parse-ints (str/join %))) row))
                     \+ (reduce + (map #(first (s/parse-ints (str/join %))) row))))))
            0
            inp)))
