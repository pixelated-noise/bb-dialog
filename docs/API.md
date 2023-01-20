# Table of contents
-  [`bb-dialog.core`](#bb-dialog.core) 
    -  [`*dialog-command*`](#bb-dialog.core/*dialog-command*) - A var which attempts to contain the correct version of <code>dialog</code> for the system.
    -  [`checklist`](#bb-dialog.core/checklist) - Calls a <code>--checklist</code> dialog, and returns the selected options as a seq of options.
    -  [`confirm`](#bb-dialog.core/confirm) - Calls a confirmation dialog (<code>dialog --yesno</code>), and returns a boolean depending on whether the user agreed.
    -  [`dialog`](#bb-dialog.core/dialog) - The base function wrapper for calling out to the system's version of <code>dialog</code>.
    -  [`input`](#bb-dialog.core/input) - Calls an <code>--inputbox</code> dialog, and returns the user input as a string.
    -  [`menu`](#bb-dialog.core/menu) - Calls a <code>--menu</code> dialog, and returns the selected option as a keyword.
    -  [`message`](#bb-dialog.core/message) - Calls a message dialog (<code>dialog --msgbox</code>), which simply presents some text that can be clicked past with OK or the enter key.
    -  [`pause`](#bb-dialog.core/pause) - Calls a confirmation dialog with a timeout (<code>dialog --pause</code>).
    -  [`radiolist`](#bb-dialog.core/radiolist) - Calls a <code>--radiolist</code> dialog, and returns the selected option as a keyword.

-----
# <a name="bb-dialog.core">bb-dialog.core</a>






## <a name="bb-dialog.core/*dialog-command*">`*dialog-command*`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L6-L13)
<a name="bb-dialog.core/*dialog-command*"></a>

A var which attempts to contain the correct version of [`dialog`](#bb-dialog.core/dialog) for the system. Given that this could potentially fail,
   and can't necessarily foresee all possibilities, the var is dynamic to allow rebinding by the end user.

## <a name="bb-dialog.core/checklist">`checklist`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L110-L129)
<a name="bb-dialog.core/checklist"></a>
``` clojure

(checklist title body choices & {:keys [in-fn out-fn], :or {in-fn name, out-fn keyword}})
```


Calls a `--checklist` dialog, and returns the selected options as a seq of options.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `choices`: A list of options. Each item in the list should be a vector of 3 elements: the choice value itself, a string description,
     and a boolean indicating whether the option is toggled or not.
     By default, the values are assumed to be keywords, and the function returns a seq of keywords, but you can customize this behavior with
     optional keyword arguments:
   - `:in-fn`: a function that will be applied to convert each key to a string for use by [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)
   - `:out-fn`: a function that will be applied to each string option selected and returned by [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog), to convert it back into a
     Clojure value

   Returns: seq of keywords (or results of `out-fn`), or nil if the user selects cancel or selects no choices

## <a name="bb-dialog.core/confirm">`confirm`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L49-L58)
<a name="bb-dialog.core/confirm"></a>
``` clojure

(confirm title body)
```


Calls a confirmation dialog (`dialog --yesno`), and returns a boolean depending on whether the user agreed.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: boolean

## <a name="bb-dialog.core/dialog">`dialog`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L15-L34)
<a name="bb-dialog.core/dialog"></a>
``` clojure

(dialog type title body & args)
```


The base function wrapper for calling out to the system's version of [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog).

   Args:
   - `type`: A string containing the CLI option for the type of dialog to display (see `man dialog`)
   - `title`: A string containing the title text for the dialog
   - `body`: A string containing the body text for the dialog
   - `args`: Any additional CLI arguments will be `apply`'d to the `shell` call; this allows for adding additional CLI arguments to dialog

   Returns:
   A process map as per [`babashka.process`](https://github.com/babashka/process/blob/master/API.md#process-). Of useful note are the `:exit`
   and `:err` keys, which will contain the return values from the call to [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog).

## <a name="bb-dialog.core/input">`input`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L73-L84)
<a name="bb-dialog.core/input"></a>
``` clojure

(input title body)
```


Calls an `--inputbox` dialog, and returns the user input as a string.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: string, or nil if the user selects cancel

## <a name="bb-dialog.core/menu">`menu`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L86-L108)
<a name="bb-dialog.core/menu"></a>
``` clojure

(menu title body choices & {:keys [in-fn out-fn], :or {in-fn name, out-fn keyword}})
```


Calls a `--menu` dialog, and returns the selected option as a keyword.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `choices`: A map of options to their descriptions.

   By default, `choices` is assumed to be a map of keywords to strings, and returns a keyword, but you can customize this behavior with
   optional keyword arguments:
   - `:in-fn`: a function that will be applied to convert each key to a string for use by [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)
   - `:out-fn`: a function that will be applied to the string option selected and returned by [[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog), to convert it back into a
     Clojure value

   Returns: keyword (or result of `out-fn`), or nil if the user selects cancel

## <a name="bb-dialog.core/message">`message`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L36-L47)
<a name="bb-dialog.core/message"></a>
``` clojure

(message title body)
```


Calls a message dialog (`dialog --msgbox`), which simply presents some text that can be clicked past with OK or the enter key.
   The message can be interrupted also with ESC, and so the return value is a boolean that indicates whether or not the prompt
   returned a zero exit code as from OK/enter.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog

   Returns: boolean

## <a name="bb-dialog.core/pause">`pause`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L60-L71)
<a name="bb-dialog.core/pause"></a>
``` clojure

(pause title body timeout)
```


Calls a confirmation dialog with a timeout (`dialog --pause`). Unless interrupted by the user selecting cancel or hitting ESC,
   the dialog will automatically end with a `true` result after `timeout` seconds.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `timeout`: The number of seconds the dialog should wait before automatically exiting.

   Returns: boolean

## <a name="bb-dialog.core/radiolist">`radiolist`</a> [📃](https://github.com/pixelated-noise/bb-dialog/blob/main/src/bb_dialog/core.clj#L131-L154)
<a name="bb-dialog.core/radiolist"></a>
``` clojure

(radiolist title body choices & {:keys [in-fn out-fn], :or {in-fn name, out-fn keyword}})
```


Calls a `--radiolist` dialog, and returns the selected option as a keyword.

   Args:
   - `title`: The title text of the dialog
   - `body`: The body text of the dialog
   - `choices`: A list of options. Each item in the list should be a vector of 3 elements: the choice value itself, a string description,
     and a boolean indicating whether the option is toggled or not. Note that since only one choice can be selected at the same time,
     [[[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)](#bb-dialog.core/dialog) will ignore the toggled state of all but the first toggled item in the list.

     By default, the values are assumed to be keywords, and the function returns a seq of keywords, but you can customize this behavior with
     optional keyword arguments:
   - `:in-fn`: a function that will be applied to convert each key to a string for use by [[[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)
   - `:out-fn`: a function that will be applied to each string option selected and returned by [[[`dialog`](#bb-dialog.core/dialog)](#bb-dialog.core/dialog)](#bb-dialog.core/dialog), to convert it back into a
     Clojure value

   Returns: keyword (or results of `out-fn`), or nil if the user selects cancel
