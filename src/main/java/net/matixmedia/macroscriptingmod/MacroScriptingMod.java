package net.matixmedia.macroscriptingmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.matixmedia.macroscriptingmod.commandsystem.CommandManager;
import net.matixmedia.macroscriptingmod.commandsystem.commands.*;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventConnectToServer;
import net.matixmedia.macroscriptingmod.exceptions.InitializationException;
import net.matixmedia.macroscriptingmod.scripting.InterruptDebugger;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.scripting.libs.*;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class MacroScriptingMod implements ClientModInitializer, EventListener {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting");
    private static MacroScriptingMod INSTANCE;
    public static String getChatPrefix() {
        return Chat.Color.BLUE + "[" + Chat.Color.GRAY + "MacroScriptingMod" + Chat.Color.BLUE + "] " + Chat.Color.GRAY;
    }
    public static MacroScriptingMod getInstance() {
        return INSTANCE;
    }

    private CommandManager commandManager;
    private ScriptManager scriptManager;
    private Path scriptsDir;
    private Globals luaGlobals;
    private Runtime runtime;
    private ServerInfo lastServer;


    @Override
    public void onInitializeClient() {
        this.onInitialize();
    }

    public void onInitialize() {
        LOGGER.info("Initializing");
        INSTANCE = this;

        LOGGER.info("Checking scripts directory");
        scriptsDir = MinecraftClient.getInstance().runDirectory.toPath().normalize().resolve("scripts");
        try {
            Files.createDirectories(scriptsDir);
        } catch (IOException exception) {
            throw new InitializationException("Could not create scripts directory", exception);
        }

        this.scriptManager = new ScriptManager(this.scriptsDir);
        this.registerRuntime();
        this.registerCommands();
    }

    private void registerCommands() {
        LOGGER.info("Registering commands");

        this.commandManager = new CommandManager();

        this.commandManager.registerCommand(new CommandHelp(this.commandManager, getChatPrefix()));
        this.commandManager.registerCommand(new CommandEval(this.runtime));
        this.commandManager.registerCommand(new CommandScripts(this.scriptManager));
        this.commandManager.registerCommand(new CommandScriptsDir(this.scriptManager));
        this.commandManager.registerCommand(new CommandRun(this.runtime, this.scriptManager));
        this.commandManager.registerCommand(new CommandStop(this.runtime));
        this.commandManager.registerCommand(new CommandRunning(this.runtime));
    }

    private void registerRuntime() {
        this.runtime = new Runtime();

        this.runtime.addLibrary(JseBaseLib.class);
        this.runtime.addLibrary(PackageLib.class);
        this.runtime.addLibrary(StringLib.class);
        this.runtime.addLibrary(TableLib.class);
        this.runtime.addLibrary(JseMathLib.class);
        this.runtime.addLibrary(Bit32Lib.class);
        this.runtime.addLibrary(CoroutineLib.class);
        this.runtime.addLibrary(JseIoLib.class);
        this.runtime.addLibrary(JseOsLib.class);

        this.runtime.addLibrary(LibPlayer.class);
        this.runtime.addLibrary(LibWorld.class);
        this.runtime.addLibrary(LibInput.class);
        this.runtime.addLibrary(LibTime.class);
        this.runtime.addLibrary(LibGui.class);
        this.runtime.addLibrary(LibServer.class);
        this.runtime.addLibrary(LibText.class);
        this.runtime.addLibrary(LibScript.class);
        this.runtime.addLibrary(LibHttp.class);
        this.runtime.addLibrary(LibEvents.class);
        this.runtime.addLibrary(LibSettings.class);

        this.runtime.addLibrary(InterruptDebugger.class);
    }

    @EventHandler
    public void onConnectToServer(EventConnectToServer event) {
        this.lastServer = event.getServerInfo();
    }

    public ServerInfo getLastServer() {
        return lastServer;
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
