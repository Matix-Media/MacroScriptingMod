package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.eventsystem.Event;
import net.matixmedia.macroscriptingmod.eventsystem.ModifiableEvent;

public class EventChatMessage {

    public static class Receive extends CancellableEvent implements ModifiableEvent {
        private String message;
        private final String originalMessage;

        public Receive(String message) {
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

    public static class Send extends CancellableEvent implements ModifiableEvent {
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
