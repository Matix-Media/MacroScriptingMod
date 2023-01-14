package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import org.luaj.vm2.LuaValue;

public class EventScriptCustomEvent extends CancellableEvent {

    private final String event;
    private final LuaValue[] args;

    public EventScriptCustomEvent(String event, LuaValue[] args) {
        this.event = event;
        this.args = args;
    }

    public String getEvent() {
        return event;
    }

    public LuaValue[] getArgs() {
        return args;
    }
}
