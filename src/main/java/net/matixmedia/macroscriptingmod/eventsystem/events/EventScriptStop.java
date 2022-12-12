package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;

public class EventScriptStop extends CancellableEvent {
    private final RunningScript runningScript;

    public EventScriptStop(RunningScript runningScript) {
        this.runningScript = runningScript;
    }

    public RunningScript getRunningScript() {
        return runningScript;
    }
}
