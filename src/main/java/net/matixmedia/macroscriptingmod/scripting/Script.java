package net.matixmedia.macroscriptingmod.scripting;

import net.minecraft.entity.ai.brain.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class Script {
    private String scriptContent;
    private File sourcePath;

    public Script(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    public Script(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    public String getContent() throws IOException {
        if (sourcePath == null) return this.scriptContent;
        else return String.join("\n", Files.readAllLines(this.sourcePath.toPath()));
    }

    public File getFile() {
        return this.sourcePath;
    }
}
