# bb-dialog

A simple wrapper library for working with [dialog](https://invisible-island.net/dialog/manpage/dialog.html) from Babashka.

This allows for a smoother way of adding simple TUI dialogs to your Babashka scripts on supported operating systems.

## Usage

For the time being, you should be able to employ bb-dialog as a git dependency in your `bb.edn`, and releases are tagged for ease of use:

```
{:deps {com.github.pixelated-noise/bb-dialog {:git/tag "v0.1"}}}
```

## API docs

See [quickdocs](docs/API.md).

## Options supported (current and planned)

- [x] Checklist (`--checklist`)
- [x] Confirmation (`--yesno`)
- [x] Basic text input (`--inputbox`)
- [x] Menu (`--menu`)
- [x] Message box (`--msgbox`)
- [x] Pause/timed dialog (`--pause`)
- [x] Radio list (`--radiolist`)
- [ ] Calendar (`--calendar`)
- [ ] Directory select (`--dselect`)
- [ ] File select (`--fselect`)
- [ ] Time picker (`--timebox`)
