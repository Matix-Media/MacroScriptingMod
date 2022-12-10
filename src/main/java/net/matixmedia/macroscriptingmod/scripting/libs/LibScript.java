package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.exceptions.ScriptException;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.ArrayList;
import java.util.List;

public class LibScript extends Lib {

    public LibScript() {
        super("script");
    }

    public static class Run extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();
            String scriptName = arg.checkjstring();

            Script script = mod.getScriptManager().loadScript(scriptName);
            if (script == null) {
                throw new ScriptException("Script not found");
            }
            Runtime.RunScriptResult result;
            try {
                result = mod.getRuntime().execute(script);
            } catch (Exception e) {
                throw new ScriptException("Error executing lua script: " + e.getMessage());
            }

            return LuaValue.valueOf(result.getRunningScript().getUuid().toString());
        }
    }

    public static class Stop extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();
            String scriptId = arg.checkjstring();

            for (RunningScript runningScript : mod.getRuntime().getRunningScripts()) {
                if (!runningScript.getUuid().toString().equals(scriptId)) continue;

                runningScript.stop();
                break;
            }
            return null;
        }
    }

    public static class GetRunningScripts extends LibArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();
            if (args.narg() == 0) {
                LuaTable runningScripts = LuaValue.tableOf();
                for (RunningScript runningScript : mod.getRuntime().getRunningScripts()) {
                    runningScripts.set(runningScript.getUuid().toString(),
                            runningScript.getScript().getFile() != null ? runningScript.getScript().getFile().getName() : "<Script>");
                }
                return runningScripts;
            } else if (args.narg() == 1) {
                String nameOrId = args.arg1().checkjstring();

                List<LuaValue> runningScripts = new ArrayList<>();
                for (RunningScript runningScript : mod.getRuntime().getRunningScriptsByNameOrId(nameOrId)) {
                    runningScripts.add(LuaValue.valueOf(runningScript.getUuid().toString()));
                }

                return LuaValue.listOf(runningScripts.toArray(new LuaValue[0]));
            }
            return argerror("Invalid amount of arguments (valid is 0 or 1)");
        }
    }
}
