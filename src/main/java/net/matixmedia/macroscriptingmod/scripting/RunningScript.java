package net.matixmedia.macroscriptingmod.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.Date;
import java.util.UUID;

public class RunningScript {
    private final Script script;
    private Globals globals;
    private LuaValue chunk;
    private final Date startedAt;
    private final UUID uuid;
    private final Thread thread;

    public RunningScript(Script script, Thread thread) {
        this.script = script;

        this.startedAt = new Date();
        this.uuid = UUID.randomUUID();
        this.thread = thread;
    }

    public void setGlobals(Globals globals) {
        this.globals = globals;
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
        return globals;
    }

    public Thread getThread() {
        return thread;
    }

    public LuaValue getChunk() {
        return chunk;
    }
}
