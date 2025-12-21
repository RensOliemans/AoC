(ns aoc.2022.day09
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]
            ;; [aoc.util.vec :as v]
	    ))

(def input (d/day-input 2022 9))

(defn- move [[x y] [x' y']]
  [(+ x x') (+ y y')])

(defn- parse-input [input]
  (->> (s/parse-lines input)
       (map #(str/split % #" "))
       (map (fn [[dir amount]] [(parse-dir dir) (Long/parseLong amount)]))))

(defn- parse-dir [dir]
  (case dir
    "R" [+1 +0]
    "L" [-1 +0]
    "U" [+0 -1]
    "D" [+0 +1]))

(defn- head-path
  "Given a path and a motion, returns the path with motion appended."
  [path [dir amount]]
  (loop [i amount
         curr (peek path)
         p path]
    (if (zero? i)
      p
      (let [next (move curr dir)]
        (recur (dec i)
               next
               (conj p next))))))


(defn- adjacent? [[x y] [x' y']]
  (>= 1 (max (abs (- x x'))
             (abs (- y y')))))

(defn part1 [input]
  (let [path (->> (parse-input input)
                  (reduce head-path [[0 0]]))
        initial (first path)
        [tail-path _ _]
        (reduce (fn [[visited tail head] head']
                  (let [movement (mapv - tail head')]
                    (cond
                      ;; tail stays where it is
                      (->> movement
                           (map abs)
                           (apply max)
                           (>= 1))
                      [(conj visited tail) tail head']
                      ;; H moves, T jumps along
                      :else [(conj visited head) head head'])))
                [#{initial} initial initial]
                (rest path))]
    (count tail-path)))

(defn part2 [input])
