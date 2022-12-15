package net.matixmedia.macroscriptingmod.scripting.libs;

import net.matixmedia.macroscriptingmod.api.scripting.*;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjBlock;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjDirection;
import net.matixmedia.macroscriptingmod.api.scripting.objects.ObjEntity;
import net.matixmedia.macroscriptingmod.eventsystem.EventHandler;
import net.matixmedia.macroscriptingmod.eventsystem.EventListener;
import net.matixmedia.macroscriptingmod.eventsystem.EventManager;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventRenderWorld;
import net.matixmedia.macroscriptingmod.eventsystem.events.EventTick;
import net.matixmedia.macroscriptingmod.mixins.AccessorClientPlayerInteractionManager;
import net.matixmedia.macroscriptingmod.rendering.color.QuadColor;
import net.matixmedia.macroscriptingmod.scripting.helpers.EspBox;
import net.matixmedia.macroscriptingmod.scripting.helpers.Tracer;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class LibWorld extends Lib implements EventListener {

    private final List<EspBox> espBoxes = new ArrayList<>();
    private final List<Tracer> tracers = new ArrayList<>();
    private final List<BlockPos> miningBlocks = new ArrayList<>();

    public LibWorld() {
        super("world");
    }

    @Override
    public void init() {
        super.init();
        EventManager.registerListener(this);
    }

    public void dispose() {
        super.dispose();
        EventManager.unregisterListener(this);
    }

    @EventHandler
    public void onRenderWorld(EventRenderWorld event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;

        synchronized (this.espBoxes) {
            synchronized (this.tracers) {
                for (int i = 0; i < this.espBoxes.size(); i++) {
                    EspBox espBox = this.espBoxes.get(i);
                    if (espBox == null) continue;
                    espBox.render(event.getMatrices(), event.getTickDelta(), event.getLimitTime(), event.getCamera(), event.getGameRenderer(), event.getPositionMatrix());
                }
                for (int i = 0; i < this.tracers.size(); i++) {
                    Tracer tracer = this.tracers.get(i);
                    if (tracer == null) continue;
                    tracer.render(event.getMatrices(), event.getTickDelta(), event.getLimitTime(), event.getCamera(), event.getGameRenderer(), event.getPositionMatrix());
                }
            }

        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null) return;
        if (mc.player == null) return;

        for (EspBox espBox : this.espBoxes) {
            if (!espBox.isDistanceFalloff()) continue;
            double x = (espBox.minX + espBox.maxX) / 2;
            double y = (espBox.minY + espBox.maxY) / 2;
            double z = (espBox.minZ + espBox.maxZ) / 2;
            double distance = new Vec3d(x, y, z).distanceTo(mc.player.getPos());
            espBox.setDistance(distance);
        }

        for (Tracer tracer : this.tracers) {
            if (!tracer.isDistanceFalloff()) continue;
            double distance = tracer.getTarget().distanceTo(mc.player.getPos());
            tracer.setDistance(distance);
        }


        synchronized (this.miningBlocks) {
            ListIterator<BlockPos> iterator = this.miningBlocks.listIterator();
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                Vec3d posV = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                float breakDelta = mc.world.getBlockState(pos).calcBlockBreakingDelta(mc.player, mc.world, pos);

                if (posV.distanceTo(mc.player.getEyePos()) > 4.5) {
                    iterator.remove();
                    continue;
                }

                if (mc.interactionManager != null) {
                    mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
                    mc.player.swingHand(Hand.MAIN_HAND);

                    if (((AccessorClientPlayerInteractionManager) mc.interactionManager).getCurrentBreakingProgress() == 0F) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @AutoLibFunction
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

    @AutoLibFunction
    public static class GetBlockRelative extends ThreeArgFunction {
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

    @AutoLibFunction
    public static class GetNearbyEntities extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (this.getMinecraft().player == null) return NIL;
            if (this.getMinecraft().world == null) return NIL;
            ClientPlayerEntity player = this.getMinecraft().player;
            int range = arg.checkint();

            double bottomX = player.getX() - range;
            double bottomY = player.getY() - range;
            double bottomZ = player.getZ() - range;

            double topX = player.getX() + range;
            double topY = player.getY() + range;
            double topZ = player.getZ() + range;

            List<Entity> entities = this.getMinecraft().world.getOtherEntities(this.getMinecraft().player, new Box(bottomX, bottomY, bottomZ, topX, topY, topZ));
            List<LuaValue> luaEntities = new ArrayList<>();

            for (Entity entity : entities) luaEntities.add(new ObjEntity(entity).toLua());

            return LuaValue.listOf(luaEntities.toArray(new LuaValue[0]));
        }
    }

    @AutoLibFunction
    public static class GetEntities extends LibZeroArgFunction {
        @Override
        public LuaValue call() {
            if (this.getMinecraft().world == null) return NIL;
            List<LuaValue> luaEntities = new ArrayList<>();
            for(Entity entity : this.getMinecraft().world.getEntities()) if (entity != null) luaEntities.add(new ObjEntity(entity).toLua());

            return LuaValue.listOf(luaEntities.toArray(new LuaValue[0]));
        }
    }

    @AutoLibFunction
    public static class ShowEspBox extends LibArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LibWorld instance = (LibWorld) getInstance(LibWorld.class, this.getRunningScript());
            if (instance == null) return null;
            String errorMessage = "Invalid amount of arguments (valid is 6 or 9)";

            if (args.narg() < 3) return argerror(errorMessage);
            double minX = args.arg1().checkdouble();
            double minY = args.arg(2).checkdouble();
            double minZ = args.arg(3).checkdouble();

            double maxX = args.arg(4).checkdouble();
            double maxY = args.arg(5).checkdouble();
            double maxZ = args.arg(6).checkdouble();

            if (args.narg() == 6) {
                EspBox espBox = new EspBox(minX, minY, minZ, maxX, maxY, maxZ);
                instance.espBoxes.add(espBox);

                return LuaValue.valueOf(espBox.getUuid().toString());
            } else if (args.narg() == 9) {
                int red = args.arg(7).checkint();
                int green = args.arg(8).checkint();
                int blue = args.arg(9).checkint();

                EspBox espBox = new EspBox(minX, minY, minZ, maxX, maxY, maxZ, red, green, blue);
                instance.espBoxes.add(espBox);

                return LuaValue.valueOf(espBox.getUuid().toString());
            }

            return argerror(errorMessage);
        }
    }

    @AutoLibFunction
    public static class RemoveEspBox extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibWorld instance = (LibWorld) getInstance(LibWorld.class, this.getRunningScript());
            if (instance == null) return null;
            String espBoxId = arg.checkjstring();

            ListIterator<EspBox> iterator = instance.espBoxes.listIterator();
            while (iterator.hasNext()) {
                EspBox espBox = iterator.next();
                if (espBox.getUuid().toString().equals(espBoxId)) {
                    iterator.remove();
                    break;
                }
            }
            return null;
        }
    }

    @AutoLibFunction
    public static class ShowTracer extends LibArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LibWorld instance = (LibWorld) getInstance(LibWorld.class, this.getRunningScript());
            if (instance == null) return null;
            String err = "Invalid amount of arguments (valid is 3 or 6)";

            if (args.narg() < 3) return argerror(err);
            double x = args.arg1().checkdouble();
            double y = args.arg(2).checkdouble();
            double z = args.arg(3).checkdouble();

            if (args.narg() == 3) {
                Tracer tracer = new Tracer(x, y, z);
                instance.tracers.add(tracer);

                return LuaValue.valueOf(tracer.getUuid().toString());
            } else if (args.narg() == 6) {
                int red = args.arg(4).checkint();
                int green = args.arg(5).checkint();
                int blue = args.arg(6).checkint();

                Tracer tracer = new Tracer(x, y, z, red, green, blue);
                instance.tracers.add(tracer);

                return LuaValue.valueOf(tracer.getUuid().toString());
            }

            return argerror(err);
        }
    }

    @AutoLibFunction
    public static class RemoveTracer extends LibOneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            LibWorld instance = (LibWorld) getInstance(LibWorld.class, this.getRunningScript());
            if (instance == null) return null;
            String tracerId = arg.checkjstring();

            ListIterator<Tracer> iterator = instance.tracers.listIterator();
            while (iterator.hasNext()) {
                Tracer tracer = iterator.next();
                if (tracer.getUuid().toString().equals(tracerId)) {
                    iterator.remove();
                    break;
                }
            }
            return null;
        }
    }

    @AutoLibFunction
    public static class BreakBlock extends LibThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            if (this.getMinecraft().player == null) return null;
            if (this.getMinecraft().interactionManager == null) return null;
            if (this.getMinecraft().world == null) return null;
            LibWorld instance = (LibWorld) getInstance(LibWorld.class, this.getRunningScript());
            if (instance == null) return null;

            int x = arg1.checkint();
            int y = arg2.checkint();
            int z = arg3.checkint();

            BlockPos pos = new BlockPos(x, y, z);
            synchronized (instance.miningBlocks) {
                instance.miningBlocks.add(pos);
            }
            return null;
        }
    }

    @AutoLibFunction
    public static class CalcYawPitchTo extends LibThreeArgFunction {
        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
            if (this.getMinecraft().player == null) return null;
            double xPos = arg1.checkdouble() + 0.5D;
            double zPos = arg3.checkdouble() + 0.5D;
            double deltaX = xPos - this.getMinecraft().player.getX();
            double deltaZ = zPos - this.getMinecraft().player.getZ();

            int yaw;
            for (yaw = (int)(Math.atan2(deltaZ, deltaX) * 180.0 / Math.PI - 90.0); yaw < 0; yaw += 360) {}

            if (yaw > 180) yaw -= 360;
            else if (yaw < -180) yaw += 360;

            return new ObjDirection(yaw - 180, 0).toLua();
        }
    }
}
