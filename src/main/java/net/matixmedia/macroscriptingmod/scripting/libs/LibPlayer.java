package net.matixmedia.macroscriptingmod.scripting.libs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.matixmedia.macroscriptingmod.api.scripting.*;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjGameMode;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjItem;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjLocation;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjPlayer;
import net.matixmedia.macroscriptingmod.scripting.helpers.ItemSearch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
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

    public static class GetYaw extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return null;
            return LuaValue.valueOf(this.getMinecraft().player.getHeadYaw());
        }
    }

    public static class GetPitch extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().player == null) return null;
            return LuaValue.valueOf(this.getMinecraft().player.getPitch());
        }
    }

    public static class Look extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            if (this.getMinecraft().player ==  null) return null;
            double yaw = arg1.checkdouble();
            double pitch = arg2.checkdouble();
            pitch = MathHelper.clamp(pitch,-90,90);
            yaw = MathHelper.clamp(yaw,-180,180);
            this.getMinecraft().player.setYaw((float) yaw);
            this.getMinecraft().player.setPitch((float) pitch);
            return null;
        }
    }

    public static class LookSmooth extends LibThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            if (this.getMinecraft().player ==  null) return null;
            MinecraftClient mc = this.getMinecraft();
            double yawWanted = arg1.checkdouble();
            double pitchWanted = arg2.checkdouble();
            double seconds = arg3.checkdouble();
            double steps = seconds * 100;
            double yaw = mc.player.getHeadYaw();
            double pitch = mc.player.getPitch();
            yawWanted = MathHelper.clamp(yawWanted,-180,180);
            pitchWanted = MathHelper.clamp(pitchWanted,-90,90);

            if (yaw < 0) yaw += 360;
            if (yawWanted < 0) yawWanted += 360;

            pitch += 90;
            pitchWanted += 90;

            double yawDiff = yawWanted - yaw;
            double pitchDiff = pitchWanted - pitch;

            if (yawDiff > 180) yawDiff -= 360;
            else if (yawDiff < -180) yawDiff += 360;

            double yawStep = yawDiff / steps;
            double pitchStep = pitchDiff / steps;

            double savedYaw = mc.player.getHeadYaw();
            double savedPitch = mc.player.getPitch();
            for (int i = 0; i < steps; i++) {
                double toBeSetYaw = savedYaw + yawStep;
                double toBeSetPitch = savedPitch + pitchStep;

                if (toBeSetYaw > 180) toBeSetYaw -= 360;
                else if (toBeSetYaw < -180) toBeSetYaw += 360;

                mc.player.setYaw((float) (toBeSetYaw));
                mc.player.setPitch((float) (toBeSetPitch));
                savedYaw = toBeSetYaw;
                savedPitch = toBeSetPitch;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mc.player.setYaw((float) yawWanted);
            mc.player.setPitch((float) pitchWanted - 90);

            return null;
        }

    }

    public static class LookRelative extends LibTwoArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            if (this.getMinecraft().player ==  null) return null;
            double yaw = arg1.checkdouble() + this.getMinecraft().player.getHeadYaw();
            double pitch = arg2.checkdouble() + this.getMinecraft().player.getPitch();
            if (yaw > 180) {
                while (true) {
                    yaw -= 360;
                    if(yaw >= -180 && yaw <= 180) break;
                }
            }
            if (yaw < -180) {
                while (true) {
                    yaw += 360;
                    if(yaw >= -180 && yaw <= 180) break;
                }
            }
            pitch = MathHelper.clamp(pitch,-90,90);
            yaw = MathHelper.clamp(yaw,-180,180);
            this.getMinecraft().player.setYaw((float) yaw);
            this.getMinecraft().player.setPitch((float) pitch);
            return null;
        }
    }


    public static class LookSmoothRelative extends LibThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            if (this.getMinecraft().player ==  null) return null;
            MinecraftClient mc = this.getMinecraft();
            double yawWanted = arg1.checkdouble() + mc.player.getHeadYaw();
            double pitchWanted = arg2.checkdouble() + mc.player.getPitch();
            double seconds = arg3.checkdouble();
            double steps = seconds * 100;
            double yaw = mc.player.getHeadYaw();
            double pitch = mc.player.getPitch();
            if (yawWanted > 180) {
                while (true) {
                    yawWanted -= 360;
                    if(yawWanted >= -180 && yawWanted <= 180) break;
                }
            }
            if (yawWanted < -180) {
                while (true) {
                    yawWanted += 360;
                    if(yawWanted >= -180 && yawWanted <= 180) break;
                }
            }
            yawWanted = MathHelper.clamp(yawWanted,-180,180);
            pitchWanted = MathHelper.clamp(pitchWanted,-90,90);

            if (yaw < 0) yaw += 360;
            if (yawWanted < 0) yawWanted += 360;

            pitch += 90;
            pitchWanted += 90;

            double yawDiff = yawWanted - yaw;
            double pitchDiff = pitchWanted - pitch;

            if (yawDiff > 180) yawDiff -= 360;
            else if (yawDiff < -180) yawDiff += 360;

            double yawStep = yawDiff / steps;
            double pitchStep = pitchDiff / steps;

            double savedYaw = mc.player.getHeadYaw();
            double savedPitch = mc.player.getPitch();
            for (int i = 0; i < steps; i++) {
                double toBeSetYaw = savedYaw + yawStep;
                double toBeSetPitch = savedPitch + pitchStep;

                if (toBeSetYaw > 180) toBeSetYaw -= 360;
                else if (toBeSetYaw < -180) toBeSetYaw += 360;

                mc.player.setYaw((float) (toBeSetYaw));
                mc.player.setPitch((float) (toBeSetPitch));
                savedYaw = toBeSetYaw;
                savedPitch = toBeSetPitch;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mc.player.setYaw((float) yawWanted);
            mc.player.setPitch((float) pitchWanted - 90);

            return null;
        }

    }


}
