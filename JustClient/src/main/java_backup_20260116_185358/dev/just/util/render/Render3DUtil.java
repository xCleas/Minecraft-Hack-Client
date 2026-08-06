package dev.just.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Pair;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.math.MatrixStack.Entry;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;

public final class Render3DUtil implements IMinecraft {
   private static final Map<VoxelShape, Pair<List<Box>, List<Render3DUtil.Line>>> SHAPE_OUTLINES = new HashMap<>();
   private static final Map<VoxelShape, List<Box>> SHAPE_BOXES = new HashMap<>();
   public static final List<Render3DUtil.Texture> TEXTURE_DEPTH = new ArrayList<>();
   public static final List<Render3DUtil.Texture> TEXTURE = new ArrayList<>();
   public static final List<Render3DUtil.Line> LINE_DEPTH = new ArrayList<>();
   public static final List<Render3DUtil.Line> LINE = new ArrayList<>();
   public static final List<Render3DUtil.Quad> QUAD_DEPTH = new ArrayList<>();
   public static final List<Render3DUtil.Quad> QUAD = new ArrayList<>();
   public static Entry lastWorldSpaceMatrix = new MatrixStack().peek();
   public static Matrix4f lastProjMat = new Matrix4f();

   public static void onWorldRender(EventRender3D e) {
      if (!TEXTURE.isEmpty()) {
         Set<Identifier> identifiers = TEXTURE.stream().map(texture -> texture.id).collect(Collectors.toCollection(LinkedHashSet::new));
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         identifiers.forEach(
            id -> {
               RenderSystem.setShaderTexture(0, id);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               BufferBuilder bufferx = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               TEXTURE.stream()
                  .filter(texture -> texture.id.equals(id))
                  .forEach(tex -> quadTexture(tex.entry, bufferx, tex.x, tex.y, tex.width, tex.height, tex.color));
               BufferRenderer.drawWithGlobalProgram(bufferx.end());
            }
         );
         RenderSystem.disableBlend();
         TEXTURE.clear();
      }

      if (!TEXTURE_DEPTH.isEmpty()) {
         Set<Identifier> identifiers = TEXTURE_DEPTH.stream().map(texture -> texture.id).collect(Collectors.toCollection(LinkedHashSet::new));
         RenderSystem.enableBlend();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         identifiers.forEach(
            id -> {
               RenderSystem.setShaderTexture(0, id);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               BufferBuilder bufferx = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
               TEXTURE_DEPTH.stream()
                  .filter(texture -> texture.id.equals(id))
                  .forEach(tex -> quadTexture(tex.entry, bufferx, tex.x, tex.y, tex.width, tex.height, tex.color));
               BufferRenderer.drawWithGlobalProgram(bufferx.end());
            }
         );
         RenderSystem.depthMask(true);
         RenderSystem.disableBlend();
         TEXTURE_DEPTH.clear();
      }

      if (!LINE.isEmpty()) {
         GL11.glEnable(2881);
         Set<Float> widths = LINE.stream().map(line -> line.width).collect(Collectors.toCollection(LinkedHashSet::new));
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
         widths.forEach(
            width -> {
               RenderSystem.lineWidth(width);
               BufferBuilder bufferx = IMinecraft.tessellator().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
               LINE.stream()
                  .filter(line -> line.width == width)
                  .forEach(line -> vertexLine(line.entry, bufferx, line.start.toVector3f(), line.end.toVector3f(), line.colorStart, line.colorEnd));
               BufferRenderer.drawWithGlobalProgram(bufferx.end());
            }
         );
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         LINE.clear();
         GL11.glDisable(2881);
      }

      if (!QUAD.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         QUAD.forEach(quad -> vertexQuad(quad.entry, buffer, quad.x, quad.y, quad.w, quad.z, quad.color));
         BufferRenderer.drawWithGlobalProgram(buffer.end());
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         QUAD.clear();
      }

      if (!LINE_DEPTH.isEmpty()) {
         GL11.glEnable(2881);
         Set<Float> widths = LINE_DEPTH.stream().map(line -> line.width).collect(Collectors.toCollection(LinkedHashSet::new));
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
         widths.forEach(
            width -> {
               RenderSystem.lineWidth(width);
               BufferBuilder bufferx = IMinecraft.tessellator().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
               LINE_DEPTH.stream()
                  .filter(line -> line.width == width)
                  .forEach(line -> vertexLine(line.entry, bufferx, line.start.toVector3f(), line.end.toVector3f(), line.colorStart, line.colorEnd));
               BufferRenderer.drawWithGlobalProgram(bufferx.end());
            }
         );
         RenderSystem.depthMask(true);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         LINE_DEPTH.clear();
         GL11.glDisable(2881);
      }

      if (!QUAD_DEPTH.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         QUAD_DEPTH.forEach(quad -> vertexQuad(quad.entry, buffer, quad.x, quad.y, quad.w, quad.z, quad.color));
         BufferRenderer.drawWithGlobalProgram(buffer.end());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         QUAD_DEPTH.clear();
      }
   }

