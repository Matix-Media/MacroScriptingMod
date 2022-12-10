package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.Event;

public class EventDisconnect extends Event {
    private final String reason;

    public EventDisconnect(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
