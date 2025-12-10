(ns aoc.2021.day08
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]
            [clojure.set :refer [map-invert]]))

(def input (d/day-input 2021 8))

(defn part1 [input]
  (->> input
       s/parse-lines
       (map #(str/split % #" \| "))
       (map last)
       (mapcat #(str/split % #" "))
       (map count)
       (filter #(or (= % 2)
                    (= % 3)
                    (= % 4)
                    (= % 7)))
       count))

(defn- map-wires
  "Maps wires from a given input, where each word in the input is a list
  of segment connections. We use the following grid for reference:

       aaaa 
      b    c
      b    c
       dddd 
      e    f
      e    f
       gggg 
  "
  [input]
  (let [mappings (as-> input x
                   (str/split x #" ")
                   (sort-by count x)
                   (vec x))
        mapping (atom {})
        one     (nth mappings 0)
        seven   (nth mappings 1)
        four    (nth mappings 2)
        middle  (subvec mappings 3 6)
        large   (subvec mappings 6 9)]

    ;; c and f
    (swap! mapping assoc
           \c (vec one)
           \f (vec one))

    ;; a
    (swap! mapping assoc
           \a (first (remove (set (flatten (vals @mapping))) seven)))

    ;; b and d are the segments that are in 4 but not in 1. Of these,
    ;; only b occurs in all large numbers (0, 6, 9), so we know these
    ;; for sure.
    (let [only-four (remove (set one) four)]
      (doseq [c only-four]
        (swap! mapping assoc
               (if (every? #(some #{c} %) large) \b \d) c)))

    ;; c and f disambiguation: 6 does not have c.
    (let [d-val    (@mapping \d)
          cs       (@mapping \c)]
      (doseq [c cs
              :let [other (first (remove #{c} cs))]]
        (when (some #(not (some #{c} %)) large)
          (swap! mapping assoc \c c \f other))))

    ;; e and g, only remaining chars. Only g will be used by all three
    ;; remaining 'middle' numbers (3, 4, 5)
    (let [used       (set (vals @mapping))
          remaining  (vec (remove used "abcdefg"))]
      (doseq [l remaining
              :let [o (first (remove #{l} remaining))]]
        (when (every? #(some #{l} %) middle)
          (swap! mapping assoc \g l \e o))))

    @mapping))

(defn- to-number [mapping patterns]
  (condp = (count patterns)
    2 1
    3 7
    4 4
    5 (condp = (sort (map mapping patterns))
        '(\a \c \d \e \g) 2
        '(\a \c \d \f \g) 3
        '(\a \b \d \f \g) 5
        )
    6 (condp = (sort (map mapping patterns))
        '(\a \b \c \e \f \g) 0
        '(\a \b \d \e \f \g) 6
        '(\a \b \c \d \f \g) 9
        )
    7 8))

(defn- find-output [line]
  (let [[connections output] (str/split line #" \| ")
        mapping (map-wires connections)
        mapping (map-invert mapping)
        result (map #(to-number mapping %) (str/split output #" "))]
    (apply str result)))

(defn part2 [input]
  (->> input
       s/parse-lines
       (pmap find-output)
       (map parse-long)
       (reduce +)))
