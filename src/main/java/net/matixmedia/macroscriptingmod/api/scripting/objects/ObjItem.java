package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class ObjItem extends Obj {
    public String name;
    public int amount;

    public ObjItem(ItemStack stack) {
        this.name = Registry.ITEM.getId(stack.getItem()).toString();
        this.amount = stack.getCount();
    }
}
