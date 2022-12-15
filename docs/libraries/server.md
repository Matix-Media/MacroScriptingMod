---
tags:
    - Lib
---

# Server

`require("server")`

The server library contains functions related to interactions with the server.

## Functions

### `reconnect()`

Reconnects to the last connected server.

:::info

You need to be disconnected in order to use this function.

:::

```lua title="example.lua"
server.reconnect()
```

### `get_online_players()`

Returns a list of currently connected players.

The returned list contains the player's `name` and `UUID`.

```lua title="example.lua"
local players = server.get_online_players()

for _, player in pairs(players) do
    print("Player " .. player.name .. " is online")
end
```
