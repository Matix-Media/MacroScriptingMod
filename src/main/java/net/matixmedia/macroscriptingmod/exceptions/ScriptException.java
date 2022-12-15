package net.matixmedia.macroscriptingmod.exceptions;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

public class ScriptException extends LuaError {
    public ScriptException(Throwable cause) {
        super(cause);
    }

    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, int level) {
        super(message, level);
    }

    public ScriptException(LuaValue message_object) {
        super(message_object);
    }
}
