package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjLocation;
import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class LibPlayer extends Lib {

    public LibPlayer() {
        super("player");
    }

    public static class GetLocation extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return null;

            return new ObjLocation(mc.player.getX(), mc.player.getY(), mc.player.getZ()).toLua();
        }
    }
}
