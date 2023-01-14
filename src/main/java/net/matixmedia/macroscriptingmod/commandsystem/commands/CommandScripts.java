package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class CommandScripts extends Command {

    private final ScriptManager scriptManager;

    public CommandScripts(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean execute(String[] args, boolean silent) {
        Chat.sendClientSystemMessage("Available scripts:");
        for (Script script : this.scriptManager.getAvailableScripts()) {
            if (script.getFile() != null) {
                Chat.sendClientSystemMessage(" - " + script.getScriptName());
            } else {
                try {
                    Chat.sendClientSystemMessage(" - " + script.getContent().substring(0, 20));
                } catch (IOException e) {
                    Chat.sendClientSystemMessage(" - <error while loading>");
                    throw new RuntimeException(e);
                }
            }
        }

        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "scripts";
    }

    @Override
    public @Nullable String getHelp() {
        return null;
    }
}
