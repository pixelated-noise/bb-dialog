#! /usr/bin/env bb

(require '[bb-dialog.core :refer :all]
         '[clojure.string :as str])

(def game-state (atom {:cname "Default"
                       :class :hero
                       :items []}))

(defn add-item! [item]
  (swap! game-state update :items conj item))

(message "Our story"
         "You are a little panda who lives in a little panda village
          in a little bamboo grove in the highlands. It is a scary
          time for the little village. The dark lord's armies have
          been sweeping the lands below, and the people fear that
          soon he will come for the little panda village too. You
          have been chosen to go forth and investigate.")

;; the code below does not account for the player hitting "cancel" in which case
;; we get nil values

(loop []
  (swap! game-state assoc :cname (input "What is your name, little one?"
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

  (when-not (confirm "Are you happy with these choices?"
                     (str/join "\n"
                               (let [{:keys [cname class items]} @game-state]
                                 [(str "Name: " cname)
                                  (str "Class: " (name class))
                                  (str "Items: " (str/join ", " (map name items)))])))
    (swap! game-state assoc :items [])
    (recur)))

(def choice1
  (menu "The adventure begins"
        (str "Alright, " (:cname @game-state) ", time to set off."
             " You make your way down the path through the grove and reach"
             " a fork in the road. Which way will you go?")
        {:W "West"
         :E "East"}))

(when (= choice1 :W)
  (message "You died!"
           (str "The path west from the grove leads down a wet and slippery slope, and you lose your footing "
                "and slide down the hill, only to find the bridge is out! The little panda tumbles to their doom "
                "in the river below, never to be seen again."))
  (System/exit 0))
