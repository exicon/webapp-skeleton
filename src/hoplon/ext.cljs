(ns hoplon.ext
  (:require
    [hoplon.core :refer [p]])
  (:require-macros
    [hoplon.core :refer [defelem]]
    [hoplon.ext :refer [macro-test]]))

(defelem example [attrs kids]
  (p attrs "Example: " kids))
