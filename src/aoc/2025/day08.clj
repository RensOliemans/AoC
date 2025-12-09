(ns aoc.2025.day08
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.set :as set]))

(def input (d/day-input 2025 8))

(defn- pairs
  [elements]
  (for [i (range (count elements))
        j (range (count elements))
        :while (< j i)]
    [(nth elements i) (nth elements j)]))

(defn- distance [[x y z] [x' y' z']]
  (Math/sqrt (+ (Math/pow (- x' x) 2) (Math/pow (- y' y) 2) (Math/pow (- z' z) 2))))

(defn part1
  ([input] (part1 input 1000))
  ([input n]
   (let [shortest-pairs
         (->> input
              s/parse-lines
              (map s/parse-ints)
              pairs
              (map (fn [[a b]] {[a b] (distance a b)}))
              (into {})
              (sort-by val)
              (take n))]
     (->> shortest-pairs
          (reduce (fn [sets [[one two] distance]]
                    (let [s1 (some #(if (contains? % one) % nil) sets)
                          s2 (some #(if (contains? % two) % nil) sets)]
                      (-> (remove
                           #(or (= s1 %)
                                (= s2 %)) sets)
                          (conj (set/union (or s1 #{one}) (or s2 #{two}))))))
                  [])
          (map count)
          (sort >)
          (take 3)
          (reduce *)))))

(defn part2 [input]
  (let [boxes (->> input
                   s/parse-lines
                   (map s/parse-ints))
        shortest-pairs
        (->> boxes
             pairs
             (map (fn [[a b]] {[a b] (distance a b)}))
             (into {})
             (sort-by val))]
    (loop [sets []
           remaining shortest-pairs]
      (let [[[one two] distance] (first remaining)
            s1 (some #(if (contains? % one) % nil) sets)
            s2 (some #(if (contains? % two) % nil) sets)
            new-sets
            (-> (remove #(or (= s1 %)
                             (= s2 %)) sets)
                (conj (set/union (or s1 #{one}) (or s2 #{two}))))]
        (if (and (= 1 (count new-sets))
                 (every? (fn [x] (contains? (first new-sets) x)) boxes))
          (* (first one) (first two))
          (recur new-sets (rest remaining)))))))
