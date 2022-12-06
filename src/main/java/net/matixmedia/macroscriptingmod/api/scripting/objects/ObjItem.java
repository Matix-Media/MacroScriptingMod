package net.matixmedia.macroscriptingmod.api.scripting.objects;

import net.matixmedia.macroscriptingmod.api.scripting.Obj;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class ObjItem extends Obj {
    private final static Style LORE_STYLE = Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(true);

    public String type;
    public int amount;
    public String lore;
    public String name;

    public ObjItem(ItemStack stack) {
        this.type = Registry.ITEM.getId(stack.getItem()).toString();
        this.amount = stack.getCount();
        this.lore = "";
        this.name = stack.getName().getString();

        try {
            List<String> lines = new ArrayList<>();
            if (stack.getNbt() != null && stack.getNbt().contains("display", 10)) {
                NbtCompound compound = stack.getNbt().getCompound("display");

                if (compound.getType("Lore") == 9) {
                    NbtList list = compound.getList("Lore", 8);

                    for (int i = 0; i < list.size(); i++) {
                        String lineText = list.getString(i);

                        MutableText text = Text.Serializer.fromJson(lineText);
                        if (text != null) lines.add(Texts.setStyleIfAbsent(text, LORE_STYLE).toString());
                    }
                }
            }
            this.lore = String.join("\n", lines);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
