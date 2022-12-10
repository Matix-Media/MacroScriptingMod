package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibTwoArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.*;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class LibEvents extends Lib implements EventListener {

    private final List<EventListener> listeners = new ArrayList<>();
    private boolean useFunctionHandlers = false;

    public LibEvents() {
        super("events");
    }

    @Override
    public void init() {
        EventManager.registerListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        EventManager.unregisterListener(this);
    }

    private record EventListener(String event, LuaValue handler) {
        public EventListener(String event, LuaValue handler) {
            this.event = event.toLowerCase();
            this.handler = handler;
        }
    }

    @EventHandler
    public void onChatSend(EventChatMessage.Send event) {
        boolean cancelled = this.callEvent("on_chat_send", new LuaValue[] {LuaValue.valueOf(event.getMessage())});
        if (cancelled) event.cancel();
    }

    @EventHandler
    public void onChatReceive(EventChatMessage.Receive event) {
        boolean cancelled = this.callEvent("on_chat_receive", new LuaValue[] {LuaValue.valueOf(event.getMessage())});
        if (cancelled) event.cancel();
    }

    @EventHandler
    public void onConnectToServer(EventConnectToServer event) {
        boolean cancelled = this.callEvent("on_connect", new LuaValue[] {LuaValue.valueOf(event.getServerInfo().address)});
        if (cancelled) event.cancel();
    }

    @EventHandler
    public void onDisconnect(EventDisconnect event) {
        this.callEvent("on_disconnect", new LuaValue[] {LuaValue.valueOf(event.getReason())});
    }

    @EventHandler
    public void onTick(EventTick event) {
        this.callEvent("on_tick", new LuaValue[0]);
    }

    private boolean callEvent(String event, LuaValue[] args) {
        boolean cancelEvent = false;

        for (EventListener listener : this.listeners) {
            if (!listener.event.equals(event.toLowerCase())) continue;

            LuaValue result = listener.handler.invoke(args).arg1();
            if (result.isboolean() && !result.checkboolean()) cancelEvent = true;
        }

        if (this.useFunctionHandlers) {
            LuaValue functionHandler = this.getRunningScript().getGlobals().get(event);
            if (!functionHandler.isfunction()) return cancelEvent;
            LuaValue result = functionHandler.invoke(args).arg1();
            if (result.isboolean() && !result.checkboolean()) cancelEvent = true;
        }

        return cancelEvent;
    }

    public static class UseFunctionHandlers extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            LibEvents instance = (LibEvents) getInstance(LibEvents.class, this.getRunningScript());
            if (instance == null) return null;

            instance.useFunctionHandlers = true;
            return null;
        }
    }

    public static class Register extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            LibEvents instance = (LibEvents) getInstance(LibEvents.class, this.getRunningScript());
            if (instance == null) return null;

            String eventName = arg1.checkjstring();
            LuaValue handler = arg2.checkfunction();

            instance.listeners.add(new LibEvents.EventListener(eventName, handler));
            return null;
        }
    }
}
