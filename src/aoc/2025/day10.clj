(ns aoc.2025.day10
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str])
  (:import [org.ojalgo.optimisation ExpressionsBasedModel Variable]
           [org.ojalgo.optimisation Optimisation$State]))

(def input (d/day-input 2025 10))

(defn- parse-line [line]
  (let [[indicator & buttons] (str/split line #" ")
        indicator (-> indicator
                      (str/replace #"\[" "")
                      (str/replace #"\]" ""))
        joltage (->> (last buttons)
                     s/parse-ints)
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
  (let [ind (:indicator data)
        empty-ind (apply str (repeat (count ind) \.))]

    (loop [n 1]
      (let [btns (combo/combinations (:buttons data) n)]
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

(defn- solve [buttons joltage]
  (let [nb (count buttons)
        nj (count joltage)
        model (ExpressionsBasedModel.)]

    (doseq [b (range nb)]
      (doto (.addVariable model)
        (.lower 0)
        (.weight 1)
        (.integer true)))

    (doseq [j (range nj)]
      (let [expr (.addExpression model)]
        (.level expr (get joltage j))
        (doseq [b (range nb)]
          (when (some #{j} (get buttons b))
            ;; connect button b to constraint j
            (.set expr b 1)))))

    (let [result (.minimise model)
          state (.getState result)]
      ;; (.intValue) sometimes get things wrong, 2.9999999994 becomes
      ;; 2, round ourselves
      (vec (for [i (range nb)]
             (Math/round (.doubleValue (.get result i))))))))

(defn part2 [input]
  (let [problems
        (->> input
             s/parse-lines
             (map parse-line))]
    (->> problems
         (pmap (fn [problem]
                 (solve (vec (:buttons problem)) (:joltage problem))))
         (map #(reduce + %))
         (reduce +))))
