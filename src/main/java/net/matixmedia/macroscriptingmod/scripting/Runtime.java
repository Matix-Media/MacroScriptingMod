package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventScriptStop;
import net.matixmedia.macroscriptingmod.exceptions.ScriptInterruptedException;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.matixmedia.macroscriptingmod.utils.RealTimeOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Runtime {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting/Runtime");

    private PrintStream printStream;
    private final List<Class<? extends LibFunction>> libraries = new ArrayList<>();
    private final List<RunningScript> runningScripts = new ArrayList<>();

    public Runtime() {
        RealTimeOutputStream outputStream = new RealTimeOutputStream(Chat::sendClientMessage);
        this.printStream = new PrintStream(outputStream, true, StandardCharsets.US_ASCII);
    }

    public void addLibrary(Class<? extends LibFunction> lib) {
        this.libraries.add(lib);
    }

    private GlobalsHolder createGlobals(RunningScript runningScript, InterruptDebugger interruptDebugger) {
        Globals globals = new Globals();
        globals.STDOUT = this.printStream;
        List<LibFunction> initiatedLibs = new ArrayList<>();
        try {
            Class<Lib> libClass = Lib.class;
            for (Class<? extends LibFunction> lib : this.libraries) {
                LibFunction libInstance = null;

                for (Constructor<?> constructor : lib.getConstructors()) {
                    if (constructor.getTypeParameters().length != 0) continue;
                    libInstance = (LibFunction) constructor.newInstance();
                    break;
                }

                if (libInstance == null) continue;
                if (libClass.isAssignableFrom(lib)) ((Lib) libInstance).setRunningScript(runningScript);

                globals.load(libInstance);
                initiatedLibs.add(libInstance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        globals.load(interruptDebugger);

        LoadState.install(globals);
        LuaC.install(globals);

        GlobalsHolder holder = new GlobalsHolder(globals, initiatedLibs);
        runningScript.setGlobalsHolder(holder);
        return holder;
    }

    public RunScriptResult execute(Script script) throws RuntimeException {
        InterruptDebugger interruptDebugger = new InterruptDebugger();
        RunningScript runningScript = new RunningScript(script, Thread.currentThread(), interruptDebugger, this);

        return new RunScriptResult(runningScript, CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Creating sandbox for " + runningScript.getUuid());
            GlobalsHolder globalsHolder = this.createGlobals(runningScript, interruptDebugger);
            if (globalsHolder == null) throw new RuntimeException("Globals could not be initiated");
            this.runningScripts.add(runningScript);

            LOGGER.info("Successfully created sandbox");

            LuaValue chunk;
            try {
                chunk = globalsHolder.getGlobals().load(script.getContent(), script.getFile() != null ? script.getFile().getName() : "<LUA CODE>");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            runningScript.setChunk(chunk);
            LuaValue result = LuaValue.NIL;
            RuntimeException exception = null;
            try {
                result = chunk.call();
            } catch (RuntimeException e) {
                if (e instanceof LuaError luaError && luaError.getCause() != null && luaError.getCause() instanceof ScriptInterruptedException) {
                    LOGGER.info("Cause: " + luaError.getCause().getClass().getSimpleName());
                    LOGGER.info("Script got stopped");
                } else {
                    LOGGER.error("Error executing lua script");
                    LOGGER.error("Type: " + e.getClass().getSimpleName());
                    if (e instanceof LuaError luaError && luaError.getCause() != null) LOGGER.error("Cause: " + e.getCause().getClass().getSimpleName());
                    Chat.sendClientSystemMessage(Chat.Color.RED + "Error executing lua script: " + e.getMessage());
                    e.printStackTrace();
                    exception = e;
                }
            }

            EventScriptStop event = new EventScriptStop.Pre(runningScript);
            EventManager.fire(event);
            if (!event.isCancelled()) runningScript.stop();

            if (exception != null) throw exception;

            return result;
        }));
    }

    public void removeSandbox(RunningScript runningScript) {
        LOGGER.info("Removing sandbox for " + runningScript.getUuid());
        this.runningScripts.remove(runningScript);
        EventManager.fire(new EventScriptStop.Post(runningScript));

        for (LibFunction lib : runningScript.getGlobalsHolder().getLibraries()) if (lib instanceof Lib _lib) _lib.dispose();
    }

    public Collection<RunningScript> getRunningScripts() {
        return new ArrayList<>(this.runningScripts);
    }

    public Collection<RunningScript> getRunningScriptsByNameOrId(String nameOrId) {
        List<RunningScript> returned = new ArrayList<>();

        for (RunningScript runningScript : this.runningScripts) {
            if ((runningScript.getUuid().toString().equals(nameOrId)) ||
                    (runningScript.getScript().getFile() != null && runningScript.getScript().getFile().getName().equals(nameOrId))) {
                returned.add(runningScript);
            }
        }

        return returned;
    }

    public static class RunScriptResult {
        private CompletableFuture<LuaValue> result;
        private RunningScript runningScript;

        public RunScriptResult(RunningScript runningScript, CompletableFuture<LuaValue> result) {
            this.runningScript = runningScript;
            this.result = result;
        }

        public CompletableFuture<LuaValue> getResult() {
            return result;
        }

        public RunningScript getRunningScript() {
            return runningScript;
        }
    }
}
