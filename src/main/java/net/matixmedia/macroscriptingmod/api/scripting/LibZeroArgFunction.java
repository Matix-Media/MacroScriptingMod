package net.matixmedia.macroscriptingmod.api.scripting;

import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public abstract class LibZeroArgFunction extends LibArgFunction {
    abstract public LuaValue call();

    /** Default constructor */
    public LibZeroArgFunction() {
    }

    public LuaValue call(LuaValue arg) {
        return call();
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return call();
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        return call();
    }

    public Varargs invoke(Varargs varargs) {
        return call();
    }
}
