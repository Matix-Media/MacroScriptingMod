package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class LibWorld extends Lib {

    public LibWorld() {
        super("world");
    }

    public static class GetBlock extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.world == null) return null;

            int realX = x.checkint();
            int realY = y.checkint();
            int realZ = z.checkint();

            BlockPos pos = new BlockPos(realX, realY, realZ);

            Block block = mc.world.getBlockState(pos).getBlock();

            return new ObjBlock(pos, block).toLua();
        }
    }

    public static class GetBlockRel extends ThreeArgFunction {
        @Override
        public LuaValue call(LuaValue x, LuaValue y, LuaValue z) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.world == null) return null;

            int realX = x.checkint();
            int realY = y.checkint();
            int realZ = z.checkint();

            BlockPos pos = new BlockPos(mc.player.getPos().x + realX, mc.player.getPos().y + realY, mc.player.getPos().z + realZ);

            Block block = mc.world.getBlockState(pos).getBlock();

            return new ObjBlock(pos, block).toLua();
        }
    }
}
