package dev.just.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.animations.impl.EaseInOutQuad;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.math.RayTraceUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.providers.ResourceProvider;
import java.awt.Color;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.client.render.VertexFormat;
import org.joml.Matrix4f;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "TargetESP",
   desc = "SGVkZWZsZWRpxJ9pbml6IHJha2lwIMO8emVyaW5kZSDFn8SxayBnw7Zyc2VsIGVmZWt0bGVyIGfDtnN0ZXJpcg==",
   type = Type.Render
)
public class TargetESP extends Function {
   private final ModeSetting mode = new ModeSetting(I0O1l0I1.b("TW9k"), I0O1l0I1.b("RGFpcmU="), "Isaretci", "Isaretci2", I0O1l0I1.b("SGF5YWxldGxlcg=="), I0O1l0I1.b("RGFpcmU="));
   private final float[] SCALE_CACHE = new float[101];
   private final EaseInOutQuad animation = new EaseInOutQuad(800, 1.0);
   private Entity lastTarget = null;
   private double scale = 0.0;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public TargetESP() {
      this.addSettings(new Setting[]{this.mode});
      this.state = true; // Varsayilan olarak acik

      for (int i = 0; i <= 100; i++) {
         this.SCALE_CACHE[i] = Math.max(lO1I0l1O.f(0.28F) * ((float)i / lO1I0l1O.f(100.0F)), lO1I0l1O.f(0.2F));
      }
   }

  @Override
  public void onEvent(Event event) {
      if (event instanceof EventRender3D) {
          handleRenderInternal((EventRender3D) event);
      }
  }

   private void handleRenderInternal(EventRender3D renderEvent) {
      l1O0I1lO.fakeHandler();
      Entity currentTarget = getTarget();
      if (currentTarget == null || this.lastTarget != null && this.lastTarget.equals(currentTarget)) {
         if (currentTarget == null) {
            this.animation.setDirection(AxisDirection.NEGATIVE);
         }
      } else {
         this.animation.setDirection(AxisDirection.POSITIVE);
      }

      this.lastTarget = currentTarget;
      if (l1O0I1lO.opaqueTrue() && currentTarget != null) {
         if (this.mode.is("Isaretci") || this.mode.is("Isaretci2")) {
            this.render(currentTarget);
         } else if (this.mode.is(I0O1l0I1.b("SGF5YWxldGxlcg=="))) {
            this.renderGhosts(lO1I0l1O.i(14), lO1I0l1O.i(8), lO1I0l1O.f(1.8F), lO1I0l1O.f(3.0F), currentTarget);
         } else if (this.mode.is(I0O1l0I1.b("RGFpcmU="))) {
            this.cicle(currentTarget, renderEvent.getMatrixStack(), renderEvent.getDeltatick().getTickDelta(true));
         }
      }
   }

   private Entity getTarget() {
      // AttackAura hedefi
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.attackAura != null
          && Manager.FUNCTION_MANAGER.attackAura.state && Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
         return Manager.FUNCTION_MANAGER.attackAura.getTarget();
      }

      // Crosshair hedefi (baktığın entity)
      if (mc.crosshairTarget instanceof EntityHitResult hit) {
         Entity e = hit.getEntity();
         if (e instanceof LivingEntity && e != mc.player) {
            return e;
         }
      }

