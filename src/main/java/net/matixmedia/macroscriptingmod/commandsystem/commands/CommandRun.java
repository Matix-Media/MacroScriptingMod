package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaError;

import java.io.IOException;

public class CommandRun extends Command {
    private final Runtime runtime;
    private final ScriptManager scriptManager;

    public CommandRun(Runtime runtime, ScriptManager scriptManager) {
        this.runtime = runtime;
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean execute(String[] args) {
        String scriptName = String.join(" ", args);

        Script script = this.scriptManager.loadScript(scriptName);
        if (script == null) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Script not found");
            return true;
        }
        Chat.sendClientSystemMessage("Running \"" + scriptName + "\"...");
        try {
            this.runtime.execute(script);
        } catch (Exception e) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Error executing lua script: " + e.getMessage());
        }
        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "run";
    }

    @Override
    public @Nullable String getHelp() {
        return "<script name>";
    }
}
