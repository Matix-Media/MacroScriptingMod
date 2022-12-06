package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.luaj.vm2.LuaValue;

public class ObjEntity extends Obj {

    public String ID;
    public String type;
    public double x;
    public double y;
    public double z;
    public double pitch;
    public double yaw;
    public LuaValue helmet;
    public LuaValue chest_plate;
    public LuaValue leggings;
    public LuaValue boots;

    public ObjEntity(Entity entity) {
        this.ID = entity.getUuid().toString();
        this.type = Registry.ENTITY_TYPE.getId(entity.getType()).toString();
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.pitch = entity.getPitch();
        this.yaw = entity.getHeadYaw();

        int i = 0;
        for (ItemStack item : entity.getArmorItems()) {
            LuaValue luaItem = new ObjItem(item).toLua();

            if (i == 0) boots = luaItem;
            else if (i == 1) leggings = luaItem;
            else if (i == 2) chest_plate = luaItem;
            else helmet = luaItem;

            i++;
        }
    }
}
