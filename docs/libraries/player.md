---
tags:
    - Lib
---

# Player

`require("player")`

The player libraries contains all kinds of functions related to the player.

## Functions

### `get_info()`

Returns general information about the current player.

The returned object contains the players `UUID` and `name`.

```lua title="example.lua"
local player_info = player.get_info()

print(player_info.name)
print(player_info.UUID)
```
