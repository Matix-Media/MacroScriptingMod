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

### `disconnect()`

Disconnects the player from the current server.

```lua title="example.lua"
server.disconnect()
```

### `get_online_players()`

Returns a list of currently connected players.

The returned list contains the player's `name`, `display_name` and `uuid`.

```lua title="example.lua"
local players = server.get_online_players()

for _, player in pairs(players) do
    print("Player " .. player.name .. " is online")
end
```
