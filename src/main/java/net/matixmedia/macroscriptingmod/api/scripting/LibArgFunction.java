package net.matixmedia.macroscriptingmod.api.scripting;

import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.lib.LibFunction;

public abstract class LibArgFunction extends LibFunction {
    private RunningScript runningScript;

    public void setRunningScript(RunningScript runningScript) {
        this.runningScript = runningScript;
    }

    public RunningScript getRunningScript() {
        return this.runningScript;
    }

    protected MinecraftClient getMinecraft() {
        return MinecraftClient.getInstance();
    }
}
