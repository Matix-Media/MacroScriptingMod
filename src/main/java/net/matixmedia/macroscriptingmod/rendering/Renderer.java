package net.matixmedia.macroscriptingmod.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.matixmedia.macroscriptingmod.math.Boxes;
import net.matixmedia.macroscriptingmod.rendering.color.LineColor;
import net.matixmedia.macroscriptingmod.rendering.color.QuadColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class Renderer {

    public void boxOutline(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
        this.setup();

        MatrixStack matrices = this.matrixFrom(box.minX, box.minY, box.minZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth(lineWidth);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vertexer.vertexBoxLines(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
        tessellator.draw();

        RenderSystem.enableCull();

        this.cleanup();
    }

    public void boxFill(Box box, QuadColor color, Direction... excludeDirs) {
        this.setup();

        MatrixStack matrices = this.matrixFrom(box.minX, box.minY, box.minZ);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Vertexer.vertexBoxQuads(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
        tessellator.draw();

        this.cleanup();
    }

    public void line(double x1, double y1, double z1, double x2, double y2, double z2, LineColor color, float width) {
        this.setup();

        MatrixStack matrices = this.matrixFrom(x1, y1, z1);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth(width);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
        Vertexer.vertexLine(matrices, buffer, 0f, 0f, 0f, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), color);
        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();

        this.cleanup();
    }

    private MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();

        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180F));

        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        return matrices;
    }

    private void setup() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
    }

    private void cleanup() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}
