package net.matixmedia.macroscriptingmod.commandsystem;

import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventChatMessage;
import net.matixmedia.macroscriptingmod.utils.CLIParser;
import net.matixmedia.macroscriptingmod.utils.Chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager implements EventListener {
    private final List<Command> registeredCommands = new ArrayList<>();

    public CommandManager() {
        EventManager.registerListener(this);
    }

    public void registerCommand(Command command) {
        registeredCommands.add(command);
    }

    public Collection<Command> getRegisteredCommands() {
        return new ArrayList<>(this.registeredCommands);
    }

    @EventHandler
    public void onMessage(EventChatMessage.Send event) {
        if (!event.getMessage().startsWith(".")) return;
        event.cancel();

        boolean silent = event.getMessage().startsWith("...");

        String[] commandAndArgs = event.getMessage().substring(silent ? 3 : 1).split(" ", 2);
        if (commandAndArgs.length < 1) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Invalid command.");
            return;
        }
        String commandName = commandAndArgs[0];
        String rawArgs = commandAndArgs.length > 1 ? commandAndArgs[1] : "";
        List<String> args;
        try {
            args = new CLIParser(rawArgs).getTokens();
        } catch (Exception e) {
            Chat.sendClientSystemMessage(Chat.Color.RED + e.getMessage());
            return;
        }

        Command executableCommand = null;
        for (Command command : this.registeredCommands) {
            if (!command.getCommand().equalsIgnoreCase(commandName)) continue;
            executableCommand = command;
        }

        if (executableCommand == null) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Unknown command.");
            return;
        }

        boolean result = executableCommand.execute(
                executableCommand.acceptsUnparsedArguments() ? new String[] {rawArgs} : args.toArray(new String[0]), silent);

        if (!result) Chat.sendClientSystemMessage(Chat.Color.RED + "Help: " + Chat.Color.AQUA + "." +
                executableCommand.getCommand() + (executableCommand.getHelp() != null ? " " + executableCommand.getHelp() : ""));
    }
}
