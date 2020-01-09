(ns persistents.core
  (:require [clojure.java.io :as io]))

(def exists (memfn exists))

(defn <-disk [name]
  (if (exists (io/file name))
    (-> name
      slurp
      read-string)))

(defn ->disk [name item]
  (locking name
    (io/make-parents name)
    (->> item
     str
     (spit name))))

(defn hdd-synced-atom
  ([filename]
   (hdd-synced-atom filename (<-disk filename)))
  ([filename initial]
   (hdd-synced-atom filename initial false))
  ([filename initial replace?]
   (let [-a (atom (if replace?
                    initial
                    (or (<-disk filename) initial)))]
     (->disk filename initial)
     (add-watch -a (keyword (str filename "-atom-watcher"))
       (fn [key atom old-state new-state]
         (->disk filename new-state))))))