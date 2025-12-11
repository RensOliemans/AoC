(ns aoc.2021.day10
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2021 10))

(def opening-brackets #{\( \[ \{ \<})
(def bracket-pairs {\) \(, \] \[, \} \{, \> \<})
(def mismatch-scores {\) 3, \] 57, \} 1197, \> 25137})

(defn- parse-line [line]
  (loop [open []
         i 0]
    (if (= i (count line))
      {:result :incomplete :open open}
      (let [c (nth line i)]
        (cond
          ;; opening bracket, add to open and continue
          (opening-brackets c) (recur (conj open c) (inc i))
          ;; closing bracket and a match: remove from open and
          ;; continue
          (= (bracket-pairs c) (peek open)) (recur (pop open) (inc i))
          ;; closing bracket and no match: invalid
          :else {:result :invalid :score (mismatch-scores c)})))))

(defn part1 [input]
  (->> input
       s/parse-lines
       vec
       (map parse-line)
       (filter #(= :invalid (:result %)))
       (map :score)
       (reduce +)))

(def completion-scores {\( 1, \[ 2, \{ 3, \< 4})

(defn- score [brackets]
  (reduce (fn [acc i]
            (+ (completion-scores i) (* 5 acc)))
          0
          (rseq brackets)))

(defn part2 [input]
  (let [scores
        (->> input
             s/parse-lines
             vec
             (map parse-line)
             (filter #(= :incomplete (:result %)))
             (map :open)
             (map score)
             sort)]
    (nth scores (quot (count scores) 2))))

