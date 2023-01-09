#! /usr/bin/env bb

(require '[bb-dialog.core :refer :all])

(def game-state (atom {:name "Default"
                       :class :hero
                       :items []}))

(defn add-item! [item]
  (swap! game-state update :items conj item))

(message "The adventure begins"
         "You are a little panda who lives in a little panda village
          in a little bamboo grove in the highlands. It is a scary
          time for the little village. The dark lord's armies have 
          been sweeping the lands below, and the people fear that
          soon he will come for the little panda village too. You
          have been chosen to go forth and investigate.")

(swap! game-state assoc :name (input "What is your name, little one?"
                                     "Enter your name below."))

(swap! game-state assoc :class (radiolist "What is your class?"
                                          "What has your panda trained as? 
                                           Choose below."
                                          [[:WAR "Warrior" true]
                                           [:MGE "Mage" false]
                                           [:ROG "Rogue" false]]))

(case (:class @game-state)
  :WAR (add-item! :sword)
  :MGE (add-item! :staff)
  :ROG (add-item! :knife))

(add-item! :potion)

