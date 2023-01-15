package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.*;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LibText extends Lib {
    public LibText() {
        super("text");
    }

    @AutoLibFunction
    public static class Trim extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String input = arg.checkjstring();
            return LuaValue.valueOf(input.trim());
        }
    }

    @AutoLibFunction
    public static class Split extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            String value = arg1.checkjstring();
            String delimiter = arg2.checkjstring();
            List<LuaValue> parts = new ArrayList<>();
            for (String part : value.split(delimiter)) {
                parts.add(LuaValue.valueOf(part));
            }
            return LuaValue.listOf(parts.toArray(new LuaValue[0]));
        }
    }

    @AutoLibFunction
    public static class StripColorCodes extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String input = arg.checkjstring();
            return LuaValue.valueOf(Chat.Color.stripColor(input));
        }
    }

    @AutoLibFunction
    public static class RandomUuid extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(UUID.randomUUID().toString());
        }
    }
}
