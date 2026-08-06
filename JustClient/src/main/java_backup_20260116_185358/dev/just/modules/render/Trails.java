package dev.just.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.IEntity;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.VertexFormat;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "Trails",
   type = Type.Render,
   desc = "QXJrYW7EsXpkYSDFn8SxayBiaXIgaXogYsSxcmFrxLFy"
)
public class Trails extends Function {
   private final MultiSetting targets = new MultiSetting(Strings.b("xZ51cmFkYSBHw7ZzdGVy"), Arrays.asList(Strings.b("QXJrYWRhxZ9sYXI="), Strings.b("QmVu")), new String[]{Strings.b("T3l1bmN1bGFy"), Strings.b("QXJrYWRhxZ9sYXI="), Strings.b("QmVu")});
   private final long trailLifetimeMs = 250L;
   private final double minDistance = 0.01;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Trails() {
      this.addSettings(new Setting[]{this.targets});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 7);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 6;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 7);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (event instanceof EventUpdate) {
                  handleUpdateInternal();
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender3D renderEvent) {
                  handleRenderInternal(renderEvent);
               }
               _s = 6;
               break;

            case 3:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 6;
               break;

            case 4:
               FlowObfuscator.fakeHandler();
               _s = 6;
               break;

            case 5:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.l(entropy) ^ FAKE_STATE;
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
      FlowObfuscator.fakeHandler();
      long now = System.currentTimeMillis();
      for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
         if (FlowObfuscator.opaqueTrue() && this.shouldRenderTrails(entity)) {
            List<Trails.Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();
            trails.removeIf(t -> t.isExpired(now));
         }
      }
   }

   private void handleRenderInternal(EventRender3D renderEvent) {
      FlowObfuscator.fakeHandler();
      long now = System.currentTimeMillis();
      float tickDelta = renderEvent.getDeltatick().getTickDelta(true);
      Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

      for (PlayerEntity entityx : Manager.SYNC_MANAGER.getPlayers()) {
         if (FlowObfuscator.opaqueTrue() && this.shouldRenderTrails(entityx)) {
            Vec3d interp = this.interpolateEntityPosition(entityx, tickDelta);
            List<Trails.Trail> trails = ((IEntity)entityx).justClientFabric1_21_4$getTrails();
            if (trails.isEmpty()) {
               trails.add(new Trails.Trail(interp, this.getTrailColor(entityx), now));
            } else {
               Trails.Trail last = trails.get(trails.size() - NumberGuard.i(1));
               if (last.pos.distanceTo(interp) >= NumberGuard.d(0.01)) {
                  trails.add(new Trails.Trail(interp, this.getTrailColor(entityx), now));
               }
            }

            this.render(renderEvent, entityx, cameraPos, now);
         }
      }
   }

   private int getTrailColor(PlayerEntity entity) {
      return Manager.FRIEND_MANAGER.isFriend(entity.getName().getString()) ? new Color(0, 255, 0).getRGB() : ColorUtil.getColorStyle(360.0F);
   }

   private boolean shouldRenderTrails(PlayerEntity entity) {
      if (entity == mc.player) {
         return mc.options.getPerspective() == Perspective.FIRST_PERSON ? false : this.targets.get(Strings.b("QmVu"));
      } else {
         return this.targets.get(Strings.b("QXJrYWRhxZ9sYXI=")) && Manager.FRIEND_MANAGER.isFriend(entity.getName().getString()) ? true : this.targets.get(Strings.b("T3l1bmN1bGFy"));
      }
   }

   private Vec3d interpolateEntityPosition(PlayerEntity entity, float tickDelta) {
      double ix = entity.prevX + (entity.getX() - entity.prevX) * (double)tickDelta;
      double iy = entity.prevY + (entity.getY() - entity.prevY) * (double)tickDelta;
      double iz = entity.prevZ + (entity.getZ() - entity.prevZ) * (double)tickDelta;
      return new Vec3d(ix, iy, iz);
   }

   private void render(EventRender3D event, PlayerEntity entity, Vec3d cameraPos, long now) {
      List<Trails.Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();
      if (!trails.isEmpty()) {
         float playerHeight = entity.getHeight();
         event.getMatrixStack().push();
         RenderSystem.disableCull();
         RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.enableDepthTest();
         RenderSystem.depthFunc(515);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

         for (Trails.Trail p : trails) {
            if (!p.isExpired(now)) {
               float ageFrac = (float)(now - p.time) / 250.0F;
               float alpha = 1.0F - Math.min(1.0F, ageFrac);
               alpha = Math.max(0.01F, alpha);
               int color = RenderUtil.injectAlpha(p.color, (int)(alpha * 255.0F));
               Vec3d posRel = p.pos.subtract(cameraPos);
               buffer.vertex(
                     event.getMatrixStack().peek().getPositionMatrix(),
                     (float)posRel.x,
                     (float)(posRel.y + (double)playerHeight),
                     (float)posRel.z
                  )
                  .color(color);
               buffer.vertex(
                     event.getMatrixStack().peek().getPositionMatrix(), (float)posRel.x, (float)posRel.y, (float)posRel.z
                  )
                  .color(color);
            }
         }

         RenderUtil.render3D.endBuilding(buffer);
         RenderUtil.disableRender();
         RenderSystem.disableDepthTest();
         event.getMatrixStack().pop();
      }
   }

   public class Trail {
      public final Vec3d pos;
      public final int color;
      public final long time;

      public Trail(Vec3d pos, int color, long time) {
         this.pos = pos;
         this.color = color;
         this.time = time;
      }

      public boolean isExpired(long now) {
         return now - this.time > 250L;
      }
   }
}
