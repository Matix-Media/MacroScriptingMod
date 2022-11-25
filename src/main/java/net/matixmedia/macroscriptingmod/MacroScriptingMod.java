package net.matixmedia.macroscriptingmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.matixmedia.macroscriptingmod.commandsystem.CommandManager;
import net.matixmedia.macroscriptingmod.commandsystem.commands.CommandEval;
import net.matixmedia.macroscriptingmod.commandsystem.commands.CommandRun;
import net.matixmedia.macroscriptingmod.exceptions.InitializationException;
import net.matixmedia.macroscriptingmod.scripting.Runtime;
import net.matixmedia.macroscriptingmod.scripting.ScriptManager;
import net.matixmedia.macroscriptingmod.scripting.libs.LibPlayer;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class MacroScriptingMod implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("MacroScripting");
    public static String getChatPrefix() {
        return Chat.Color.BLUE + "[" + Chat.Color.GRAY + "MacroScriptingMod" + Chat.Color.BLUE + "] " + Chat.Color.GRAY;
    }

    private CommandManager commandManager;
    private ScriptManager scriptManager;
    private Path scriptsDir;
    private Globals luaGlobals;
    private Runtime runtime;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing");

        LOGGER.info("Checking scripts directory");
        scriptsDir = MinecraftClient.getInstance().runDirectory.toPath().normalize().resolve("scripts");
        try {
            Files.createDirectories(scriptsDir);
        } catch (IOException exception) {
            throw new InitializationException("Could not create scripts directory", exception);
        }

        this.scriptManager = new ScriptManager(this.scriptsDir);

        this.runtime = new Runtime();

        this.runtime.addLibrary(new JseBaseLib());
        this.runtime.addLibrary(new PackageLib());
        this.runtime.addLibrary(new StringLib());
        this.runtime.addLibrary(new TableLib());
        this.runtime.addLibrary(new JseMathLib());
        this.runtime.addLibrary(new Bit32Lib());
        this.runtime.addLibrary(new CoroutineLib());
        this.runtime.addLibrary(new JseIoLib());
        this.runtime.addLibrary(new JseOsLib());

        this.runtime.addLibrary(new LibPlayer());

        this.runtime.init();

        this.registerCommands();
    }

    private void registerCommands() {
        LOGGER.info("Registering commands");

        this.commandManager = new CommandManager();

        this.commandManager.registerCommand(new CommandEval(this.runtime));
        this.commandManager.registerCommand(new CommandRun(this.runtime, this.scriptManager));
    }
}