      return null;
   }

   @Override
   public void toggle() {
      // Kapatılamaz - her zaman açık kalır
      this.state = true;
   }

   @Override
   public void onDisable() {
      // Kapatılamaz - hemen tekrar aç
      this.state = true;
   }

   public void renderGhosts(int espLength, int factor, float shaking, float amplitude, Entity target) {
      if (target != null) {
         Camera camera = mc.gameRenderer.getCamera();
         if (camera != null) {
            float hitProgress = RayTraceUtil.getHitProgress(target);
            float delta = mc.getRenderTickCounter().getTickDelta(true);
            Vec3d camPos = camera.getPos();
            double tX = MathUtil.interpolate(target.prevX, target.getX(), (double)delta) - camPos.x;
            double tY = MathUtil.interpolate(target.prevY, target.getY(), (double)delta) - camPos.y;
            double tZ = MathUtil.interpolate(target.prevZ, target.getZ(), (double)delta) - camPos.z;
            float age = MathUtil.interpolateFloat((float)(target.age - 1), (float)target.age, delta);
            boolean canSee = mc.player.canSee(target);
            RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
            RenderSystem.setShaderTexture(0, ResourceProvider.firefly);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            if (canSee) {
               RenderSystem.enableDepthTest();
               RenderSystem.depthMask(false);
            } else {
               RenderSystem.disableDepthTest();
            }

            BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float pitch = camera.getPitch();
            float yaw = camera.getYaw();
            float ghostAlpha = (float)this.animation.getOutput();

            for (int j = 0; j < 3; j++) {
               for (int i = 0; i <= espLength; i++) {
                  float offset = (float)i / (float)espLength;
                  double radians = Math.toRadians((double)((((float)i / 1.5F + age) * (float)factor + (float)(j * 120)) % (float)(factor * 360)));
                  double sinQuad = Math.sin(Math.toRadians((double)(age * 2.5F + (float)(i * (j + 1)))) * (double)amplitude) / (double)shaking;
                  MatrixStack matrices = new MatrixStack();
                  matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + 180.0F));
                  matrices.translate(
                     tX + Math.cos(radians) * (double)target.getWidth(), tY + 1.0 + sinQuad, tZ + Math.sin(radians) * (double)target.getWidth()
                  );
                  matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
                  matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                  Matrix4f matrix = matrices.peek().getPositionMatrix();
                  int baseColor;
                  if (hitProgress > 0.0F) {
                     baseColor = Color.RED.getRGB();
                  } else {
                     baseColor = ColorUtil.getColorStyle((float)((int)(180.0F * offset)));
                  }

                  int color = RenderUtil.applyOpacity(baseColor, offset * ghostAlpha);
                  float scale = this.SCALE_CACHE[Math.min((int)(offset * 100.0F), 100)];
                  buffer.vertex(matrix, -scale, scale, 0.0F).texture(0.0F, 1.0F).color(color);
                  buffer.vertex(matrix, scale, scale, 0.0F).texture(1.0F, 1.0F).color(color);
                  buffer.vertex(matrix, scale, -scale, 0.0F).texture(1.0F, 0.0F).color(color);
                  buffer.vertex(matrix, -scale, -scale, 0.0F).texture(0.0F, 0.0F).color(color);
               }
            }

            RenderUtil.render3D.endBuilding(buffer);
            if (canSee) {
               RenderSystem.depthMask(true);
               RenderSystem.disableDepthTest();
            } else {
               RenderSystem.enableDepthTest();
            }

            RenderSystem.disableBlend();
         }
      }
   }

   private void cicle(Entity target, MatrixStack matrices, float tickDelta) {
      Vec3d camPos = mc.gameRenderer.getCamera().getPos();
      double x = MathHelper.lerp((double)tickDelta, target.lastRenderX, target.getX()) - camPos.x;
      double z = MathHelper.lerp((double)tickDelta, target.lastRenderZ, target.getZ()) - camPos.z;
      double y = MathHelper.lerp((double)tickDelta, target.lastRenderY, target.getY())
         - camPos.y
         + Math.min(Math.sin((double)System.currentTimeMillis() / 400.0) + 0.95, (double)target.getHeight());
      disableDepth();
      RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      Matrix4f matrix = matrices.peek().getPositionMatrix();
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
      int baseColor = ColorUtil.getColorStyle(360.0F);
      float r = (float)(baseColor >> 16 & 0xFF) / 255.0F;
      float g = (float)(baseColor >> 8 & 0xFF) / 255.0F;
      float b = (float)(baseColor & 0xFF) / 255.0F;
      float alpha = (float)this.animation.getOutput();
      float radius = target.getWidth() * 0.8F;

      for (float i = 0.0F; (double)i <= 6.440264939859076; i = (float)((double)i + (Math.PI / 20))) {
         double vecX = x + (double)radius * Math.cos((double)i);
         double vecZ = z + (double)radius * Math.sin((double)i);
         buffer.vertex(matrix, (float)vecX, (float)(y - Math.cos((double)System.currentTimeMillis() / 400.0) / 2.0), (float)vecZ)
            .color(r, g, b, 0.01F * alpha);
         buffer.vertex(matrix, (float)vecX, (float)y, (float)vecZ).color(r, g, b, 1.0F * alpha);
      }

      RenderUtil.render3D.endBuilding(buffer);
      endRender();
   }

   private static void disableDepth() {
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
   }

   private static void endRender() {
      RenderUtil.disableRender();
      RenderSystem.enableDepthTest();
      RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void render(Entity target) {
      Camera camera = mc.gameRenderer.getCamera();
      if (camera != null) {
         this.scale = this.animation.getOutput();
         if (this.scale != 0.0) {
            float delta = mc.getRenderTickCounter().getTickDelta(true);
            float hitProgress = RayTraceUtil.getHitProgress(target);
            Vec3d camPos = camera.getPos();
            double tX = MathUtil.interpolate(target.prevX, target.getX(), (double)delta) - camPos.x;
            double tY = MathUtil.interpolate(target.prevY, target.getY(), (double)delta) - camPos.y;
            double tZ = MathUtil.interpolate(target.prevZ, target.getZ(), (double)delta) - camPos.z;
            MatrixStack matrices = this.setupMatrices(camera, target, delta, tX, tY, tZ);
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            disableDepth();
            RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
            if (this.mode.is("Isaretci")) {
               RenderSystem.setShaderTexture(0, ResourceProvider.marker);
            }

            if (this.mode.is("Isaretci2")) {
               RenderSystem.setShaderTexture(0, ResourceProvider.marker2);
            }

            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            float alpha = (float)this.animation.getOutput();
            int[] baseColors = hitProgress > 0.0F
               ? new int[]{Color.RED.getRGB(), ColorUtil.getColorStyle(0.0F), Color.RED.getRGB(), ColorUtil.getColorStyle(270.0F)}
               : new int[]{ColorUtil.getColorStyle(90.0F), ColorUtil.getColorStyle(0.0F), ColorUtil.getColorStyle(180.0F), ColorUtil.getColorStyle(270.0F)};
            this.drawQuad(matrix, this.applyAlphaToColors(baseColors, alpha));
            endRender();
         }
      }
   }

   private MatrixStack setupMatrices(Camera camera, Entity target, float delta, double tX, double tY, double tZ) {
      MatrixStack matrices = new MatrixStack();
      float pitch = camera.getPitch();
      float yaw = camera.getYaw();
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + 180.0F));
      matrices.translate(tX, tY + (double)(target.getEyeHeight(target.getPose()) / 2.0F), tZ);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
      matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
      float interpolatedAngle = MathUtil.interpolateFloat(1.0F, 1.0F, delta);
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(interpolatedAngle));
      float radians = (float)Math.toRadians((double)((float)(System.currentTimeMillis() % 3600L) / 5.0F));
      matrices.multiplyPositionMatrix(new Matrix4f().rotate(radians, 0.0F, 0.0F, 1.0F));
      matrices.translate(-0.75, -0.75, -0.01);
      return matrices;
   }

   private int[] applyAlphaToColors(int[] colors, float alpha) {
      int[] out = new int[colors.length];

      for (int i = 0; i < colors.length; i++) {
         Color color = new Color(colors[i]);
         out[i] = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * alpha)).getRGB();
      }

      return out;
   }

   private void drawQuad(Matrix4f matrix, int[] colors) {
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      buffer.vertex(matrix, 0.0F, 1.5F, 0.0F).texture(0.0F, 1.0F).color(colors[0]);
      buffer.vertex(matrix, 1.5F, 1.5F, 0.0F).texture(1.0F, 1.0F).color(colors[1]);
      buffer.vertex(matrix, 1.5F, 0.0F, 0.0F).texture(1.0F, 0.0F).color(colors[2]);
      buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).texture(0.0F, 0.0F).color(colors[3]);
      RenderUtil.render3D.endBuilding(buffer);
   }
}
