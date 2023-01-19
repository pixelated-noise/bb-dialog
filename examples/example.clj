#!/usr/bin/env bb

(require '[babashka.process :as p :refer [shell]])
(require '[babashka.fs :refer [which]])
(require '[bb-dialog.core :refer :all])

;; comment out the various forms below at will

(comment
 (println
  (message "Important!"
           "Pandas are cute!")))

(comment
  (println
   (confirm "Important Query!"
            "Are cats soft?!")))

(comment
  (println
   (pause "Are you sure you want to do this?"
          "This adorable puppy will be sent\n to a farm!"
          10)))

(comment
  (println
   (input "CAT NAME"
          "WHAT IS CAT NAME TELL ME NOW")))

(comment
  (println
   (menu "PIE CHOOSER"
         "Which is best pie?"
         {:apple "Apple"
          :pumpkin "Pumpkin"
          :cheese "Cheese"})))

(comment
  (println
   (menu "Zip Code"
         "Select the correct zip code for your area"
         {60605 "Chicago"
          10005 "New York City"
          90028 "Hollywood"}
         :in-fn str
         :out-fn #(Integer/parseInt %))))

(comment
  (println
   (checklist "Options"
              "Choose options"
              [[:a "Option a" false]
               [:b "Option b" true]
               [:c "Option c" false]])))

(println
 (radiolist "Options"
            "Choose options"
            [[:a "Option a" false]
             [:b "Option b" true]
             [:c "Option c" true]]))
