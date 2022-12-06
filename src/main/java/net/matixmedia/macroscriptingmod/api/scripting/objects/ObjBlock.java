package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class ObjBlock extends Obj {

    public String type;
    public int x, y, z;

    public ObjBlock(BlockPos pos, Block block) {
        this.type = Registry.BLOCK.getId(block).toString();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }
}
