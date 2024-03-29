package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.*;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.*;
import net.matixmedia.macroscriptingmod.exceptions.ScriptInterruptedException;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.ArrayList;
import java.util.List;

public class LibEvents extends Lib implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Lib/Events");

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
    public void onScriptStop(EventScriptStop.Pre event) {
        if (event.getRunningScript() == this.getRunningScript() && (this.useFunctionHandlers || this.listeners.size() > 0))
            event.cancel();
    }

    @EventHandler
    public void onScriptFinallyStop(EventScriptStop.Post event) {
        this.callEvent("on_script_end", new LuaValue[] {LuaValue.valueOf(event.getRunningScript().getUuid().toString())});
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
    public void onTitle(EventTitle event) {
        boolean cancelled = this.callEvent("on_" + event.getTitleType().name().toLowerCase(), new LuaValue[] {LuaValue.valueOf(event.getContent())});
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

    @EventHandler
    public void onScriptCustomEvent(EventScriptCustomEvent event) {
        this.callEvent(event.getEvent(), event.getArgs());
    }

    public boolean callEvent(String event, LuaValue[] args) {
        boolean cancelEvent = false;
        try {
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
        } catch (Exception e) {
            if (e instanceof LuaError luaError) {
                if (!(luaError.getCause() instanceof ScriptInterruptedException)) {
                    Chat.sendClientSystemMessage(Chat.Color.RED + "Error calling event listener:\n" + e.getMessage());
                    LOGGER.error("Error calling event listener:");
                    LOGGER.error(e.getMessage());
                }
            } else throw e;
        }
        return cancelEvent;
    }

    @AutoLibFunction
    public static class UseFunctionHandlers extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            LibEvents instance = (LibEvents) getInstance(LibEvents.class, this.getRunningScript());
            if (instance == null) return null;

            instance.useFunctionHandlers = true;
            return null;
        }
    }

    @AutoLibFunction
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

    @AutoLibFunction
    public static class Send extends LibArgFunction {
        @Override
        public LuaValue call() {
            invoke();
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg) {
            invoke(new LuaValue[]{arg});
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            invoke(new LuaValue[]{arg1, arg2});
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            invoke(new LuaValue[]{arg1, arg2, arg3});
            return null;
        }

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
            invoke(new LuaValue[]{arg1, arg2, arg3, arg4});
            return null;
        }

        @Override
        public Varargs invoke(Varargs args) {
            if (args.narg() == 0) return argerror("Please specify an event name");

            String eventName = args.arg1().checkjstring();
            List<LuaValue> eventArguments = new ArrayList<>();

            for (int i = 2; i < args.narg() + 1; i++) {
                eventArguments.add(args.arg(i));
            }

            EventManager.fire(new EventScriptCustomEvent(eventName, eventArguments.toArray(new LuaValue[0])));
            return null;
        }
    }
}
