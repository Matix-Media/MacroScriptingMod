package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class CommandStop extends Command {
    private final Runtime runtime;
    private final ScriptManager scriptManager;

    public CommandStop(Runtime runtime, ScriptManager scriptManager) {
        this.runtime = runtime;
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean execute(String[] args) {

        Chat.sendClientSystemMessage(Chat.Color.RED + "This currently not implemented");

        return true;
        /*
        String scriptName = String.join(" ", args);

        for (Script script : this.runtime.getRunningScripts()) {
            if (script.getFile() == null) continue;

            if (script.getFile().getName().equals(scriptName)) {

            }
        }

        Script script = this.scriptManager.loadScript(scriptName);
        if (script == null) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Script not found");
            return true;
        }
        try {
            this.runtime.execute(script);
        } catch (IOException | RuntimeException e) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Error executing lua script: " + e.getMessage());
        }
        return true;*/
    }

    @Override
    public @NotNull String getCommand() {
        return "stop";
    }

    @Override
    public @Nullable String getHelp() {
        return "<script name>";
    }
}
