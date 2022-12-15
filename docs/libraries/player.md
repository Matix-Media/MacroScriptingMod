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

### `get_location()`

Returns general the location of the current player.

The returned object contains the players exact `x`, `y` and `z` position.

```lua title="example.lua"
local player_location = player.get_location()

print("Player is at: " .. player_location.x .. "/" .. player_location.y .. "/" .. player_location.z)
```

### `get_gamemode()`

Returns the current player's gamemode.

The returned object returns the gamemode's `id` and `name`.

`id` and their correlating `name` can be one of the following values  
-`0`: `survival`  
-`1`: `creative`  
-`2`: `adventure`  
-`3`: `spectator`

```lua title="example.lua"
local player_gamemode = player.get_gamemode()

print("Player is in gamemode: " .. player_gamemode.name .. " with id: " .. player_gamemode.id)
```

### `get_health()`

Returns the current player's health.

```lua title="example.lua"
print("Player is at: " .. player.get_health() .. " health.")
```

### `get_hunger()`

Returns the current player's hunger.

```lua title="example.lua"
print("Player is at: " .. player.get_hunger() .. " hunger.")
```

### `get_xp_level()`

Returns the current player's experience level.

```lua title="example.lua"
print("Player is at level: " .. player.get_xp_level())
```

### `get_xp()`

Returns the current player's experience.  
Resets after each experience level reached.

:::info

This is not the number you see above your hotbar.

:::

```lua title="example.lua"
print("Player is at level: " .. player.get_xp_level())
```

### `get_total_xp()`

Returns the current player's total experience.

:::info

This is not the number you see above your hotbar.

:::

```lua title="example.lua"
print("Player is at level: " .. player.get_xp_level())
```
