package net.matixmedia.macroscriptingmod.api.scripting;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;

public abstract class LibTwoArgFunction extends LibArgFunction {
    abstract public LuaValue call(LuaValue arg1, LuaValue arg2);

    /** Default constructor */
    public LibTwoArgFunction() {
    }

    public final LuaValue call() {
        return call(NIL, NIL);
    }

    public final LuaValue call(LuaValue arg) {
        return call(arg, NIL);
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
        return call(arg1, arg2);
    }

    public Varargs invoke(Varargs varargs) {
        return call(varargs.arg1(),varargs.arg(2));
    }
}
