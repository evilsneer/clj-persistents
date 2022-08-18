(ns persistents.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def exists (memfn exists))

(defn <-disk [name]
  (if (exists (io/file name))
    (-> name
      slurp
      edn/read-string)))

(defn ->disk [name item]
  (binding [*print-dup* true]
    (locking name
     (io/make-parents name)
     (->> item
       pr-str
       (spit name)))))

(defn hdd-synced-atom
  ([filename]
   (hdd-synced-atom filename (<-disk filename)))
  ([filename initial]
   (hdd-synced-atom filename initial false))
  ([filename initial replace?]
   (let [actual-initial (if replace?
                          initial
                          (or (<-disk filename) initial))
         -a             (atom actual-initial)]
     (->disk filename actual-initial)
     (add-watch -a (keyword (str filename "-atom-watcher"))
       (fn [key atom old-state new-state]
         (->disk filename new-state))))))