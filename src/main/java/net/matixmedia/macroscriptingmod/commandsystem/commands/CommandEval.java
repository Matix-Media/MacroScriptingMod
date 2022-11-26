package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.LuaError;

import java.io.IOException;

public class CommandEval extends Command {

    private Runtime runtime;

    public CommandEval(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length < 1) return false;
        String code = String.join(" ", args);

        Chat.sendClientSystemMessage("Evaluating lua code...");
        //Chat.sendClientSystemMessage(code);
        try {
            this.runtime.execute(new Script(code));
        } catch (IOException | RuntimeException e) {
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
