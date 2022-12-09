package net.matixmedia.macroscriptingmod.scripting.libs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.matixmedia.macroscriptingmod.api.scripting.Lib;
import net.matixmedia.macroscriptingmod.api.scripting.LibOneArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibThreeArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.LibZeroArgFunction;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjGameMode;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjItem;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjLocation;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjPlayer;
import net.matixmedia.macroscriptingmod.scripting.helpers.ItemSearch;
import net.matixmedia.macroscriptingmod.utils.Chat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.math.Vec3d;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.List;

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

    public static class GetDistanceTo extends LibThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            if (this.getMinecraft().player == null) return NIL;
            double x = arg1.checkdouble();
            double y = arg2.checkdouble();
            double z = arg3.checkdouble();

            return LuaValue.valueOf(this.getMinecraft().player.getEyePos().distanceTo(new Vec3d(x, y, z)));
        }
    }

    public static class FindItem extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return NIL;

            ItemSearch search = new ItemSearch(arg.checkjstring());
            List<ItemStack> inventory = mc.player.getInventory().main;

            for (int i = 0; i < inventory.size(); i++) {
                ItemStack slot = inventory.get(i);
                if (search.matches(slot)) return LuaValue.valueOf(i);
            }

            return NIL;
        }
    }

    public static class GetItem extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null) return NIL;
            int slot = arg.checkint();
            List<ItemStack> inventory = mc.player.getInventory().main;

            return new ObjItem(inventory.get(slot)).toLua();
        }
    }

    public static class SelectHotbarSlot extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (this.getMinecraft().player == null) return null;

            int slotId = arg.checkint();
            if (slotId > 8) return argerror(1, "Slot must be between 0 and 8");

            this.getMinecraft().player.getInventory().selectedSlot = slotId;
            return null;
        }
    }

    public static class Tellraw extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            //JsonElement jsonElement = JsonParser.parseString(arg.checkjstring());
            MutableText text = Text.Serializer.fromJson(arg.checkjstring());
            Chat.sendClientMessage(text);
            return null;
        }
    }
}
