package net.matixmedia.macroscriptingmod.commandsystem;

import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventChatMessage;
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

        List<String> args = List.of(event.getMessage().substring(silent ? 3 : 1).split(" "));

        Command executableCommand = null;
        for (Command command : this.registeredCommands) {
            if (!command.getCommand().equalsIgnoreCase(args.get(0))) continue;
            executableCommand = command;
        }

        if (executableCommand == null) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Unknown command.");
            return;
        }

        String[] passedArgs;
        if (args.size() > 1) passedArgs = args.subList(1, args.size()).toArray(new String[args.size() - 2]);
        else passedArgs = new String[0];

        boolean result = executableCommand.execute(passedArgs, silent);
        if (!result) Chat.sendClientSystemMessage("Help: " + Chat.Color.AQUA + "." +
                executableCommand.getCommand() + (executableCommand.getHelp() != null ? " " + executableCommand.getHelp() : ""));
    }
}
