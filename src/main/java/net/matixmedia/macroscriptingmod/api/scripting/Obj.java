package net.matixmedia.macroscriptingmod.api.scripting;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public abstract class Obj {
    public LuaValue toLua() {
        return CoerceJavaToLua.coerce(this);
    }
}
