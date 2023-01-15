---
tags:
  - Lib
---

# Script

`require("script")`

The scripts library provides functions to interact with other scripts.

## Functions

### `run(script)`

**Arguments:**

- `script`: the name of the script to be run

Runs a script in a new context.

Returns the ID of the started script.

```lua title="example.lua"
local script_id = script.run("my_script.lua")
```

### `stop(id)`

**Arguments:**

- `id`: the ID of the script to be stopped

Stops a running script.

```lua title="example.lua"
local script_id = script.run("my_script.lua")

script.stop(script_id)
```

### `get_running_scripts()`

Returns a map of all currently running scripts.

The map contains the IDs as the key and the corresponding file name as the value.

```lua title="example.lua"
local running_scripts = script.get_running_scripts()

for id, file in pairs(running_scripts) do
    print(id .. ": " .. file)
end
```
