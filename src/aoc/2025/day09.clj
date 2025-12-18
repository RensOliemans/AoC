(ns aoc.2025.day09
  (:require [aoc.util.day :as d]
            [aoc.util.string :as s]))

(def input (d/day-input 2025 9))

(defn- pairs [xs]
  (for [i (range (count xs))
        j (range i)]
    [(nth xs i) (nth xs j)]))

(defn- size [[x y] [x' y']]
  (* (inc (abs (- x x')))
     (inc (abs (- y y')))))

(defn- largest-square [points]
  (reduce
   (fn [m [a b]]
     (max m (size a b)))
   0
   (pairs points)))

(defn part1 [input]
  (->> input
       s/parse-lines
       (mapv s/parse-ints)
       largest-square))

(defn- outliers [points]
  (reduce
   (fn [[outliers [x y :as prev]] [x' y' :as next]]
     (cond
       (< 10 (/ x x')) [(conj outliers prev) next]
       (< 10 (/ x' x)) [(conj outliers next) next]
       :else [outliers next]))
   [[] (first points)]
   (rest points)))

(defn- y-limit [points x-cutoff]
  (->> points
       (take-while #(> (first %) x-cutoff))
       last
       last))

(defn- max-size [anchor points]
  (->> points
       (map #(size % anchor))
       (reduce max)))

(defn part2
  "For this puzzle we require some knowledge about our input. Put the
  points in desmos' polygon() function and see what I mean.

  We split our circle in two parts, north and south. For each
  half-circle we discard the input of the other half and find the
  biggest circle, with one caveat: the y-limit must be not too far
  from our north and south anchors. Again, look at the polygon."
  [input]
  (let [points (->> input
                    s/parse-lines
                    (mapv s/parse-ints))
        [outliers _] (outliers points)

        north-point (apply max-key last outliers)
        south-point (apply min-key last outliers)

        nx (first north-point)
        sx (first south-point)

        ny-limit (y-limit points nx)
        sy-limit (y-limit (rseq points) sx)

        north-half (filter #(<= (last north-point) (last %) ny-limit) points)
        south-half (filter #(>= (last south-point) (last %) sy-limit) points)]
    (max (max-size north-point north-half)
         (max-size south-point south-half))))
