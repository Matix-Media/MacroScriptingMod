package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibTwoArgFunction;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class LibEvents extends Lib {

    private final List<EventListener> listeners = new ArrayList<>();

    public LibEvents() {
        super("events");
    }

    private record EventListener(String event, LuaValue handler) {}

    public static class Register extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            LibEvents instance = (LibEvents) getInstance(LibEvents.class, this.getRunningScript());
            if (instance == null) return null;

            String eventName = arg1.checkjstring();
            LuaValue handler = arg2.checkfunction();

            instance.listeners.add(new EventListener(eventName, handler));
            return null;
        }
    }
}
