(ns hoplon.ext
  (:require
    [hoplon.core :as h]
    [javelin.core :as j]))

(defmacro macro-test [] 123)

(defmacro x [[bindings items] body]
  `(h/loop-tpl* (j/cell= ~items)
                (fn [item#] (j/cell-let [~bindings item#] ~body))))
