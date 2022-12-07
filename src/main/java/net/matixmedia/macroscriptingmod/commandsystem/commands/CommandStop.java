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

        boolean stoppedScripts = false;
        for (RunningScript runningScript : this.runtime.getRunningScriptsByNameOrId(scriptName)) {
                runningScript.stop();
                Chat.sendClientSystemMessage("Stopped \"" + scriptName + "\"");
                stoppedScripts = true;
        }
        if (!stoppedScripts) Chat.sendClientSystemMessage(Chat.Color.RED + "Script not found");
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
