package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;

public class CommandScriptsDir extends Command {

    private final ScriptManager scriptManager;

    public CommandScriptsDir(ScriptManager scriptManager) {
        this.scriptManager = scriptManager;
    }

    @Override
    public boolean execute(String[] args, boolean silent) {
        Chat.sendClientSystemMessage("Opening scripts directory...");
        try {
            Desktop.getDesktop().open(scriptManager.getScriptsDirectory().toFile());
        } catch (IOException e) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Error opening scripts directory: " + e.getMessage());
        }
        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "scriptsdir";
    }

    @Override
    public @Nullable String getHelp() {
        return null;
    }
}
