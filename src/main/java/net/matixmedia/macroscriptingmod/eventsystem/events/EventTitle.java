package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.CancellableEvent;
import net.matixmedia.macroscriptingmod.eventsystem.ModifiableEvent;

public class EventTitle extends CancellableEvent implements ModifiableEvent {

    private final String originalContent;
    private String content;
    private String subtitle;
    private final TitleType titleType;

    public EventTitle(String content, TitleType titleType) {
        this.content = content;
        this.originalContent = content;
        this.titleType = titleType;
    }

    public String getContent() {
        return this.content;
    }

    public TitleType getTitleType() {
        return this.titleType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean isModified() {
        return !this.originalContent.equals(this.content);
    }

    public enum TitleType {
        TITLE,
        SUBTITLE,
        ACTIONBAR
    }
}
