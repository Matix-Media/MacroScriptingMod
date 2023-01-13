package net.matixmedia.macroscriptingmod.commandsystem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command {
    public abstract boolean execute(String[] args, boolean silent);

    @NotNull
    public abstract String getCommand();

    @Nullable
    public abstract String getHelp();
}
