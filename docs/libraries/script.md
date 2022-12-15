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

-   `script`: the name of the script to be run

Runs a script in a new context.

Returns the ID of the started script.

```lua title="example.lua"
local script_id = script.run("my_script.lua")
```

### `stop(id)`

**Arguments:**

-   `id`: the ID of the script to be stopped

Stops a running script.

```lua title="example.lua"
local script_id = script.run("my_script.lua")

script.stop(script_id)
```

### `get_running_scripts()`

Returns a list of all currently running scripts.

The list contains the IDs of all the running scripts.

```lua title="example.lua"
local running_scripts = script.get_running_scripts("")

for _, script in pairs(running_scripts) do
    print("Script with ID " .. script .. " is running")
end
```
