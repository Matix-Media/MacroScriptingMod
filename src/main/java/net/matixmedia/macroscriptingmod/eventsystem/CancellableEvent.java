package net.matixmedia.macroscriptingmod.eventsystem;

public abstract class CancellableEvent extends Event{
    private boolean cancelled;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}
