# bb-dialog

A simple wrapper library for working with [dialog](https://invisible-island.net/dialog/manpage/dialog.html) from Babashka.

This allows for a smoother way of adding simple TUI dialogs to your Babashka scripts on supported operating systems.

## Requirements

You will need some version of either `dialog`, `whiptail`, or `Xdialog` installed. `dialog` or `whiptail` may already be installed on most
Linux systems, however on OS X you will need to install `dialog` from brew. There is also a Windows port of `dialog` [here](https://andrear.altervista.org/home/cdialog.php).

## Usage

For the time being, you should be able to employ bb-dialog as a git dependency in your `bb.edn`, and releases are tagged for ease of use:

```
{:deps {com.github.pixelated-noise/bb-dialog {:git/tag "v0.2"
                                              :git/sha "66cd35803ce17c3c224348c408efe38a2acde949}}}
```

## API docs

See [quickdocs](docs/API.md).

Also see [the announcement blog post](https://www.pixelated-noise.com/blog/2023/01/20/bb-dialog-announcement/index.html).

## Options supported (current and planned)

- [x] Checklist (`--checklist`)
- [x] Confirmation (`--yesno`)
- [x] Basic text input (`--inputbox`)
- [x] Menu (`--menu`)
- [x] Message box (`--msgbox`)
- [x] Pause/timed dialog (`--pause`)
- [x] Radio list (`--radiolist`)
- [x] Tree view (`--treeview`)
- [ ] Calendar (`--calendar`)
- [ ] Directory select (`--dselect`)
- [ ] File select (`--fselect`)
- [ ] Time picker (`--timebox`)

## Legal

Copyright (c) 2023 Pixelated Noise Ltd

Distributed under the Eclipse Public License version 2.0 or later.
