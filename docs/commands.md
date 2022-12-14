---
sidebar_position: 3
---

# Commands

Commands are always prefixed with a `.`. This differentiates vanilla commands from MacroScriptingMod commands and prevents confusion.

In order to run a command, simply send `.<command>` in chat.

## Available commands

### `.run <script name>`

**Arguments:**

-   `<script name>`: The name of the script you want to execute

The run command allows you to run a script which is located in the `scripts` directory.

```
.run my_script.lua
```

### `.stop <script name/ID>`

**Arguments:**

-   `<script name/ID>`: The name or ID of a running script

Stops all running scripts with the specified name or ID.

```
.stop my_script.lua

.stop bcc7ac99-9730-43c9-82a5-f7723c220fc6
```

### `.running`

Lists all running scripts.

### `eval <code>`

-   `<code>`: Lua code to be evaluated

Evaluates Lua code without a script file.

```
.eval print("Hello, World")
```

### `.help`

Lists all available commands.
