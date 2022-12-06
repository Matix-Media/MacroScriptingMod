package net.matixmedia.macroscriptingmod.commandsystem.commands;

import net.matixmedia.macroscriptingmod.commandsystem.Command;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class CommandStop extends Command {
    private final Runtime runtime;

    public CommandStop(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean execute(String[] args) {

        String scriptName = String.join(" ", args);

        for (RunningScript runningScript : this.runtime.getRunningScripts()) {
            if (runningScript.getScript().getFile() == null) continue;

            if (runningScript.getScript().getFile().getName().equals(scriptName)) {
                runningScript.getThread().stop();
                Chat.sendClientSystemMessage("Stopped \"" + scriptName + "\"");
                return true;
            }
        }
        Chat.sendClientSystemMessage(Chat.Color.RED + "Script not found");
        return true;
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
