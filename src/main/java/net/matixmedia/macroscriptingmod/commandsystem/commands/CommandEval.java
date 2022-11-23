package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandEval extends Command {
    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        String code = String.join(" ", args);

        Chat.sendClientSystemMessage("Evaluating lua code...");
        Chat.sendClientSystemMessage(code);

        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "eval";
    }

    @Override
    public @Nullable String getHelp() {
        return "<code>";
    }
}
