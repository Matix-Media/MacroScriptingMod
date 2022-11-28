package net.matixmedia.macroscriptingmod.api.scripting;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

public abstract class LibThreeArgFunction extends LibFunction {
    abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3);

    /** Default constructor */
    public LibThreeArgFunction() {
    }

    public final LuaValue call() {
        return call(NIL, NIL, NIL);
    }

    public final LuaValue call(LuaValue arg) {
        return call(arg, NIL, NIL);
    }

    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        return call(arg1, arg2, NIL);
    }

    public Varargs invoke(Varargs varargs) {
        return call(varargs.arg1(),varargs.arg(2),varargs.arg(3));
    }
}
