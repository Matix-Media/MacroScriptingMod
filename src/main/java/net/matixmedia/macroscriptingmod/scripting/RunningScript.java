package net.matixmedia.macroscriptingmod.scripting;

import org.luaj.vm2.Globals;

import java.util.Date;
import java.util.UUID;

public class RunningScript {
    private final Script script;
    private Globals globals;
    private final Date startedAt;
    private final UUID uuid;

    public RunningScript(Script script) {
        this.script = script;

        this.startedAt = new Date();
        this.uuid = UUID.randomUUID();
    }

    public void setGlobals(Globals globals) {
        this.globals = globals;
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
}
