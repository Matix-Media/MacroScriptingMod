package net.matixmedia.macroscriptingmod.scripting;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ScriptManager {
    private Map<String, Script> scripts = new HashMap<>();
    private Path scriptsDir;

    public ScriptManager(Path scriptsDir) {
        this.scriptsDir = scriptsDir;
    }


    public Script loadScript(String scriptName) {
        Path scriptPath = scriptsDir.resolve(scriptName);
        if (scripts.containsKey(scriptPath.toString())) {
            return scripts.get(scriptPath.toString());
        } else {
            File scriptFile = scriptPath.toFile();
            if (!scriptFile.exists()) return null;
            Script script = new Script(scriptFile);
            scripts.put(scriptPath.toString(), script);
            return script;
        }
    }
}
