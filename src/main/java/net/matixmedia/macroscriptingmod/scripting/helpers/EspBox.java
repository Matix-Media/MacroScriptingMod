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
    private int red, green, blue;

    public EspBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);

        this.distanceFalloff = true;
        this.uuid = UUID.randomUUID();
        this.renderer = new Renderer();
        this.distance = 0;
    }

    public EspBox(double x1, double y1, double z1, double x2, double y2, double z2, int red, int green, int blue) {
        super(x1, y1, z1, x2, y2, z2);

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

        this.renderer.boxOutline(this, QuadColor.single(r, g, b, 255), 2);
        this.renderer.boxFill(this, QuadColor.single(r, g, b, 50));
    }
}
