(ns aoc.2021.day04
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]))

(def input (d/day-input 2021 4))

(defn- parse-board
  "parses a board from input (([0-9]+ ){5}){5} into a seq of length 5,
  each containing a seq of length 5."
  [board]
  (->> board
       s/parse-lines
       (map s/parse-ints)))

(defn- winning
  [board numbers]
  (let [hit? #(every? (set numbers) %)]
    (when (or (some hit? board)
              (some hit? (v/transpose board)))
      board)))


(defn part1 [input]
  (let [[numbers & boards] (s/parse-blocks input)
        numbers (s/parse-ints numbers)
        boards (map parse-board boards)
        [winning-board selected]
        (reduce (fn [selected n]
                  (if-let [winner (some #(winning % selected) boards)]
                    (reduced [winner selected])
                    (conj selected n)))
                []
                numbers)]
    (->> winning-board
         flatten
         (remove (set selected))
         (reduce +)
         (* (last selected)))))

(defn part2 [input]
  (let [[numbers & boards] (s/parse-blocks input)
        numbers (s/parse-ints numbers)
        boards (map parse-board boards)
        [last-board selected]
        (loop [selected []
               nums numbers
               non-winners boards]
          (if (= 1 (count non-winners))
            [(first non-winners) selected]
            (recur (conj selected (first nums))
                   (rest nums)
                   (filter #(not (winning % selected)) non-winners))))]
    (->> last-board
             flatten
             (remove (set selected))
             (reduce +)
             (* (last selected)))))
