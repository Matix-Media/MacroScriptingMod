package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.luaj.vm2.LuaValue;

public class LibServer extends Lib {
    public LibServer() {
        super("server");
    }

    public static class Reconnect extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (!(mc.currentScreen instanceof DisconnectedScreen)) return null;

            ServerInfo lastServer = MacroScriptingMod.getInstance().getLastServer();
            ConnectScreen.connect(new MultiplayerScreen(null), mc, ServerAddress.parse(lastServer.address), lastServer);
            return null;
        }
    }
}
