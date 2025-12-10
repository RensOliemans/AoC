(ns aoc.2025.day10
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]
            [clojure.string :as str]))

(def input (d/day-input 2025 10))

(def tinput "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
")

(defn- parse-line [line]
  (let [[indicator & buttons] (str/split line #" ")
        joltage (last buttons)
        buttons (->> (butlast buttons)
                     (map s/parse-ints))]
    {:indicator indicator :buttons buttons :joltage joltage}))

(defn- toggle [x]
  (condp = x
    \. \#
    \# \.))

(defn- press-button
  "Presses a button, which is a list of indices.

  For example, with indicator: `.##.` and button `(0 2)`, this will
  output `##..` (though a vector)."
  [indicator idxs]
  (reduce (fn [acc i]
            (update acc i toggle))
          indicator
          idxs))

(defn- press-buttons [indicator buttons]
  (reduce press-button (vec indicator) buttons))

(defn- amount-indicator
  "Computes the minimum amount of button presses necessary to obtain a
  given indicator by brute-forcing every possible combination."
  [data]
  (let [ind (-> (:indicator data)
              (str/replace #"\[" "")
              (str/replace #"\]" ""))
        empty-ind (apply str (repeat (count ind) \.))]

    (loop [n 1]
      (let [btns (v/combinations-with-replacement (:buttons data) n)]
        (if (not (empty? (filter #(= ind (apply str (press-buttons empty-ind %)))
                                 btns)))
          n
          (recur (inc n)))))))

(defn part1 [input]
  (->> input
       s/parse-lines
       (map parse-line)
       (map amount-indicator)
       (reduce +)))
