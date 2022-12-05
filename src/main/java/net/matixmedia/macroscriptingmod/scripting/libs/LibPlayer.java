package net.matixmedia.macroscriptingmod.scripting.libs;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjGameMode;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjLocation;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class LibPlayer extends Lib {

    public LibPlayer() {
        super("player");
    }

    public static class GetInfo extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return NIL;

            return new ObjPlayer(mc.player).toLua();
        }
    }

    public static class GetLocation extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return NIL;

            return new ObjLocation(mc.player.getX(), mc.player.getY(), mc.player.getZ()).toLua();
        }
    }

    public static class GetGamemode extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.getNetworkHandler() == null) return NIL;
            PlayerListEntry networkPlayer = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (networkPlayer == null || networkPlayer.getGameMode() == null) return NIL;

            return new ObjGameMode(networkPlayer.getGameMode()).toLua();
        }
    }
}
