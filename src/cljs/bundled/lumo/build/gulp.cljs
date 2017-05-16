(ns lumo.build.gulp
  (:require [lumo.closure :as closure]
            [lumo.util :as util]
            [cljs.analyzer :as ana]
            [cljs.env :as env]
            [lumo.io :as io]))

(def through2 (js/require "through2"))

(def File (js/require "vinyl"))

(defn compile-to-stream [source opts compiler-env]
  (let [file-list (atom [])
        js-string (atom "")]
    (through2
     ;; Options
     #js {"objectMode" true}
     ;; _transform function
     (fn [file enc callback]
       (println file)
       (this-as this
         (cond
           (or (.isNull file) (.isDirectory file)) (.push this file)
           (.isStream file) (println "Lumo gulp build error: Streams are not yet supported.")
           (.isBuffer file) (swap! file-list conj file)
           :else nil)
         (callback)))
     ;; _flush function
     (fn [callback]
       (this-as this
         (println "File-list: " @file-list)
         (callback))))))

(defn build
  ([source opts]
   (build source opts
          (env/default-compiler-env
           (closure/add-externs-sources opts))))
  ([source opts compiler-env]
   (doseq [[unknown-opt suggested-opt] (util/unknown-opts (set (keys opts)) closure/known-opts)]
     (when suggested-opt
       (println (str "WARNING: Unknown compiler option '" unknown-opt "'. Did you mean '" suggested-opt "'?"))))
   (binding [ana/*cljs-warning-handlers* (:warning-handlers opts ana/*cljs-warning-handlers*)]
     (compile-to-stream source opts compiler-env))))

(defn debug [] (println "mahhildur1"))
