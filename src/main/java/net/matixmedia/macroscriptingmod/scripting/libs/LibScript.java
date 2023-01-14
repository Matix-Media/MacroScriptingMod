package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.*;
import net.matixmedia.macroscriptingmod.exceptions.ScriptException;
import net.matixmedia.macroscriptingmod.scripting.RunningScript;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.Script;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class LibScript extends Lib {

    public LibScript() {
        super("script");
    }

    @AutoLibFunction
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

    @AutoLibFunction
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

    @AutoLibFunction
    public static class GetRunningScripts extends LibArgFunction {
        @Override
        public LuaValue call() {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();

            LuaTable runningScripts = LuaValue.tableOf();
            for (RunningScript runningScript : mod.getRuntime().getRunningScripts()) {
                runningScripts.set(runningScript.getUuid().toString(),
                        runningScript.getScript().getFile() != null ? runningScript.getScript().getFile().getName() : "<Script>");
            }
            return runningScripts;
        }

        @Override
        public LuaValue call(LuaValue arg) {
            MacroScriptingMod mod = MacroScriptingMod.getInstance();
            String nameOrId = arg.checkjstring();

            LuaTable runningScripts = LuaValue.tableOf();
            for (RunningScript runningScript : mod.getRuntime().getRunningScriptsByNameOrId(nameOrId)) {
                runningScripts.set(runningScript.getUuid().toString(),
                        runningScript.getScript().getFile() != null ? runningScript.getScript().getFile().getName() : "<Script>");
            }

            return runningScripts;
        }
    }

    @AutoLibFunction
    public static class GetCurrentScript extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            return LuaValue.valueOf(this.getRunningScript().getUuid().toString());
        }
    }

    @AutoLibFunction
    public static class GetArguments extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            List<LuaValue> arguments = new ArrayList<>();
            for (String argument : this.getRunningScript().getArguments()) {
                arguments.add(LuaValue.valueOf(argument));
            }
            return LuaValue.listOf(arguments.toArray(new LuaValue[0]));
        }
    }
}
