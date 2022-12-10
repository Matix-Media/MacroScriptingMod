package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.commandsystem.CommandManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandHelp extends Command {
    private final CommandManager commandManager;
    private final String prefix;

    public CommandHelp(CommandManager commandManager, String prefix) {
        this.commandManager = commandManager;
        this.prefix = prefix;
    }

    @Override
    public boolean execute(String[] args) {
        StringBuilder message = new StringBuilder("§7Commands:");

        for (Command command : commandManager.getRegisteredCommands()) {
            message.append("\n").append(prefix).append("§7 - §b.").append(command.getCommand());
            if (command.getHelp() != null) message.append(" §7- ").append(command.getHelp());
        }
        message.append("§r");

        Chat.sendClientSystemMessage(message.toString());

        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "help";
    }

    @Override
    public @Nullable String getHelp() {
        return null;
    }
}
