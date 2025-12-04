(ns aoc.core
  (:require [clojure.string :as str])
  (:import (java.io StringWriter)))

(defn- parse-arg [arg]
  (let [[_ year day] (re-find #"(\d+)(?:\.(\d+))?" arg)]
    [(when year (read-string year))
     (when day (read-string day))]))

(defn day-var [year day-num var-name]
  (let [ns (format "aoc.%d.day%02d" year day-num)]
    (require (symbol ns))
    (find-var (symbol (format "%s/%s" ns var-name)))))

(defn day-val [year day-num var-name]
  (when-let [var (day-var year day-num var-name)]
    (var-get var)))

(defn result [answer time]
  (let [[secs ms] [(quot time 1000) (rem time 1000)]
        time-desc (if (zero? secs)
                    (str ms "ms")
                    (str secs "s " ms "ms <TOO SLOW>"))]
    (println (format "%s, %s" answer time-desc))))

;; From https://stackoverflow.com/questions/62724497/how-can-i-record-time-for-function-call-in-clojure
(defmacro time-execution
  [& body]
  `(let [s# (StringWriter.)]
     (binding [*out* s#]
       {:return (time ~@body)
        :time   (int (read-string (str/replace (str s#) #"[^0-9\\.]" "")))})))

(defn execute [year day-num]
  (let [input   (or (day-val year day-num "input") "")
        part1   (day-var year day-num "part1")
        part2   (day-var year day-num "part2")]
    (println (format "%d Day %d: %s" year, day-num name))
    (let [p1 (time-execution (part1 input))]
      (print "  part 1: ")
      (result (:return p1) (:time p1)))
    (let [p2 (time-execution (part2 input))]
      (print "  part 2: ")
      (result (:return p2) (:time p2)))
    (println)))

(defn -main [& args]
  (let [specs (map parse-arg args)]
    (doseq [[year day] specs]
      (execute year day))))
