#! /usr/bin/env bb

(require '[bb-dialog.core :refer :all])

(def game-state (atom {:name "Default"
                       :class :hero
                       :items []}))

