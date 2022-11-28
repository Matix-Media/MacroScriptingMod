package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.Event;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;

public class EventScriptEnd extends Event {
    private final RunningScript runningScript;

    public EventScriptEnd(RunningScript runningScript) {
        this.runningScript = runningScript;
    }

    public RunningScript getRunningScript() {
        return this.runningScript;
    }
}
