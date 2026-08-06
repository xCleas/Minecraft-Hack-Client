package dev.just.util.shader;

import dev.just.manager.IMinecraft;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.math.MatrixStack.Entry;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShaderManager implements IMinecraft {
   public static void vertexShader(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
      float[] rgba = ColorUtil.rgba(color);
      Matrix4f matrix = matrixStack.peek().getPositionMatrix();
      BufferBuilder builder = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      builder.vertex(matrix, x, y, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
      builder.vertex(matrix, x, y + height, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
      builder.vertex(matrix, x + width, y + height, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
      builder.vertex(matrix, x + width, y, 0.0F).color(rgba[0], rgba[1], rgba[2], rgba[3]);
      RenderUtil.render3D.endBuilding(builder);
   }

   public static void vertexShader(MatrixStack matrixStack, float x, float y, float width, float height, int color1, int color2, int color3, int color4) {
      Matrix4f matrix = matrixStack.peek().getPositionMatrix();
      BufferBuilder builder = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      builder.vertex(matrix, x, y, 0.0F)
         .color(
            (float)ColorUtil.getRed(color1) / 255.0F,
            (float)ColorUtil.getGreen(color1) / 255.0F,
            (float)ColorUtil.getBlue(color1) / 255.0F,
            (float)ColorUtil.getAlpha(color1) / 255.0F
         );
      builder.vertex(matrix, x, y + height, 0.0F)
         .color(
            (float)ColorUtil.getRed(color2) / 255.0F,
            (float)ColorUtil.getGreen(color2) / 255.0F,
            (float)ColorUtil.getBlue(color2) / 255.0F,
            (float)ColorUtil.getAlpha(color2) / 255.0F
         );
      builder.vertex(matrix, x + width, y + height, 0.0F)
         .color(
            (float)ColorUtil.getRed(color3) / 255.0F,
            (float)ColorUtil.getGreen(color3) / 255.0F,
            (float)ColorUtil.getBlue(color3) / 255.0F,
            (float)ColorUtil.getAlpha(color3) / 255.0F
         );
      builder.vertex(matrix, x + width, y, 0.0F)
         .color(
            (float)ColorUtil.getRed(color4) / 255.0F,
            (float)ColorUtil.getGreen(color4) / 255.0F,
            (float)ColorUtil.getBlue(color4) / 255.0F,
            (float)ColorUtil.getAlpha(color4) / 255.0F
         );
      RenderUtil.render3D.endBuilding(builder);
   }

   public static void vertexLine(MatrixStack matrices, VertexConsumer buffer, float x1, float y1, float z1, float x2, float y2, float z2, int lineColor) {
      Matrix4f model = matrices.peek().getPositionMatrix();
      Entry entry = matrices.peek();
      Vector3f normalVec = getNormal(x1, y1, z1, x2, y2, z2);
      buffer.vertex(model, x1, y1, z1)
         .color(
            (float)ColorUtil.getRed(lineColor) / 255.0F,
            (float)ColorUtil.getGreen(lineColor) / 255.0F,
            (float)ColorUtil.getBlue(lineColor) / 255.0F,
            (float)ColorUtil.getAlpha(lineColor) / 255.0F
         )
         .normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
      buffer.vertex(model, x2, y2, z2)
         .color(
            (float)ColorUtil.getRed(lineColor) / 255.0F,
            (float)ColorUtil.getGreen(lineColor) / 255.0F,
            (float)ColorUtil.getBlue(lineColor) / 255.0F,
            (float)ColorUtil.getAlpha(lineColor) / 255.0F
         )
         .normal(entry, normalVec.x(), normalVec.y(), normalVec.z());
   }

   public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
      float xNormal = x2 - x1;
      float yNormal = y2 - y1;
      float zNormal = z2 - z1;
      float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
      return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
   }
}
