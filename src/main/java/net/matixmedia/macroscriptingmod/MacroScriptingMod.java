package net.matixmedia.macroscriptingmod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.matixmedia.macroscriptingmod.commandsystem.CommandManager;
import net.matixmedia.macroscriptingmod.commandsystem.commands.CommandEval;
import net.matixmedia.macroscriptingmod.exceptions.InitializationException;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;

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
    private Path scriptsDir;
    private Globals luaGlobals;


    @Override
    public void onInitialize() {
        LOGGER.info("Initializing");

        this.registerCommands();

        LOGGER.info("Checking scripts directory");
        scriptsDir = MinecraftClient.getInstance().runDirectory.toPath().normalize().resolve("scripts");
        try {
            Files.createDirectories(scriptsDir);
        } catch (IOException exception) {
            throw new InitializationException("Could not create scripts directory", exception);
        }
    }

    private void registerCommands() {
        LOGGER.info("Registering commands");

        this.commandManager = new CommandManager();

        this.commandManager.registerCommand(new CommandEval());
    }
}
