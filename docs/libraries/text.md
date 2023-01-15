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

- `text`: the text to be trimmed

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

- `text`: the text to be stripped

Strips all color codes from the provided text.

Returns the stripped text.

```lua title="example.lua"
local colored_text = "§cText with §3color"
local stripped_text = text.strip_color_codes(colored_text)

print(stripped_text)
--- will print: "Text with color"
```

### `split(text, seperator)`

**Arguments:**

- `text`: the text to be split
- `seperator`: the seperator, where to split the text.

Splits the given text into list and returns that list.

```lua title="example.lua""
local split_text = text.split("Hello World", " ")
for i, part of ipairs(split_text) do
    print(part)
end
```

### `random_uuid()`

Returns a random UUID.

```lua title="example.lua"
print(text.random_uuid())
```
