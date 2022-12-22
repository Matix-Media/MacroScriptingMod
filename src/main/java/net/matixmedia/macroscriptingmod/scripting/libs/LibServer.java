package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.MacroScriptingMod;
import net.matixmedia.macroscriptingmod.api.scripting.AutoLibFunction;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

public class LibServer extends Lib {

    public LibServer() {
        super("server");
    }

    @AutoLibFunction
    public static class Disconnect extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().world == null) return null;

            boolean isSinglePlayer = this.getMinecraft().isInSingleplayer();
            boolean connectedToRealms = this.getMinecraft().isConnectedToRealms();

            this.getMinecraft().world.disconnect();
            if (isSinglePlayer) {
                this.getMinecraft().disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
            } else {
                this.getMinecraft().disconnect();
            }

            TitleScreen titleScreen = new TitleScreen();
            if (isSinglePlayer) {
                this.getMinecraft().setScreen(titleScreen);
            } else if (connectedToRealms) {
                this.getMinecraft().setScreen(new RealmsMainScreen(titleScreen));
            } else {
                this.getMinecraft().setScreen(new MultiplayerScreen(titleScreen));
            }

            return null;
        }
    }

    @AutoLibFunction
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

    @AutoLibFunction
    public static class GetOnlinePlayers extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            List<LuaValue> entries = new ArrayList<>();
            for (PlayerListEntry entry : this.getMinecraft().player.networkHandler.getPlayerList()) {
                entries.add(new ObjPlayer(entry).toLua());
            }

            return LuaValue.listOf(entries.toArray(new LuaValue[0]));
        }
    }
}
