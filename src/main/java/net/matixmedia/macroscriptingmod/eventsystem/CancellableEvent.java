package net.matixmedia.macroscriptingmod.eventsystem;

public abstract class CancellableEvent extends Event{
    private boolean canceled;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCanceled() {
        return this.canceled;
    }
}
