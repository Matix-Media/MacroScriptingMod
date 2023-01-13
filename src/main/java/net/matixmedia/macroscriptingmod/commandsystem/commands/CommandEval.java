package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandEval extends Command {

    private final Runtime runtime;

    public CommandEval(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean execute(String[] args, boolean silent) {
        if (args.length < 1) return false;
        String code = String.join(" ", args);

        if (!silent) Chat.sendClientSystemMessage("Evaluating lua code...");
        try {
            this.runtime.execute(new Script(code));
        } catch (RuntimeException e) {
            Chat.sendClientSystemMessage(Chat.Color.RED + "Error evaluating lua code: " + e.getMessage());
        }

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
