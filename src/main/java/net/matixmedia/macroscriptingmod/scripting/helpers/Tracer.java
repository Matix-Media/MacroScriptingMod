package net.matixmedia.macroscriptingmod.scripting.helpers;

import net.matixmedia.macroscriptingmod.rendering.Renderer;
import net.matixmedia.macroscriptingmod.rendering.color.LineColor;
import net.matixmedia.macroscriptingmod.rendering.color.QuadColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class Tracer {
    private final UUID uuid;
    private final Renderer renderer;
    private Vec3d target;
    private boolean distanceFalloff;
    private double distance;
    private int red, green, blue;

    public Tracer(double x, double y, double z) {
        this.target = new Vec3d(x, y, z);
        this.distanceFalloff = true;
        this.uuid = UUID.randomUUID();
        this.renderer = new Renderer();
        this.distance = 0;
    }

    public Tracer(double x, double y, double z, int red, int green, int blue) {
        this.target = new Vec3d(x, y, z);
        this.distanceFalloff = false;
        this.uuid = UUID.randomUUID();
        this.renderer = new Renderer();
        this.distance = 0;

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Vec3d getTarget() {
        return target;
    }

    public boolean isDistanceFalloff() {
        return distanceFalloff;
    }

    public void setDistanceFalloff(boolean distanceFalloff) {
        this.distanceFalloff = distanceFalloff;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void render(MatrixStack matrices, float tickDelta, long limitTime, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.cameraEntity == null) return;

        int r, g, b;
        if (this.distanceFalloff) {
            r = (int) ((1 - this.distance / 40) * 255);
            g = (int) ((this.distance / 40) * 255);
            b = 0;
        } else {
            r = this.red;
            g = this.green;
            b = this.blue;
        }
        r = MathHelper.clamp(r, 0, 255);
        g = MathHelper.clamp(g, 0, 255);
        b = MathHelper.clamp(b, 0, 255);

        Vec3d vec = new Vec3d(0, 0, 75)
                .rotateX(-(float) Math.toRadians(gameRenderer.getCamera().getPitch()))
                .rotateY(-(float) Math.toRadians(gameRenderer.getCamera().getYaw()))
                .add(mc.cameraEntity.getEyePos());

        LineColor lineColor = LineColor.single(r, g, b, 255);
        this.renderer.line(vec.x, vec.y, vec.z, this.target.x, this.target.y, this.target.z, lineColor, 1);
    }
}
