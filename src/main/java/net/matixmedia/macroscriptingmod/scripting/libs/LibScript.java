package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import net.matixmedia.macroscriptingmod.utils.Chat;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;

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
                Chat.sendClientSystemMessage(Chat.Color.RED + "Script not found");
                return NIL;
            }
            Chat.sendClientSystemMessage("Running \"" + scriptName + "\"...");
            Runtime.RunScriptResult result;
            try {
                result = mod.getRuntime().execute(script);
            } catch (Exception e) {
                throw new LuaError("Error executing lua script: " + e.getMessage());
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

    public static class GetRunningScripts extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();

            List<LuaValue> runningScripts = new ArrayList<>();
            for (RunningScript runningScript : mod.getRuntime().getRunningScripts()) {
                runningScripts.add(LuaValue.valueOf(runningScript.getUuid().toString()));
            }
            return LuaValue.listOf(runningScripts.toArray(new LuaValue[0]));
        }
    }
}
