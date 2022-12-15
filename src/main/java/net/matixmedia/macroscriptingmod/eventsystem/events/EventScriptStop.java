package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;

public class EventScriptStop extends CancellableEvent {
    private final RunningScript runningScript;

    private EventScriptStop(RunningScript runningScript) {
        this.runningScript = runningScript;
    }

    public RunningScript getRunningScript() {
        return runningScript;
    }

    public static class Pre extends EventScriptStop {
        public Pre(RunningScript runningScript) {
            super(runningScript);
        }
    }

    public static class Post extends EventScriptStop {
        public Post(RunningScript runningScript) {
            super(runningScript);
        }
    }


}
