(ns aoc.2024.day07
  (:require [aoc.util.day :as d]
            [clojure.string :as str] 
            [aoc.util.string :as s]))

(def input (d/day-input 2024 07))

(defn- operators-1 []
  [{:name :multiplication :reverse #(/ %1 %2) :valid? #(= 0 (mod %1 %2))}
   {:name :addition :reverse #(- %1 %2) :valid? #(> %1 %2)}])

(defn- operators-2 []
  (into (operators-1)
        [{:name :concatenation
          :reverse (fn [n x]
                     (let [s (str n)]
                       (Long/parseLong (subs s 0 (- (count s) (count (str x)))))))
          :valid? (fn [n x]
                    (let [sn (str n)
                          sx (str x)]
                      (and (> (count sn) (count sx))
                           (str/ends-with? sn sx))))}]))

(defn- is-correct?
  "Can `target` be made with `numbers`, using `operators`?"
  [target numbers operators]
  (if (= 1 (count numbers))
    (= target (first numbers))
    (->> operators
         (filter (fn [op] ((:valid? op) target (last numbers))))
         (some (fn [op]
                 (is-correct?
                  ((:reverse op) target (last numbers))
                  (butlast numbers)
                  operators))))))

(defn- sum-correct-equations [input operators]
  (->> (s/parse-lines input)
       (map s/parse-ints)
       (filter (fn [[target & numbers]] (is-correct? target numbers operators)))
       (map first)
       (reduce +)))

(defn part1 [input]
  (let [ops (operators-1)]
    (sum-correct-equations input ops)))

(defn part2 [input]
  (let [ops (operators-2)]
    (sum-correct-equations input ops)))
