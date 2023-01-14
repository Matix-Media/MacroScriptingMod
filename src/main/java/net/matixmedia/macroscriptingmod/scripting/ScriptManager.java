package net.matixmedia.macroscriptingmod.scripting;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ScriptManager {
    private final Map<String, Script> scripts = new HashMap<>();
    private final Path scriptsDirectory;

    public ScriptManager(Path scriptsDirectory) {
        this.scriptsDirectory = scriptsDirectory;
    }


    public Script loadScript(String scriptName) {
        for (Script script : this.getAvailableScripts()) {
            if (!script.getScriptName().equals(scriptName)) continue;
            return script;
        }
        return null;
    }

    public Collection<Script> getAvailableScripts() {
        File[] files = this.scriptsDirectory.toFile().listFiles();
        if (files == null) return new ArrayList<>();

        List<Script> availableScripts = new ArrayList<>();
        List<Script> locatedScripts = new ArrayList<>();
        for (File file : files) {
            Script script;
            if (scripts.containsKey(file.getPath())) {
                script = scripts.get(file.getPath());
            } else {
                String scriptName = file.getPath().substring(this.scriptsDirectory.toString().length() + 1);
                script = new Script(scriptName, file);
                scripts.put(file.getPath(), script);
            }
            locatedScripts.add(script);
            availableScripts.add(script);
        }
        for (Map.Entry<String, Script> entry : scripts.entrySet()) {
            if (!locatedScripts.contains(entry.getValue())) scripts.remove(entry.getKey());
        }
        return availableScripts;
    }

    public Path getScriptsDirectory() {
        return scriptsDirectory;
    }
}
