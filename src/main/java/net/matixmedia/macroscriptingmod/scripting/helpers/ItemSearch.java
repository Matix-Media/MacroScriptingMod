package net.matixmedia.macroscriptingmod.scripting.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class ItemSearch {
    private final String itemName;

    public ItemSearch(String itemNameId) {
        this.itemName = itemNameId.toLowerCase();
    }

    public String getItemName() {
        return itemName;
    }

    public boolean matches(@Nullable ItemStack stack) {
        if (stack == null) return false;
        return (Registry.ITEM.getId(stack.getItem()).toString().equals(this.itemName));
    }
}
