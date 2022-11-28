package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandRunning extends Command {

    private final Runtime runtime;

    public CommandRunning(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean execute(String[] args) {
        Chat.sendClientSystemMessage("Running scripts:");
        for (RunningScript runningScript : this.runtime.getRunningScripts()) {
            if (runningScript.getScript().getFile() != null) {
                Chat.sendClientSystemMessage(" - " + runningScript.getScript().getFile().getName());
            } else {
                Chat.sendClientSystemMessage(" - " + runningScript.getUuid());
            }
        }

        return true;
    }

    @Override
    public @NotNull String getCommand() {
        return "running";
    }

    @Override
    public @Nullable String getHelp() {
        return null;
    }
}
