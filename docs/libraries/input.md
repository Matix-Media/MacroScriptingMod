---
tags:
    - Lib
---

# Input

`require("input")`

The input functions can be used to simulate keyboard and mouse inputs.

## Mapped Keys

Mapped keys are retrieved from the player settings. This makes them useable even if the player changes the key in the settings.

-   `forward`: The forward key (default: `w`)
-   `back`: The back key (default: `s`)
-   `left`: The left key (default: `a`)
-   `right`: The right key (default: `d`)
-   `jump`: The jump key (default: `space`)
-   `sneak`: The sneak key (default: `sneak`)
-   `player_list`: The player list key (default: `tab`)
-   `sprint`: The sprint key (default: `control`)
-   `attack`: The attack key (default: `mouse left click`)
-   `use`: The use/interact key (default: `mouse right click`)

## Functions

### `key_down(key)`

**Arguments:**

-   `key`: key to be pressed down. Can either be [keycode](https://www.glfw.org/docs/3.3/group__keys.html), key as string or a [mapped key](#mapped-keys)

Presses down the specified key.

```lua title="example.lua"
input.key_down("forward")
input.key_down("w")
input.key_down(87)
```

### `key_up(key)`

**Arguments:**

-   `key`: key to be released. Can either be [keycode](https://www.glfw.org/docs/3.3/group__keys.html), key as string or a [mapped key](#mapped-keys)

Releases the specified key.

```lua title="example.lua"
input.key_up("forward")
input.key_up("w")
input.key_up(87)
```

### `press_key(key)`

**Arguments:**

-   `key`: key to be press. Can either be [keycode](https://www.glfw.org/docs/3.3/group__keys.html), key as string or a [mapped key](#mapped-keys)

Presses a key down for a short time.

```lua title="example.lua"
input.press_key("forward")
input.press_key("w")
input.press_key(87)
```
