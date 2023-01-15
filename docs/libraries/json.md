---
tags:
  - Lib
---

# JSON

`require("json")`

The JSON library contains functions related to handling JSON objects.  
[Source](https://github.com/rxi/json.lua)

## Functions

### `encode(value)`

**Arguments:**

- `value`: The table you'd to convert to JSON.

Returns a string representing `value` encoded in JSON.

```lua title="example.lua"
json.encode({ 1, 2, 3, { x = 10 } })
-- Returns '[1,2,3,{"x":10}]'
```

### `decode(str)`

**Arguments:**

- `str`: The JSON you'd like to decode.

Returns a value representing the decoded JSON string.

```lua title="example.lua"
json.decode('[1,2,3,{"x":10}]')
-- Returns { 1, 2, 3, { x = 10 } }
```
