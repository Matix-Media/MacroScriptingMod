---
tags:
    - Lib
---

# Time

`require("time")`

The time library contains function related to the time.

## Functions

### `sleep(seconds)`

**Arguments:**

-   `seconds`: the amount of seconds

Delays the execution by the specified seconds.

:::info

if you want to wait for less than a second, you can use decimal values:  
`time.sleep(0.5)`

:::

```lua title="example.lua"
time.sleep(3)
print("This will get printed after 3 seconds")
```
