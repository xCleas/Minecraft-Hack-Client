package dev.just.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.util.animations.Animation;
import dev.just.util.animations.impl.EaseBackIn;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.player.TimerUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction.AxisDirection;
import net.minecraft.client.render.VertexFormat;
import org.joml.Matrix4f;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Breadcrumbs",
   type = Type.Render,
   desc = "SGFyZWtldCBlZGVya2VuIHllcmRlIGVzdGV0aWsgaGFsa2FsYXIgb2x1xZ90dXJ1cg=="
)
public class Breadcrumbs extends Function {
   private final Identifier IMAGE = Identifier.of("justclient", "images/circles/circles5.png");
   private final List<Breadcrumbs.Circle> circles = new ArrayList<>();
   private final Map<PlayerEntity, TimerUtil> spawnTimers = new HashMap<>();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private static final int MAX_CIRCLES = 200;
   private volatile long entropy = System.nanoTime();

   @Override
   protected void onDisable() {
      this.circles.clear();
      this.spawnTimers.clear();
      super.onDisable();
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 6;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 7);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  handleUpdateInternal();
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender3D render3D) {
                  this.render(render3D);
               }
               _s = 6;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
               _s = 6;
               break;

            case 5:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy = lO1I0l1O.l(entropy) ^ FAKE_STATE;
               }
               _s = 6;
               break;

            case 6:
               return;

            default:
               _s = 6;
               break;
         }
      }
   }

   private void handleUpdateInternal() {
      l1O0I1lO.fakeHandler();
      this.circles.removeIf(c -> c.timer.getTime() > lO1I0l1O.l(8000L));
      Vec3d velocity = mc.player.getVelocity();
      boolean isMoving = velocity.x * velocity.x + velocity.z * velocity.z > lO1I0l1O.d(0.001);
      TimerUtil spawnTimer = this.spawnTimers.computeIfAbsent(mc.player, p -> new TimerUtil());
      if (l1O0I1lO.opaqueTrue() && isMoving && mc.player.isOnGround() && spawnTimer.hasTimeElapsed(lO1I0l1O.l(150L)) && this.circles.size() < MAX_CIRCLES) {
         spawnTimer.reset();
         Vec3d spawnPos = new Vec3d(mc.player.getX(), Math.floor(mc.player.getY()) + lO1I0l1O.d(0.001), mc.player.getZ());
         Breadcrumbs.Circle circle = new Breadcrumbs.Circle(spawnPos, new TimerUtil(), new EaseBackIn(lO1I0l1O.i(400), 1.0, lO1I0l1O.f(1.3F)));
         circle.animation.setDirection(AxisDirection.POSITIVE);
         circle.yaw = getYawFromVelocity(velocity);
         this.circles.add(circle);
      }
   }

   private static float getYawFromVelocity(Vec3d velocity) {
      if (velocity.lengthSquared() < 1.0E-4) {
         return 0.0F;
      } else {
         double dx = velocity.x;
         double dz = velocity.z;
         return (float)(-(Math.atan2(dx, dz) * (180.0 / Math.PI)));
      }
   }

   private void render(EventRender3D eventRender3D) {
      Collections.reverse(this.circles);
      eventRender3D.getMatrixStack().push();
      RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.setShaderTexture(0, this.IMAGE);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (Breadcrumbs.Circle c : this.circles) {
         float elapsed = (float)c.timer.getTime();
         float alphaFade = MathUtil.clamp(1.0F - elapsed / 1200.0F, 0.0F, 1.0F);
         float animScale = (float)c.animation.getOutput();
         eventRender3D.getMatrixStack().push();
         eventRender3D.getMatrixStack()
            .translate(
               c.pos.x - mc.getEntityRenderDispatcher().camera.getPos().getX(),
               c.pos.y - mc.getEntityRenderDispatcher().camera.getPos().getY(),
               c.pos.z - mc.getEntityRenderDispatcher().camera.getPos().getZ()
            );
         eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
         eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(c.yaw - 180.0F));
         RenderAddon.sizeAnimation(eventRender3D.getMatrixStack(), 0.0, 0.0, (double)(animScale * 0.45F));
         float size = 1.0F;
         Matrix4f matrix = eventRender3D.getMatrixStack().peek().getPositionMatrix();
         buffer.vertex(matrix, -size, size, 0.0F)
            .texture(0.0F, 1.0F)
            .color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(270.0F), alphaFade));
         buffer.vertex(matrix, size - 0.3F, size, 0.0F)
            .texture(1.0F, 1.0F)
            .color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(0.0F), alphaFade));
         buffer.vertex(matrix, size - 0.3F, -size, 0.0F)
            .texture(1.0F, 0.0F)
            .color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(180.0F), alphaFade));
         buffer.vertex(matrix, -size, -size, 0.0F)
            .texture(0.0F, 0.0F)
            .color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(90.0F), alphaFade));
         eventRender3D.getMatrixStack().pop();
      }

      RenderUtil.render3D.endBuilding(buffer);
      RenderSystem.depthMask(true);
      RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      eventRender3D.getMatrixStack().pop();
      Collections.reverse(this.circles);
   }

   static class Circle {
      private final Vec3d pos;
      private final TimerUtil timer;
      private final Animation animation;
      private float yaw;

      public Circle(Vec3d pos, TimerUtil timer, Animation animation) {
         this.pos = pos;
         this.timer = timer;
         this.animation = animation;
      }

      public Vec3d pos() {
         return this.pos;
      }

      public TimerUtil timer() {
         return this.timer;
      }

      public Animation animation() {
         return this.animation;
      }

      public float yaw() {
         return this.yaw;
      }
   }
}
