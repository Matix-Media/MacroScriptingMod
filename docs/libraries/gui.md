---
tags:
    - Lib
---

# GUI

`require("gui")`

The GUI library handles GUIs like chests and menus.

## Functions

### `get_current_gui()`

Gets the currently open GUI.

Returns the name of the GUI.

```lua title="example.lua"
print(gui.get_current_gui())
```

### `open(name)`

**Arguments:**

-   `name`: the name of the GUI to open.

Opens the specified GUI.

```lua title="example.lua"
gui.open("chat")
```

### `close_current_gui()`

Closes the currently open GUI.

```lua title="example.lua"
gui.close_current_gui()
```

### `find_item(type)`

**Arguments:**

-   `type`: type of the item

Finds an item by the specified type.

Returns the slot the item is located in.

:::tip

If searching for a vanilla item, do not forget to put `minecraft:` before the item-name!

Slots are 0-based. Meaning that the first slot is 0, the second 1 and so on.

:::

:::caution

This method only looks in chest and other GUIs with items. It does not look in the player inventory. Even if the inventory is open.

:::

```lua title="example.lua"
print("Slot with dirt: " .. gui.find_item("minecraft:dirt"))
```

### `get_item(slot)`

**Arguments:**

-   `slot`: The slot of the item

Retrieves item information from a slot in the currently open GUI.

The returned object contains the item `type`, `amount`, `lore` and `name`.

:::tip

Slots are 0-based. Meaning that the first slot is 0, the second 1 and so on.

:::

```lua title="example.lua"
local item = gui.get_item(12)

print("Item type in slot 12: " .. item.type)
```
