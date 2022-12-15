---
tags:
    - Lib
---

# World

`require("world")`

The world library contains all kind of functions related to the world and the entities in it.

## Functions

### `get_block(x, y, z)`

**Arguments:**

-   `x`: X coordinate of block
-   `y`: Y coordinate of block
-   `z`: Z coordinate of block

Returns information about the block specified.

The returned object contains the block `x`, `y`, `z` and `type`.

```lua title="example.lua"
local block = world.get_block(0, -64, 0)

print("Block at " .. block.x .. "/" .. block.y .. "/" .. block.z .. " is of type: " .. block.type)
```

### `get_block_relative(x, y, z)`

**Arguments:**

-   `x`: relative X coordinate of block
-   `y`: relative Y coordinate of block
-   `z`: relative Z coordinate of block

Returns information about the block specified relative to the player.

The returned object contains the block `x`, `y`, `z` and `type`.

```lua title="example.lua"
local block = world.get_block_relative(0, -1, 0)

print("Relative block at " .. block.x .. "/" .. block.y .. "/" .. block.z .. " is of type: " .. block.type)
```

### `get_nearby_entities(radius)`

**Arguments:**

-   `radius`: radius around the player

Returns a list of entities in the specified radius around the player.

The returned list contains the entities `ID`, `type`, `x`, `y`, `z`, `pitch`, `yaw`, `helmet`, `chest_plate`, `leggings` and `boots`.

```lua title="example.lua"
local entities = world.get_nearby_entities(5)

for _, entity in pairs(entities) do
    print("Nearby entity is of type: " .. entity.type)
end
```

### `get_entities()`

Returns a list of all loaded entities inside the world.

The returned list contains the entities `ID`, `type`, `x`, `y`, `z`, `pitch`, `yaw`, `helmet`, `chest_plate`, `leggings` and `boots`.

```lua title="example.lua"
local entities = world.get_entities()

for _, entity in pairs(entities) do
    print("Entity is of type: " .. entity.type)
end
```

### `show_esp_box(min_x, min_y, min_z, max_x, min_y, min_z)`

**Arguments:**

-   `min_x`: minimal X coordinate of the ESP box
-   `min_y`: minimal Y coordinate of the ESP box
-   `min_z`: minimal Z coordinate of the ESP box
-   `max_x`: maximal X coordinate of the ESP box
-   `max_y`: maximal Y coordinate of the ESP box
-   `max_z`: maximal Z coordinate of the ESP box

Shows an ESP box at the specified box location. The color of the ESP box will depend of the distance of the player to the box. A custom color can be shown by [specifying the color](#show_esp_boxmin_x-min_y-min_z-max_x-min_y-min_z-red-green-blue) after the box coordinates.

The returned value is the ID of the ESP box. It can be used to [remove the ESP box](#remove_esp_boxid) again.

```lua title="example.lua"
local esp_id = world.show_esp_box(0, -60, 0, 1, -61, 1)
```

### `show_esp_box(min_x, min_y, min_z, max_x, min_y, min_z, red, green, blue)`

**Arguments:**

-   `min_x`: minimal X coordinate of the ESP box
-   `min_y`: minimal Y coordinate of the ESP box
-   `min_z`: minimal Z coordinate of the ESP box
-   `max_x`: maximal X coordinate of the ESP box
-   `max_y`: maximal Y coordinate of the ESP box
-   `max_z`: maximal Z coordinate of the ESP box
-   `red`: red value of RGB for ESP box
-   `green`: green value of RGB for ESP box
-   `blue`: blue value of RGB for ESP box

Shows a colored ESP box at the specified box location.

The returned value is the ID of the ESP box. It can be used to [remove the ESP box](#remove_esp_boxid) again.

```lua title="example.lua"
local esp_id = world.show_esp_box(0, -60, 0, 1, -61, 1, 0, 0, 200)
```

### `remove_esp_box(id)`

**Arguments:**

-   `id`: ID of ESP box to be removed

Removes the specified ESP box.

```lua title="example.lua"
local esp_id = world.show_esp_box(0, -60, 0, 1, -61, 1)

world.remove_esp_box(esp_id)
```

### `show_tracer(x, y, z)`

**Arguments:**

-   `x`: X coordinate of the target of the tracer
-   `y`: Y coordinate of the target of the tracer
-   `z`: Z coordinate of the target of the tracer

Shows a tracer line pointer towards the target location.

The returned value is the ID of the tracer. It can be used to [remove the tracer](#remove_tracerid) again.

```lua title="example.lua"
local tracer_id = world.show_tracer(0, -60, 0)
```

### `show_tracer(x, y, z, red, green, blue)`

**Arguments:**

-   `x`: X coordinate of the target of the tracer
-   `y`: Y coordinate of the target of the tracer
-   `z`: Z coordinate of the target of the tracer
-   `red`: red value of RGB for the tracer
-   `green`: green value of RGB for the tracer
-   `blue`: blue value of RGB for the tracer

Shows a colored tracer line pointer towards the target location.

The returned value is the ID of the tracer. It can be used to [remove the tracer](#remove_tracerid) again.

```lua title="example.lua"
local tracer_id = world.show_tracer(0, -60, 0, 0, 0, 200)
```

### `remove_tracer(id)`

**Arguments:**

-   `id`: ID of tracer to be removed

Removes the specified tracer.

```lua title="example.lua"
local tracer_id = world.show_tracer(0, -60, 0)

world.remove_tracer(tracer_id)
```

### `break_block(x, y, z)`

**Arguments:**

-   `x`: X coordinate of block
-   `y`: Y coordinate of block
-   `z`: Z coordinate of block

Tries breaks the specified block using the block break animation.

:::caution

Only works if the block is within the players reach.

:::

```lua title="example.lua"
world.break_block(0, -61, 0)
```

### `calc_yaw_pitch_to(x, z)`

-   `x`: target X coordinate
-   `z`: target Z coordinate

Calculates the `yaw` and `pitch` to the target location.

The returned object contains the `pitch` and `yaw`.

```lua title="example.lua"
local direction = world.calc_yaw_pitch_to(1, 1)

print("Yaw: " .. direction.yaw .. " Pitch: " .. direction.pitch)
```
