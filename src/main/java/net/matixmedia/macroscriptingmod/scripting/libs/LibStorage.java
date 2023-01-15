package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibTwoArgFunction;
import net.matixmedia.macroscriptingmod.scripting.helpers.StorageManager;
import org.luaj.vm2.LuaValue;

import java.io.IOException;

public class LibStorage extends Lib {

    public LibStorage() {
        super("storage");
    }

    @AutoLibFunction
    public static class Save extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            StorageManager storageManager = MacroScriptingMod.getInstance().getStorageManager();

            String key = arg1.checkjstring();
            try {
                switch (arg2.typename()) {
                    case "nil" -> storageManager.save(key, null);
                    case "boolean" -> storageManager.save(key, arg2.toboolean());
                    case "number" -> storageManager.save(key, arg2.toint());
                    case "string" -> storageManager.save(key, arg2.tojstring());
                    default -> {
                        return argerror(2, "Only nil, boolean, number and string are allowed to be stored");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    @AutoLibFunction
    public static class Get extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            StorageManager storageManager = MacroScriptingMod.getInstance().getStorageManager();

            String key = arg.checkjstring();
            if (!storageManager.exists(key)) return NIL;

            Object value = storageManager.get(key);
            System.out.println("Type: " + (value == null ? "null" : value.getClass().getSimpleName()));
            if (value == null) return NIL;
            else if (value instanceof Boolean b) return LuaValue.valueOf(b);
            else if (value instanceof Integer i) return LuaValue.valueOf(i);
            else if (value instanceof Double d) return LuaValue.valueOf(d);
            else if (value instanceof String s) return LuaValue.valueOf(s);

            return NIL;
        }
    }

    @AutoLibFunction
    public static class Exists extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            StorageManager storageManager = MacroScriptingMod.getInstance().getStorageManager();

            String key = arg.checkjstring();
            return LuaValue.valueOf(storageManager.exists(key));
        }
    }

    @AutoLibFunction
    public static class Delete extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            StorageManager storageManager = MacroScriptingMod.getInstance().getStorageManager();

            String key = arg.checkjstring();
            try {
                storageManager.delete(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
