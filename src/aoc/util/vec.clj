(ns aoc.util.vec)

;; https://stackoverflow.com/questions/10347315/matrix-transposition-in-clojure
(defn transpose
  "Transpose the given matrix `m` on the diagonal"
  [m]
  (apply mapv vector m))

(defn invert-map-of-sets [m]
  (reduce (fn [a [k v]] (assoc a k (conj (get a k #{}) v))) {} (for [[k s] m v s] [v k])))

;; Cardinal and intercardinal direction vectors
;; Using grid coordinates where y increases downward
(def dir-nw [-1 -1])
(def dir-n  [+0 -1])
(def dir-ne [+1 -1])
(def dir-e  [+1 +0])
(def dir-se [+1 +1])
(def dir-s  [+0 +1])
(def dir-sw [-1 +1])
(def dir-w  [-1 +0])

(def dir-up    dir-n)
(def dir-down  dir-s)
(def dir-left  dir-w)
(def dir-right dir-e)

(def adjacent-dirs
  "All 8 direction vectors including diagonals (NW, N, NE, W, E, SW, S, SE)."
  [dir-nw dir-n dir-ne
   dir-w        dir-e
   dir-sw dir-s dir-se])

(def cardinal-dirs
  "The 4 cardinal direction vectors (up, right, down, left)."
  [dir-up dir-right dir-down dir-left])

(defn adjacent-to
  "Returns all 8 adjacent positions (including diagonals) for a given position.

  Example:
    (adjacent-to [5 5]) => [[4 4] [5 4] [6 4] [4 5] [6 5] [4 6] [5 6] [6 6]]"
  [[x y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x) y]                   [(inc x) y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn- tails [xs]
  (when (seq xs)
    (lazy-seq (cons xs (tails (rest xs))))))

(defn combinations-with-replacement [xs k]
  (if (zero? k)
    '(())
    (for [[h & t] (tails xs)
          more (combinations-with-replacement (cons h t) (dec k))]
      (cons h more))))
