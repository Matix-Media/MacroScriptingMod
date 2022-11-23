package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.eventsystem.Event;

public class EventChatMessage {

    public static class Receive extends Event {}

    public static class Send extends CancellableEvent {
        private String message;
        private final String originalMessage;

        public Send(String message) {
            this.message = message;
            this.originalMessage = message;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean isModified() {
            return !this.originalMessage.equals(this.message);
        }
    }
}
