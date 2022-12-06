package net.matixmedia.macroscriptingmod.eventsystem.events;

import net.matixmedia.macroscriptingmod.eventsystem.Event;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class EventRenderWorld extends Event {

    private final MatrixStack matrices;
    private final float tickDelta;
    private final long limitTime;
    private final Camera camera;
    private final GameRenderer gameRenderer;
    private final Matrix4f positionMatrix;

    public EventRenderWorld(MatrixStack matrices, float tickDelta, long limitTime, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
        this.limitTime = limitTime;
        this.camera = camera;
        this.gameRenderer = gameRenderer;
        this.positionMatrix = positionMatrix;
    }

    public MatrixStack getMatrices() {
        return matrices;
    }

    public float getTickDelta() {
        return tickDelta;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public Camera getCamera() {
        return camera;
    }

    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public Matrix4f getPositionMatrix() {
        return positionMatrix;
    }
}
