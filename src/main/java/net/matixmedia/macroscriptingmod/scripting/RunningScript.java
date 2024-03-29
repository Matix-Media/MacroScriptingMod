package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventScriptStop;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.Date;
import java.util.UUID;

@SuppressWarnings("unused")
public class RunningScript {
    private final Script script;
    private final String[] arguments;
    private GlobalsHolder globalsHolder;
    private LuaValue chunk;
    private final Date startedAt;
    private final UUID uuid;
    private final Thread thread;
    private final InterruptDebugger interruptDebugger;
    private final Runtime runtime;

    public RunningScript(Script script, String[] arguments, Thread thread, InterruptDebugger interruptDebugger, Runtime runtime) {
        this.script = script;
        this.arguments = arguments;
        this.startedAt = new Date();
        this.uuid = UUID.randomUUID();
        this.thread = thread;
        this.interruptDebugger = interruptDebugger;
        this.runtime = runtime;
    }

    public void requestStop() {
        EventScriptStop.Pre event = new EventScriptStop.Pre(this);
        EventManager.fire(event);
        if (!event.isCancelled()) this.stop();
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

    public String[] getArguments() {
        return arguments;
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

    public Date getStartedAt() {
        return startedAt;
    }

    public boolean isForceStopped() {
        return this.interruptDebugger.isInterrupted();
    }
}
