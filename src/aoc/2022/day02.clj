(ns aoc.2022.day02
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.set :refer [map-invert]]
            [clojure.string :as str]))

(def input (d/day-input 2022 02))

(def shape-score {:rock 1, :paper 2, :scissors 3})

(def outcome-score {:loss 0, :draw 3, :win 6})

(def beats {:rock :scissors, :paper :rock, :scissors :paper})

(def beaten-by (map-invert beats))

(def opponent-map {"A" :rock, "B" :paper, "C" :scissors})

(def you-map {"X" :rock, "Y" :paper, "Z" :scissors})

(defn- outcome [opponent you]
  (cond
    (= opponent you) :draw
    (= (beats you) opponent) :win
    :else :loss))

(defn- score-round [opponent you]
  (+ (shape-score you)
     (outcome-score (outcome opponent you))))

(defn part1 [input]
  (->> (s/parse-lines input)
       (map #(str/split % #" "))
       (map (fn [[o y]]
              (score-round (opponent-map o) (you-map y))))
       (reduce +)))

(def instruction-map {"X" :loss, "Y" :draw, "Z" :win})

(defn choose-shape [opponent instruction]
  (case instruction
    :draw opponent
    :win (beaten-by opponent)
    :loss (beats opponent)))

(defn part2 [input]
  (->> (s/parse-lines input)
       (map #(str/split % #" "))
       (map (fn [[o i]]
              (let [opp (opponent-map o)
                    instr (instruction-map i)
                    you (choose-shape opp instr)]
                (score-round opp you))))
       (reduce +)))
