(ns aoc.2024.day05
  (:require [aoc.util.day :as d]
            [clojure.string :as str]
            ;; [aoc.util.string :as s]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2024 05))

(defn dep-sort
  "Sort a list of strings based on a dependency map.
     The map defines which elements should come after others."
  [dep-graph update]
  (let [graph (reduce (fn [acc item]
                        (assoc acc item 
                               (set (get dep-graph item []))))
                      {} update)
        local-deps (fn [deps] (filter #(contains? (set update) %) deps))]
    (vec (sort-by (fn [item]
                    (let [deps (get dep-graph item [])]
                      (count (local-deps deps))))
                  update))))


(defn build-dependency-graph
  [orderings]
  (let [order-pairs (->> orderings
                         (map #(str/split % #"\|"))
                         (map #(hash-map (second %), [(first %)])))]
    (apply (partial merge-with into) order-pairs)))

(defn dep-sorted? [dependency-graph update]
  (= update (dep-sort dependency-graph update)))

(defn middle-num
  "Finds the middle string in a list of string, and parses it to a
    number. Assumes the length of the list list is odd."
  [update]
  (read-string (nth update (/ (count update) 2))))

(defn parse-input
  "Parses an input string and returns three useful objects.
    The first obj is a list of orderings, strings of type \"A|B\".
    The second obj is a list of updates, each one a list of strings.
    The third obj is a dependency graph, a map."
  [input]
  (let [[orderings updates] (str/split input #"\n\n")
        orderings (str/split orderings #"\n")
        updates (str/split updates #"\n")
        updates (map #(str/split % #",") updates)
        dependency-graph (build-dependency-graph orderings)]
    [orderings updates dependency-graph]))

(defn part1 [input]
  (let [[orderings updates dep-graph] (parse-input input)
        dep-sorted? (partial dep-sorted? dep-graph)]
    (->> updates
         (pmap #(list % (dep-sorted? %)))
         (filter last)
         (pmap first)
         (pmap middle-num)
         (reduce +))))

(defn part2 [input]
  (let [[orderings updates deps] (parse-input input)
        is-sorted? (partial dep-sorted? deps)
        dep-sort (partial dep-sort deps)]
    (->> updates
         (pmap #(list % (dep-sort %)))
         (filter #(not= (first %) (last %)))
         (pmap last)
         (pmap middle-num)
         (reduce +))))