   public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width) {
      drawShape(blockPos, voxelShape, color, width, true, false);
   }

   public static boolean canSee(Box box) {
      Frustum frustum = mc.worldRenderer.getCapturedFrustum();
      return box != null && frustum != null && frustum.isVisible(box);
   }

   public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
      if (SHAPE_BOXES.containsKey(voxelShape)) {
         SHAPE_BOXES.get(voxelShape).forEach(box -> {
            box = box.offset(blockPos);
            if (canSee(box)) {
               drawBox(box, color, width, true, fill, depth);
            }
         });
      } else {
         SHAPE_BOXES.put(voxelShape, voxelShape.getBoundingBoxes());
      }
   }

   public static void drawShapeAlternative(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
      Vec3d vec3d = Vec3d.of(blockPos);
      if (SHAPE_OUTLINES.containsKey(voxelShape)) {
         Pair<List<Box>, List<Render3DUtil.Line>> pair = SHAPE_OUTLINES.get(voxelShape);
         if (fill) {
            ((List<Box>)pair.getLeft()).forEach(box -> drawBox(box.offset(vec3d), color, width, false, true, depth));
         }

         ((List<Render3DUtil.Line>)pair.getRight()).forEach(line -> drawLine(line.start().add(vec3d), line.end().add(vec3d), color, width, depth));
      } else {
         List<Render3DUtil.Line> lines = new ArrayList<>();
         voxelShape.forEachEdge(
            (minX, minY, minZ, maxX, maxY, maxZ) -> lines.add(
                  new Render3DUtil.Line(null, new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), 0, 0, 0.0F)
               )
         );
         SHAPE_OUTLINES.put(voxelShape, new Pair(voxelShape.getBoundingBoxes(), lines));
      }
   }

   public static void drawBox(Box box, int color, float width) {
      drawBox(box, color, width, true, true, false);
   }

   public static void drawBox(Box box, int color, float width, boolean line, boolean fill, boolean depth) {
      drawBox(null, box, color, width, line, fill, depth);
   }

   public static void drawBox(Entry entry, Box box, int color, float width, boolean line, boolean fill, boolean depth) {
      box = box.expand(0.001);
      double x1 = box.minX;
      double y1 = box.minY;
      double z1 = box.minZ;
      double x2 = box.maxX;
      double y2 = box.maxY;
      double z2 = box.maxZ;
      if (fill) {
         int fillColor = ColorUtilTest.multAlpha(color, 0.1F);
         drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), fillColor, depth);
         drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y1, z1), fillColor, depth);
         drawQuad(entry, new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), new Vec3d(x2, y1, z2), fillColor, depth);
         drawQuad(entry, new Vec3d(x1, y1, z2), new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), fillColor, depth);
         drawQuad(entry, new Vec3d(x1, y1, z1), new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), fillColor, depth);
         drawQuad(entry, new Vec3d(x1, y2, z1), new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z2), new Vec3d(x2, y2, z1), fillColor, depth);
      }

      if (line) {
         drawLine(entry, x1, y1, z1, x2, y1, z1, color, width, depth);
         drawLine(entry, x2, y1, z1, x2, y1, z2, color, width, depth);
         drawLine(entry, x2, y1, z2, x1, y1, z2, color, width, depth);
         drawLine(entry, x1, y1, z2, x1, y1, z1, color, width, depth);
         drawLine(entry, x1, y1, z2, x1, y2, z2, color, width, depth);
         drawLine(entry, x1, y1, z1, x1, y2, z1, color, width, depth);
         drawLine(entry, x2, y1, z2, x2, y2, z2, color, width, depth);
         drawLine(entry, x2, y1, z1, x2, y2, z1, color, width, depth);
         drawLine(entry, x1, y2, z1, x2, y2, z1, color, width, depth);
         drawLine(entry, x2, y2, z1, x2, y2, z2, color, width, depth);
         drawLine(entry, x2, y2, z2, x1, y2, z2, color, width, depth);
         drawLine(entry, x1, y2, z2, x1, y2, z1, color, width, depth);
      }
   }

   public static void vertexLine(MatrixStack matrices, VertexConsumer buffer, Vec3d start, Vec3d end, int startColor, int endColor) {
      vertexLine(matrices.peek(), buffer, start.toVector3f(), end.toVector3f(), startColor, endColor);
   }

   public static void vertexLine(Entry entry, VertexConsumer buffer, Vector3f start, Vector3f end, int startColor, int endColor) {
      if (entry == null) {
         entry = lastWorldSpaceMatrix;
      }

      Vector3f vec = getNormal(start, end);
      buffer.vertex(entry, start).color(startColor).normal(entry, vec);
      buffer.vertex(entry, end).color(endColor).normal(entry, vec);
   }

   public static void vertexQuad(Entry entry, VertexConsumer buffer, Vec3d vec1, Vec3d vec2, Vec3d vec3, Vec3d vec4, int color) {
      vertexQuad(entry, buffer, vec1.toVector3f(), vec2.toVector3f(), vec3.toVector3f(), vec4.toVector3f(), color);
   }

   public static void vertexQuad(Entry entry, VertexConsumer buffer, Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, int color) {
      if (entry == null) {
         entry = lastWorldSpaceMatrix;
      }

      buffer.vertex(entry, vec1).color(color);
      buffer.vertex(entry, vec2).color(color);
      buffer.vertex(entry, vec3).color(color);
      buffer.vertex(entry, vec4).color(color);
   }

   public static void quadTexture(Entry entry, BufferBuilder buffer, float x, float y, float width, float height, Vector4i color) {
      buffer.vertex(entry, x, y + height, 0.0F).texture(0.0F, 0.0F).color(color.x);
      buffer.vertex(entry, x + width, y + height, 0.0F).texture(0.0F, 1.0F).color(color.y);
      buffer.vertex(entry, x + width, y, 0.0F).texture(1.0F, 1.0F).color(color.w);
      buffer.vertex(entry, x, y, 0.0F).texture(1.0F, 0.0F).color(color.z);
   }

   @NotNull
   public static Vector3f getNormal(Vector3f start, Vector3f end) {
      Vector3f normal = new Vector3f(start).sub(end);
      float sqrt = MathHelper.sqrt(normal.lengthSquared());
      return normal.div(sqrt);
   }

   public static void drawLine(
      Entry entry, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color, float width, boolean depth
   ) {
      drawLine(entry, new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), color, color, width, depth);
   }

   public static void drawLine(Vec3d start, Vec3d end, int color, float width, boolean depth) {
      drawLine(null, start, end, color, color, width, depth);
   }

   public static void drawLine(Entry entry, Vec3d start, Vec3d end, int colorStart, int colorEnd, float width, boolean depth) {
      Render3DUtil.Line line = new Render3DUtil.Line(entry, start, end, colorStart, colorEnd, width);
      if (depth) {
         LINE_DEPTH.add(line);
      } else {
         LINE.add(line);
      }
   }

   public static void drawQuad(Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color, boolean depth) {
      drawQuad(null, x, y, w, z, color, depth);
   }

   public static void drawQuad(Entry entry, Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color, boolean depth) {
      Render3DUtil.Quad quad = new Render3DUtil.Quad(entry, x, y, w, z, color);
      if (depth) {
         QUAD_DEPTH.add(quad);
      } else {
         QUAD.add(quad);
      }
   }

   public static void drawTexture(Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color, boolean depth) {
      Render3DUtil.Texture texture = new Render3DUtil.Texture(entry, id, x, y, width, height, color);
      if (depth) {
         TEXTURE_DEPTH.add(texture);
      } else {
         TEXTURE.add(texture);
      }
   }

   private Render3DUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void setLastWorldSpaceMatrix(Entry lastWorldSpaceMatrix) {
      Render3DUtil.lastWorldSpaceMatrix = lastWorldSpaceMatrix;
   }

   public static void setLastProjMat(Matrix4f lastProjMat) {
      Render3DUtil.lastProjMat = lastProjMat;
   }

   public static record Line(Entry entry, Vec3d start, Vec3d end, int colorStart, int colorEnd, float width) {
   }

   public static record Quad(Entry entry, Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color) {
   }

   public static record Texture(Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color) {
   }
}
