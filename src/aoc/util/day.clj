(ns aoc.util.day
  (:require [clojure.java.io :as io]))

(defn day-input
  [year day]
  (slurp (io/resource (format "%s/day%02d.txt" year day))))

