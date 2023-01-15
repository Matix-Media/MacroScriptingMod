---
tags:
  - Lib
---

# Settings

`require("settings")`

The settings library contains functions related to interacting with the players settings.

## Functions

### `set_perspective(perspective)`

**Arguments:**

- `perspective`: Can be either `"FIRST_PERSON"`, `"BACK"` or `"FRONT"`.

Sets the players camera perspective.

```lua title="example.lua"
settings.set_perspective("FRONT")
```

### `set_fov(fov)`

**Arguments:**

- `fov`: Can be between 30 and 110.

Sets the players FOV.

```lua title="example.lua"
settings.set_fov(90)
```

### `set_chat_visibility(visibility)`

**Arguments:**

- `visibility` Can be `"FULL"`, `"SYSTEM"` or `"HIDDEN"`.

Sets players Chat-Visibility.

```lua title="example.lua"
settings.set_chat_visibility("HIDDEN")
```
