package net.matixmedia.macroscriptingmod.scripting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Script {
    private String scriptContent;
    private File sourcePath;
    private final String scriptName;

    public Script(String scriptName, File sourcePath) {
        this.scriptName = scriptName;
        this.sourcePath = sourcePath;
    }

    public Script(String scriptContent) {
        this.scriptName = "<eval>";
        this.scriptContent = scriptContent;
    }

    public String getContent() throws IOException {
        if (sourcePath == null) return this.scriptContent;
        else return String.join("\n", Files.readAllLines(this.sourcePath.toPath()));
    }

    public File getFile() {
        return this.sourcePath;
    }

    public String getScriptName() {
        return this.scriptName;
    }
}
