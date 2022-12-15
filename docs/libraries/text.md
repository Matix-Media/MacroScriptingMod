---
tags:
    - Lib
---

# Text

`require("text")`

The text library holds functions related to text operations.

## Functions

### `trim(text)`

**Arguments:**

-   `text`: the text to be trimmed

Trims the provided text on both ends.

Returns the trimmed text.

```lua title="example.lua"
local my_text = "   trim me please       "
local trimmed_text = text.trim(my_text)

print(trimmed_text)
--- will print: "trim me please"
```

### `strip_color_codes(text)`

**Arguments:**

-   `text`: the text to be stripped

Strips all color codes from the provided text.

Returns the stripped text.

```lua title="example.lua"
local colored_text = "§cText with §3color"
local stripped_text = text.strip_color_codes(colored_text)

print(stripped_text)
--- will print: "Text with color"
```
