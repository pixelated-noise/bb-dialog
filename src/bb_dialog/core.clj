(ns bb-dialog.core
  (:require [babashka.process :refer [shell]]
            [babashka.fs :refer [which]]
            [clojure.string :as str]))

(def ^:dynamic *dialog-command*
  "A var which attempts to contain the correct version of `dialog` for the
   system. Given that this could potentially fail, and can't necessarily foresee
   all possibilities, the var is dynamic to allow rebinding by the end user."
  (cond
    (which "dialog") "dialog"
    (which "whiptail") "whiptail"
    (which "Xdialog") "Xdialog"
    :else nil))

(defn command
  "The base function wrapper for calling out to the system's version of `dialog`.

   Args:

   - `type`: A string containing the CLI option for the type of dialog to
     display (see `man dialog`)
   - `title`: A string containing the title text for the dialog
   - `body`: A string containing the body text for the dialog
   - `args`: Any additional CLI arguments will be `apply`'d to the `shell` call;
     this allows for adding additional CLI arguments to dialog

   Returns:

   A process map as per [`babashka.process`](https://github.com/babashka/process/blob/master/API.md#process-).
   Of useful note are the `:exit` and `:err` keys, which will contain the return
   values from the call to `dialog`."

  [type title body & args]
  (if-let [diag *dialog-command*]
    (apply shell
           {:continue true
            :err :string}
           diag "--clear" "--title" title type body 0 0
           args)
    (throw (Exception. "bb-dialog was unable to locate a working version of dialog! Please install it in the PATH."))))

(defn message
  "Calls a message dialog (`dialog --msgbox`), which simply presents some text
   that can be clicked past with OK or the enter key.  The message can be
   interrupted also with ESC, and so the return value is a boolean that indicates
   whether or not the prompt returned a zero exit code as from OK/enter.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: boolean"
  [title body]
  (-> (command "--msgbox" title body) :exit zero?))

(defn confirm
  "Calls a confirmation dialog (`dialog --yesno`), and returns a boolean
   depending on whether the user agreed.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: boolean"
  [title body]
  (-> (command "--yesno" title body) :exit zero?))

(defn pause
  "Calls a confirmation dialog with a timeout (`dialog --pause`). Unless
   interrupted by the user selecting cancel or hitting ESC, the dialog will
   automatically end with a `true` result after `timeout` seconds.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `timeout`: The number of seconds the dialog should wait before
     automatically exiting.

   Returns: boolean"
  [title body timeout]
  (-> (command "--pause" title body timeout) :exit zero?))

(defn input
  "Calls an `--inputbox` dialog, and returns the user input as a string.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: string, or nil if the user selects cancel"
  [title body]
  (-> (command "--inputbox" title body)
      :err
      not-empty))

(defn calendar
  "Calls an `--calendar` dialog, and returns the user selected date as a string.

  Args:
  - `title`: The title text of the dialog
  - `body`: The body text of the dialog
  - `day`: The starting day of the calendar
  - `month`: The starting month of the calendar
  - `year`: The starting year of the calendar

  Returns: string (dd/mm/yyyy), or nil if the user selected cancel"
  ([title body]
   (-> (command "--calendar" title body)
       :err
       not-empty))
  ([title body day month year]
   (-> (command "--calendar" title body day month year)
       :err
       not-empty)))

(defn menu
  "Calls a `--menu` dialog, and returns the selected option as a keyword.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `choices`: A map of options to their descriptions.

   By default, `choices` is assumed to be a map of keywords to strings, and
   returns a keyword, but you can customize this behavior with optional keyword
   arguments:

   - `:in-fn`: a function that will be applied to convert each key to a string
   for use by `dialog`
   - `:out-fn`: a function that will be applied to the string option selected
   and returned by `dialog`, to convert it back into a Clojure value

   Returns: keyword (or result of `out-fn`), or nil if the user selects cancel."
  [title body choices & {:keys [in-fn out-fn] :or {in-fn name out-fn keyword}}]
  (some->> choices
           (mapcat (fn [[k v]] [(in-fn k) (str v)]))
           (apply command "--menu" title body
                  (count choices))
           :err
           not-empty
           out-fn))

(defn checklist
  "Calls a `--checklist` dialog, and returns the selected options as a seq of
   options.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `choices`: A list of options. Each item in the list should be a vector of 3
     elements: the choice value itself, a string description, and a boolean
     indicating whether the option is toggled or not.

   By default, the values are assumed to be keywords, and the function returns
   a seq of keywords, but you can customize this behavior with optional keyword
   arguments:

   - `:in-fn`: a function that will be applied to convert each key to a string
     for use by `dialog`
   - `:out-fn`: a function that will be applied to each string option selected
     and returned by `dialog`, to convert it back into a Clojure value

   Returns: seq of keywords (or results of `out-fn`), or nil if the user selects
   cancel or selects no choices."
  [title body choices & {:keys [in-fn out-fn] :or {in-fn name out-fn keyword}}]
  (let [as-list (mapcat (fn [[k d s]] [(in-fn k) d (if s "ON" "off")]) choices)
        result  (apply command "--checklist" title body (count choices) as-list)]
    (when-let [err (not-empty (:err result))]
      (map out-fn (str/split err #" ")))))

(defn radiolist
  "Calls a `--radiolist` dialog, and returns the selected option as a keyword.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   - `choices`: A list of options. Each item in the list should be a vector of 3
      elements: the choice value itself, a string description, and a boolean
      indicating whether the option is toggled or not. Note that since only one
      choice can be selected at the same time, `dialog` will ignore the toggled
      state of all but the first toggled item in the list.

   By default, the values are assumed to be keywords, and the function returns a
   seq of keywords, but you can customize this behavior with optional keyword
   arguments:
   - `:in-fn`: a function that will be applied to convert each key to a string
     for use by `dialog`
   - `:out-fn`: a function that will be applied to each string option selected
     and returned by `dialog`, to convert it back into a Clojure value

   Returns: keyword (or results of `out-fn`), or nil if the user selects cancel."
  [title body choices & {:keys [in-fn out-fn] :or {in-fn name out-fn keyword}}]
  (let [as-list (mapcat (fn [[k d s]] [(in-fn k) d (if s "ON" "off")]) choices)
        result  (apply command "--radiolist" title body (count choices) as-list)]
    (some-> result
            :err
            not-empty
            out-fn)))

;; treeview

(defn- tv-parse-tree [depth tree]
  (let [[[tag item status] children] (split-with (complement vector?) tree)]
    {:tag      tag
     :item     item
     :status   (or status :off)
     :depth    depth
     ;; don't increment depth for tagless nodes, containing sibling subtrees
     :children (map (partial tv-parse-tree (+ depth (if tag 1 0))) children)}))

(defn- tv-marshal-tree [{:keys [tag item status depth children]} in-fn]
  (cond->> (mapcat #(tv-marshal-tree % in-fn) children)
    tag (concat [(in-fn tag) item (name status) (str depth)])))

(defn- tv-flatten-tree [tree in-fn] (tv-marshal-tree (tv-parse-tree 0 tree) in-fn))

(defn treeview
  "Calls a `--treeview` dialog, and returns the selected option as a keyword.

   Args:
   - `body`: The text shown in the dialog
   - `tree`: A structure of nested vectors describing the available options
   - `:in-fn` and `:out-fn` see below

   Returns: keyword (or results of `out-fn`), or nil if the user selects cancel.

   The tree should look like so:

   ```
   [:a \"alpha\"
    [:b \"beta\"]
    [:c \"gamma\" :on
     [:c1 \"gamma1\"]
     [:c2 \"gamma2\"]]
    [:d \"delta\" :off
     [:d1 \"delta1\"]
     [:d2 \"delta2\"]
     [:d3 \"delta3\"]]]
   ```

   The `:on` keyword defines which option is preselected - only the first
   `:on` has any effect. The `:on`/`:off` keywords are optional (`:off` is
   implied if absent).

   By default, the tags of the nodes of the tree are assumed to be keywords, and
   the function returns the selected keyword, but you can customize this behavior
   with optional keyword arguments:

   - `:in-fn`: a function that will be applied to convert each tag to a string
      for use by `dialog`
   - `:out-fn`: a function that will be applied to the selected string option
      returned by `dialog`, to convert it back into a Clojure value

   Here's an example of how to use integers as tags:

   ```
   (treeview
    \"Pick one\"
    [[1 \"alpha\"
      [11 \"beta\"]
      [12 \"gamma\" :on
       [121 \"gamma1\"]
       [122 \"gamma2\"]]
      [13 \"delta\"
       [131 \"delta1\"]
       [132 \"delta2\"]
       [133 \"delta3\"]]]]
    :in-fn str
    :out-fn #(Integer/parseInt %))
   ```"
  [body tree & {:keys [in-fn out-fn] :or {in-fn name out-fn keyword}}]
  (prn (tv-flatten-tree tree in-fn) in-fn out-fn)
  (some-> (apply command "--treeview" nil body (cons 0 (tv-flatten-tree tree in-fn))) :err not-empty out-fn))
