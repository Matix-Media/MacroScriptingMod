package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;


public class LibTime extends Lib {
    public LibTime() {
        super("time");
    }

    @AutoLibFunction
    public static class Sleep extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            double seconds = arg.checkdouble();

            try {
                Thread.sleep((long) (seconds * 1000));
            } catch (InterruptedException ignored) {}

            return null;
        }
    }
}
