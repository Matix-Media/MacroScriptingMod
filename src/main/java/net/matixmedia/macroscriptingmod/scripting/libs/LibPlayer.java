package net.matixmedia.macroscriptingmod.scripting.libs;

import net.fabricmc.api.ModInitializer;
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

    public static class GetUsername extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getGameProfile().getName());
        }
    }

    public static class GetUuid extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getGameProfile().getId().toString());
        }
    }

    public static class GetHealth extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getHealth());
        }
    }

    public static class GetHunger extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getHungerManager().getFoodLevel());
        }
    }

    public static class GetXpLevel extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.experienceLevel);
        }
    }

    public static class GetXp extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf((int)(this.getMinecraft().player.experienceProgress * (float)this.getMinecraft().player.getNextLevelExperience()));
        }
    }

    public static class GetTotalXp extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.totalExperience);
        }
    }

    public static class GetOxygen extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getAir());
        }
    }

    public static class IsFlying extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getAbilities().flying);
        }
    }

    public static class CanFly extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return NIL;

            return LuaValue.valueOf(this.getMinecraft().player.getAbilities().allowFlying);
        }
    }
}
