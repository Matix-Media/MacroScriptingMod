package net.matixmedia.macroscriptingmod.scripting.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matixmedia.macroscriptingmod.rendering.Renderer;
import net.matixmedia.macroscriptingmod.rendering.color.QuadColor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class EspBox extends Box {
    private final UUID uuid;
    private final Renderer renderer;

    private boolean distanceFalloff;
    private double distance;

    public EspBox(double x1, double y1, double z1, double x2, double y2, double z2, boolean distanceFalloff) {
        super(x1, y1, z1, x2, y2, z2);

        this.distanceFalloff = distanceFalloff;
        this.uuid = UUID.randomUUID();
        this.renderer = new Renderer();
        this.distance = 0;
    }

    public UUID getUuid() {
        return uuid;
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
        float r, g, b;
        if (this.distanceFalloff) {
            r = (float) (1 - this.distance / 40);
            g = (float) (this.distance / 40);
        } else {
            r = 0;
            g = 0;
        }
        b = 0;
        r = MathHelper.clamp(r, 0, 1);
        g = MathHelper.clamp(g, 0, 1);

        this.renderer.boxOutline(this, QuadColor.single(r, g, b, 1), 2);

        this.renderer.boxFill(this, QuadColor.single(r, g, b, 0.2F));
    }
}
