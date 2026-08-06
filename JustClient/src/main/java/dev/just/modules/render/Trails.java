package dev.just.modules.render;

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
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Trails",
   type = Type.Render,
   desc = "QXJrYW7EsXpkYSDFn8SxayBiaXIgaXogYsSxcmFrxLFy"
)
public class Trails extends Function {
   private final MultiSetting targets = new MultiSetting(I0O1l0I1.b("xZ51cmFkYSBHw7ZzdGVy"), Arrays.asList(I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVu")), new String[]{I0O1l0I1.b("T3l1bmN1bGFy"), I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVu")});

   private static final int MAX_TRAILS_PER_ENTITY = 50;

   public Trails() {
      this.addSettings(new Setting[]{this.targets});
   }

   @Override
   protected void onDisable() {
      if (Manager.SYNC_MANAGER != null) {
         for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
            try {
               List<Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();
               if (trails != null) trails.clear();
            } catch (Exception ignored) {}
         }
      }
      super.onDisable();
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         long now = System.currentTimeMillis();
         for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
            if (this.shouldRenderTrails(entity)) {
               List<Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();
               trails.removeIf(t -> t.isExpired(now));
            }
         }
      } else if (event instanceof EventRender3D render3D) {
         handleRender(render3D);
      }
   }

   private void handleRender(EventRender3D renderEvent) {
      long now = System.currentTimeMillis();
      float tickDelta = renderEvent.getDeltatick().getTickDelta(true);
      Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

      for (PlayerEntity entity : Manager.SYNC_MANAGER.getPlayers()) {
         if (this.shouldRenderTrails(entity)) {
            Vec3d interp = this.interpolateEntityPosition(entity, tickDelta);
            List<Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();

            if (trails.isEmpty()) {
               trails.add(new Trail(interp, this.getTrailColor(entity), now));
            } else {
               Trail last = trails.get(trails.size() - 1);
               if (last.pos.distanceTo(interp) >= 0.01) {
                  if (trails.size() >= MAX_TRAILS_PER_ENTITY) {
                     trails.remove(0);
                  }
                  trails.add(new Trail(interp, this.getTrailColor(entity), now));
               }
            }

            this.render(renderEvent, entity, cameraPos, now);
         }
      }
   }

   private int getTrailColor(PlayerEntity entity) {
      return Manager.FRIEND_MANAGER.isFriend(entity.getName().getString()) ? new Color(0, 255, 0).getRGB() : ColorUtil.getColorStyle(360.0F);
   }

   private boolean shouldRenderTrails(PlayerEntity entity) {
      if (entity == mc.player) {
         return mc.options.getPerspective() != Perspective.FIRST_PERSON && this.targets.get(I0O1l0I1.b("QmVu"));
      } else {
         return this.targets.get(I0O1l0I1.b("QXJrYWRhxZ9sYXI=")) && Manager.FRIEND_MANAGER.isFriend(entity.getName().getString()) ? true : this.targets.get(I0O1l0I1.b("T3l1bmN1bGFy"));
      }
   }

   private Vec3d interpolateEntityPosition(PlayerEntity entity, float tickDelta) {
      double ix = entity.prevX + (entity.getX() - entity.prevX) * (double)tickDelta;
      double iy = entity.prevY + (entity.getY() - entity.prevY) * (double)tickDelta;
      double iz = entity.prevZ + (entity.getZ() - entity.prevZ) * (double)tickDelta;
      return new Vec3d(ix, iy, iz);
   }

   private void render(EventRender3D event, PlayerEntity entity, Vec3d cameraPos, long now) {
      List<Trail> trails = ((IEntity)entity).justClientFabric1_21_4$getTrails();
      if (trails.isEmpty()) return;

      float playerHeight = entity.getHeight();
      event.getMatrixStack().push();
      RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

      for (int i = 0; i < trails.size(); i++) {
         Trail trail = trails.get(i);
         float age = (float)(now - trail.time) / 250.0F;
         float alpha = Math.max(0.0F, 1.0F - age) * 0.6F;
         if (alpha <= 0.0F) continue;

         Vec3d pos = trail.pos;
         float x = (float)(pos.x - cameraPos.x);
         float y = (float)(pos.y - cameraPos.y);
         float z = (float)(pos.z - cameraPos.z);

         int color = trail.color;
         int r = (color >> 16) & 0xFF;
         int g = (color >> 8) & 0xFF;
         int b = color & 0xFF;
         int a = (int)(alpha * 255);

         buffer.vertex(event.getMatrixStack().peek().getPositionMatrix(), x, y, z).color(r, g, b, a);
         buffer.vertex(event.getMatrixStack().peek().getPositionMatrix(), x, y + playerHeight, z).color(r, g, b, a / 2);
      }

      RenderUtil.render3D.endBuilding(buffer);
      RenderUtil.disableRender();
      RenderSystem.disableDepthTest();
      event.getMatrixStack().pop();
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
