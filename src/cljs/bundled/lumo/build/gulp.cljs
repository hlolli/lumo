(ns lumo.build.gulp
  (:require [lumo.closure :as closure]
            [lumo.util :as util]
            [cljs.analyzer :as ana]
            [cljs.env :as env]
            [lumo.io :as io]))

(def through (js/require "through2"))

(def vinyl (js/require "vinyl"))

(defn compile-to-stream [source opts compiler-env]
  (.obj through
        (fn [file enc callback]
          (this-as this
            (cond
              (or (.isNull file) (.isDirectory file)) (do (.push this file)
                                                          (callback))
              (.isStream file) (do (println "Lumo gulp build error: Streams are not yet supported.")
                                   (callback))
              (.isBuffer file)                
              :else (callback))))))

(defn build
  "Given a source which can be compiled, produce runnable JavaScript."
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

