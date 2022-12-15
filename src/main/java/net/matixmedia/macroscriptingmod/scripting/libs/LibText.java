package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.luaj.vm2.LuaValue;

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
    public static class StripColorCodes extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String input = arg.checkjstring();
            return LuaValue.valueOf(Chat.Color.stripColor(input));
        }
    }
}
