#!/usr/bin/env bb

(require '[babashka.process :as p :refer [shell]])
(require '[babashka.fs :refer [which]])
(require '[bb-dialog.core :refer :all])

#_(message "Important!"
         "Pandas are cute!")

#_(confirm "Important Query!"
         "Are cats soft?!")

#_(pause "Are you sure you want to do this?"
       "This adorable puppy will be sent\n to a farm!"
       10)

#_(input "CAT NAME"
       "WHAT IS CAT NAME TELL ME NOW")

#_(menu "PIE CHOOSER"
      "Which is best pie?"
      {:apple "Apple"
       :pumpkin "Pumpkin"
       :cheese "Cheese"})

#_(menu "Zip Code"
      "Select the correct zip code for your area"
      {60605 "Chicago"
       10005 "New York City"
       90028 "Hollywood"}
      :in-fn str
      :out-fn #(Integer/parseInt %))

(checklist "Options"
           "Choose options"
           [[:a "Option a" false]
            [:b "Option b" true]
            [:c "Option c" false]])