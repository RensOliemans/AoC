(ns aoc.2022.day07
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]
            [clojure.string :as str]))

(def input (d/day-input 2022 07))

(defn- pts [pwd]
  (apply str (reverse pwd)))

(defn- directory-sizes [input]
  (let [[pwd dir-sizes]
        (reduce
         (fn [[pwd dir-sizes] line]
           (condp re-matches line
             #"\$ ls" [pwd dir-sizes]
             #"dir \w+" [pwd dir-sizes]
             #"\$ cd \.\." [(pop pwd) dir-sizes]
             #"\$ cd /" [(conj pwd "/") (assoc dir-sizes "/" 0)]
             #"\$ cd ([\w]+)"
             :>> (fn [[_ dir]]
                   (let [d (str dir "/")
                         pwd' (conj pwd d)]
                     [pwd' (assoc dir-sizes (pts pwd') 0)]))
             #"([0-9]+) .+"
             :>> (fn [[_ filesize-str]]
                   (let [filesize (parse-long filesize-str)]
                     ;; add filesize to current directory and all
                     ;; parent dirs (in pwd)
                     [pwd
                      (loop [sizes dir-sizes
                             pwd' pwd]
                        (if (empty? pwd')
                          sizes
                          (recur
                           (update sizes (pts pwd') #(+ filesize %))
                           (rest pwd'))))]))))
         ['() {}]
         (s/parse-lines input))]
    dir-sizes))

(defn part1 [input]
  (let [sizes (directory-sizes input)]
    (->> sizes
         (filter #(> 100000 (last %)))
         (map last)
         (reduce +))))

(defn part2 [input]
  (let [sizes (directory-sizes input)
        used-space (sizes "/")
        unused-space (- 70000000 used-space)
        deletion-size (- 30000000 unused-space)
        ]
    (->> sizes
         (sort-by val)
         (drop-while #(> deletion-size (val %)))
         first
         val)))
