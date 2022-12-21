---
tags:
  - Lib
---

# Player

`require("player")`

The player library contains all kinds of functions related to the player.

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

### `get_oxygen()`

Returns the current player's oxygen.

```lua title="example.lua"
print(player.get_oxygen())
```

### `is_flying()`

Returns true if the current player is flying.

```lua title="example.lua"
local player_is_flying = player.is_flying()

if player_is_flying then
  print("Player is flying.")
else
  print("Player is not flying.")
end
```

### `can_fly()`

Returns true if the current player is able to fly.

```lua title="example.lua"
local player_can_flying = player.can_fly()

if player_can_flying then
  print("Press double space button to start flying!")
else
  print("You can not fly.")
end
```

### `get_distance_to(x, y, z)`

**Arguments:**

- `x`: X coordinate of block
- `y`: Y coordinate of block
- `z`: Z coordinate of block

Returns the distance between the current player's eyes and the specified location.

```lua title="example.lua"
print(player.get_distance_to(0,0,0))
-- Returns the distance between the current player's eyes and 0/0/0
```

### `find_item(type)`

Scans through the current player's inventory to find the first slot, containing the specified item.

Returns `NIL` if the argument is not valid or the item is not found in inventory.

:::tip

If searching for a vanilla item, do not forget to put `minecraft:` before the item-name!

Slots are 0-based. Meaning that the first slot is 0, the second 1 and so on.

:::

```lua title="example.lua"
print(player.find_item("minecraft:wheat"))
```

### `get_item(slot)`

Returns the item, placed in the specified slot of the current player's inventory.

The returned object contains the item's `type`, `amount`, `lore` and `name`.

:::tip

Slots are 0-based. Meaning that the first slot is 0, the second 1 and so on.

:::

```lua title="example.lua"
local item = player.get_item(10)
-- Gets the item in slot 10 of the current player's inventory.

print(item.name)
-- Returns for example "Wheat"
print(item.type)
-- Returns for example "minecraft:wheat"
print(item.lore)
-- Returns the item lore as a string
print(item.amount)
```

### `select_hotbar_slot()`

Selects the specified slot of the current player's hotbar.

:::tip

Slots are 0-based. Meaning that the first slot is 0, the second 1 and so on.

:::

```lua title="example.lua"
player.select_hotbar_slot(4)
```

### `get_yaw()`

Returns the current player's HeadYaw.

```lua title="example.lua"
print(player.get_yaw())
```

### `get_pitch()`

Returns the current player's pitch.

```lua title="example.lua"
print(player.get_pitch())
```

### `look(yaw, pitch)`

**Arguments:**

- `yaw`: the yaw, the player should look at. Should be between -180 and 180.
- `pitch`: the pitch, the player should look at. Should be between -90 and 90.

Sets the player's HeadYaw and pitch to the specified values.

```lua title="example.lua"
player.look(45,7)
```

### `look_smooth(yaw, pitch, seconds)`

**Arguments:**

- `yaw`: the yaw, the player should look at. Should be between -180 and 180.
- `pitch`: the pitch, the player should look at. Should be between -90 and 90.
- `seconds`: the time it should take to smooth look from current HeadYaw and pitch to the specified ones.

Smoothly turns the player's HeadYaw and pitch to the specified values.

```lua title="example.lua"
player.look_smooth(45,7)
```

### `look_relative(yawIncrease, pitchIncrease)`

**Arguments:**

- `yaw`: the increase in the player's yaw wanted. Can be a negative number.
- `pitch`: the increase in the player's pitch wanted. Can be a negative number.

Increases the player's HeadYaw and pitch by the specified values.

```lua title="example.lua"
player.look_relative(45,7)
```

### `look_smooth_relative(yawIncrease, pitchIncrease, seconds)`

**Arguments:**

- `yaw`: the increase in the player's yaw wanted. Can be a negative number.
- `pitch`: the increase in the player's pitch wanted. Can be a negative number.
- `seconds`: the time it should take to smooth look from current HeadYaw and pitch to the new ones.

Smoothly increases the player's HeadYaw and pitch by the specified values.

```lua title="example.lua"
player.look_smooth_relative(45,7)
```

### `get_scoreboard_title()`

Returns the scoreboard's title.

```lua title="example.lua"
print(player.get_scoreboard_title())
```

### `get_scoreboard()`

Returns the scoreboard as a string.

:::info

all `§` are replaced by `&`.

:::

```lua title="example.lua"
print(player.get_scoreboard())
```

### `respawn()`

Respawns the player if their currently dead.

```lua title="example.lua"
player.respawn()
```
