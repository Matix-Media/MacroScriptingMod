package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.exceptions.InitializationException;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.matixmedia.macroscriptingmod.utils.RealTimeOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Runtime {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Runtime");

    private final Globals globals;
    private boolean initialized = false;

    public Runtime() {
        this.globals = new Globals();
        RealTimeOutputStream outputStream = new RealTimeOutputStream(Chat::sendClientMessage);
        globals.STDOUT = new PrintStream(outputStream, true, StandardCharsets.US_ASCII);
    }

    public void addLibrary(LibFunction lib) {
        if (this.initialized) throw new InitializationException("Cannot add libraries after initialization");

        this.globals.load(lib);
    }

    public void init() {
        if (this.initialized) throw new InitializationException("Already initialized");

        LoadState.install(this.globals);
        LuaC.install(this.globals);
    }

    public CompletableFuture<LuaValue> execute(Script script) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            LuaValue chunk = null;
            try {
                chunk = this.loadScript(script);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return chunk.call();
        });
    }

    private LuaValue loadScript(Script script) throws IOException {
        return this.globals.load(script.getContent());
    }
}
