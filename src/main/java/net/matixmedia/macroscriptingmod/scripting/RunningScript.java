package net.matixmedia.macroscriptingmod.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.Date;
import java.util.UUID;

public class RunningScript {
    private final Script script;
    private GlobalsHolder globalsHolder;
    private LuaValue chunk;
    private final Date startedAt;
    private final UUID uuid;
    private final Thread thread;
    private final InterruptDebugger interruptDebugger;
    private final Runtime runtime;

    public RunningScript(Script script, Thread thread, InterruptDebugger interruptDebugger, Runtime runtime) {
        this.script = script;

        this.startedAt = new Date();
        this.uuid = UUID.randomUUID();
        this.thread = thread;
        this.interruptDebugger = interruptDebugger;
        this.runtime = runtime;
    }

    public void stop() {
        this.interruptDebugger.interrupt();
        this.runtime.removeSandbox(this);
    }

    public void setGlobalsHolder(GlobalsHolder globals) {
        this.globalsHolder = globals;
    }

    public void setChunk(LuaValue chunk) {
        this.chunk = chunk;
    }



    public Script getScript() {
        return script;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Globals getGlobals() {
        return this.globalsHolder.getGlobals();
    }

    public GlobalsHolder getGlobalsHolder() {
        return globalsHolder;
    }

    public Thread getThread() {
        return thread;
    }

    public LuaValue getChunk() {
        return chunk;
    }

    public boolean isForceStopped() {
        return this.interruptDebugger.isInterrupted();
    }
}
