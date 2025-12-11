(ns aoc.2025.day11
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2025 11))

(defn- invert-map-of-sets [m]
  (reduce (fn [a [k v]] (assoc a k (conj (get a k #{}) v))) {} (for [[k s] m v s] [v k])))

(defn- parse-input [input]
  (->> input
       s/parse-lines
       (map #(str/split % #":? "))
       (map (fn [[from & to]] {from (set to)}))
       (into {})
       invert-map-of-sets))

(defn count-paths-to-goal [m goal cur]
  (if (= cur goal)
    1
    (let [neighbours (m cur)]
      (reduce + (map #(count-paths-to-goal m goal %) neighbours)))))
(def count-paths-to-goal (memoize count-paths-to-goal))

(defn part1 [input]
  (count-paths-to-goal (parse-input input) "you" "out"))

(defn- count-paths-to-goal-dac-fft
  ([m goal cur] (count-paths-to-goal-dac-fft m goal cur false false))
  ([m goal cur dac fft]
   (if (= cur goal)
     (if (and dac fft) 1 0)
     (let [neighbours (m cur)]
       (reduce + (map #(count-paths-to-goal-dac-fft m goal %
                                                    (or dac (= cur "dac"))
                                                    (or fft (= cur "fft")))
                      neighbours))))))
(def count-paths-to-goal-dac-fft (memoize count-paths-to-goal-dac-fft))

(defn part2 [input]
  (count-paths-to-goal-dac-fft (parse-input input) "svr" "out"))

(part2 input)
