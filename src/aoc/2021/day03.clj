(ns aoc.2021.day03
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [aoc.util.vec :as v]))

(def input (d/day-input 2021 3))

(defn gammarate-epsilon
  [bitstrings comp]
  (let [gammarate-bitstring 
        (->> bitstrings
             v/transpose
             (map (fn [bitstring]
                    (let [ones (count (filter #(= \1 %) bitstring))
                          half (/ (count bitstrings) 2)]
                      (cond
                        (> ones half) 1
                        (< ones half) 0
                        (= ones half) (if comp 1 0)))))
             (apply str))
        gammarate (Long/parseLong gammarate-bitstring 2)
        mask (dec (bit-shift-left 1 (count gammarate-bitstring)))]
    [gammarate (bit-xor gammarate mask)]))

(defn part1 [input]
  (let [inp (s/parse-lines input)
        [gammarate epsilon] (gammarate-epsilon inp true)]
    (* gammarate epsilon)))

(defn- toBinstring12 [n]
  (-> n
      (Integer/toString 2)
      Long/parseLong
      (->> (format "%012d"))))

(defn- find-element-recursive
  [inp gamma?]
  (loop [idx 0
         possible inp]
    (let [[gamma epsilon] (gammarate-epsilon possible true)
          val (if gamma? gamma epsilon)
          bit (nth (toBinstring12 val) idx)]
      (if (= 1 (count possible))
        (first possible)
        (recur (min 11 (inc idx))
               (filter #(= bit (nth % idx)) possible))))))

(defn part2 [input]
  (let [inp (s/parse-lines input)
        bitstrings (v/transpose inp)
        oxygen (find-element-recursive inp true)
        co2 (find-element-recursive inp nil)]
    (* (Integer/parseInt oxygen 2) (Integer/parseInt co2 2))))
