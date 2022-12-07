package net.matixmedia.macroscriptingmod.scripting;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventScriptEnd;
import net.matixmedia.macroscriptingmod.exceptions.InitializationException;
import net.matixmedia.macroscriptingmod.exceptions.ScriptInterruptedException;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.matixmedia.macroscriptingmod.utils.RealTimeOutputStream;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

        runningScript.setGlobals(globals);
        return new GlobalsHolder(globals, initiatedLibs);
    }

    public CompletableFuture<LuaValue> execute(Script script) throws RuntimeException {
        return CompletableFuture.supplyAsync(() -> {
            InterruptDebugger interruptDebugger = new InterruptDebugger();
            RunningScript runningScript = new RunningScript(script, Thread.currentThread(), interruptDebugger);
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
                LOGGER.info("Type: " + e.getClass().getSimpleName());
                if (e instanceof LuaError luaError && luaError.getCause() != null) {
                    LOGGER.info("Cause: " + luaError.getClass().getSimpleName());
                    LOGGER.info("Script got stopped");
                } else {
                    LOGGER.error("Error executing lua script");
                    Chat.sendClientSystemMessage(Chat.Color.RED + "Error executing lua script: " + e.getMessage());
                    e.printStackTrace();
                    exception = e;
                }
            }


            LOGGER.info("Removing sandbox for " + runningScript.getUuid());
            this.runningScripts.remove(runningScript);
            EventManager.fire(new EventScriptEnd(runningScript));

            for (LibFunction lib : globalsHolder.getLibraries()) if (lib instanceof Lib _lib) _lib.dispose();

            if (exception != null) throw exception;

            return result;
        });
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
}
