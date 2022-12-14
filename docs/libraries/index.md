---
sidebar_position: 1
---

# Libraries

Libraries are either user defined scripts or interfaces provided by the MacroScriptingMod to interact with the game.

A library can be imported using

```lua
require("library_name")
```

## Provided libraries

To import a provided library, simply use the name of the library like listed but in lowercase, e.g: [Player](./player) -> `require("player")`.

## User defined libraries

If you want to import a user defined library, simply use the name of the script without the `.lua` extension.  
More information around this topic can be found [here](https://www.lua.org/pil/8.1.html).
