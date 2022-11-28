package net.matixmedia.macroscriptingmod.api.scripting;

import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;

public abstract class LibOneArgFunction extends LibArgFunction {
    abstract public LuaValue call(LuaValue arg);

    /** Default constructor */
    public LibOneArgFunction() {
    }

    public final LuaValue call() {
        return call(NIL);
    }

    public final LuaValue call(LuaValue arg1, LuaValue arg2) {
        return call(arg1);
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        return call(arg1);
    }

    public Varargs invoke(Varargs varargs) {
        return call(varargs.arg1());
    }
}
