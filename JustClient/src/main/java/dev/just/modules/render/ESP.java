package dev.just.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.render.EventRender2D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.I1lO0l1I;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.protect.runtime.I0O1l0I1;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexFormat;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;

@FunctionAnnotation(
   name = "ESP",
   desc = "T3l1bmN1bGFyxLFuIGV0cmFmxLFuYSBlc3RldGlrIDJEIGt1dHVsYXIgw6dpemVy",
   type = Type.Render
)
public class ESP extends Function {
   private final MultiSetting targets = new MultiSetting(
      I0O1l0I1.b("R8O2csO8bsO8bQ=="), Arrays.asList(I0O1l0I1.b("T3l1bmN1bGFy"), I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVuaQ==")), new String[]{I0O1l0I1.b("T3l1bmN1bGFy"), I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVuaQ=="), I0O1l0I1.b("RcWfeWFsYXI=")}
   );

   public ESP() {
      this.addSettings(new Setting[]{this.targets});
   }

   @Override
   public void onEvent(Event event) {
      if (event instanceof EventRender2D e) {
         if (!mc.options.hudHidden) {
            Matrix4f matrix = e.getDrawContext().getMatrices().peek().getPositionMatrix();
            RenderUtil.enableRender();
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            List<AbstractClientPlayerEntity> players = Manager.SYNC_MANAGER.getPlayers();
            List<Entity> entities = this.targets.get(I0O1l0I1.b("RcWfeWFsYXI=")) ? Manager.SYNC_MANAGER.getEntities() : List.of();

            for (PlayerEntity player : players) {
               if (this.shouldRender(player)) {
                  this.drawBox(e.getDeltatick(), buffer, player, matrix);
               }
            }

            for (Entity entity : entities) {
               if (entity instanceof ItemEntity) {
                  this.drawBox(e.getDeltatick(), buffer, entity, matrix);
               }
            }

            RenderUtil.render3D.endBuilding(buffer);
            RenderUtil.disableRender();
         }
      }
   }

   private boolean shouldRender(PlayerEntity entity) {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return entity.age % lO1I0l1O.i(2) == 0;
      }

      int renderScore = evaluateRenderTarget(entity);
      return I1lO0l1I.threshold(renderScore, lO1I0l1O.i(1));
   }

   private int evaluateRenderTarget(PlayerEntity entity) {
      l1O0I1lO.fakeHandler();

      // Self check
      if (entity == mc.player) {
         return checkSelfRender();
      }

      // Friend check
      if (checkFriendRender(entity)) {
         return lO1I0l1O.i(2);
      }

      // Player check
      return this.targets.get(I0O1l0I1.b("T3l1bmN1bGFy")) ? lO1I0l1O.i(1) : 0;
   }

   private int checkSelfRender() {
      l1O0I1lO.fakeHandler();

      if (l1O0I1lO.opaqueFalse()) {
         return lO1I0l1O.i(99);
      }

      boolean firstPerson = mc.options.getPerspective() == Perspective.FIRST_PERSON;
      if (firstPerson) return 0;

      return this.targets.get(I0O1l0I1.b("QmVuaQ==")) ? lO1I0l1O.i(1) : 0;
   }

   private boolean checkFriendRender(PlayerEntity entity) {
      l1O0I1lO.fakeHandler();

      return I1lO0l1I.and(
         this.targets.get(I0O1l0I1.b("QXJrYWRhxZ9sYXI=")),
         Manager.FRIEND_MANAGER.isFriend(entity.getName().getString())
      );
   }

   public void drawBox(RenderTickCounter tick, BufferBuilder buffer, @NotNull Entity ent, Matrix4f matrix) {
      Vec3d[] corners = this.getVectors(tick, ent);
      Vector4d pos = null;

      for (Vec3d corner : corners) {
         Vec3d screen = RenderUtil.render3D.worldSpaceToScreenSpace(corner);
         if (!(screen.z <= 0.0) && !(screen.z >= 1.0)) {
            if (pos == null) {
               pos = new Vector4d(screen.x, screen.y, screen.x, screen.y);
            } else {
               if (screen.x < pos.x) {
                  pos.x = screen.x;
               }

               if (screen.y < pos.y) {
                  pos.y = screen.y;
               }

               if (screen.x > pos.z) {
                  pos.z = screen.x;
               }

               if (screen.y > pos.w) {
                  pos.w = screen.y;
               }
            }
         }
      }

      if (pos != null) {
         double screenW = (double)mc.getWindow().getScaledWidth();
         double screenH = (double)mc.getWindow().getScaledHeight();
         if (!(pos.z < 0.0) && !(pos.x > screenW) && !(pos.w < 0.0) && !(pos.y > screenH)) {
            float x1 = (float)pos.x;
            float y1 = (float)pos.y;
            float x2 = (float)pos.z;
            float y2 = (float)pos.w;
            int black = Color.BLACK.getRGB();
            this.drawRect(buffer, matrix, x1 - 1.0F, y1, x1 + 0.5F, y2 + 0.5F, black);
            this.drawRect(buffer, matrix, x1 - 1.0F, y1 - 0.5F, x2 + 0.5F, y1 + 1.0F, black);
            this.drawRect(buffer, matrix, x2 - 1.0F, y1, x2 + 0.5F, y2 + 0.5F, black);
            this.drawRect(buffer, matrix, x1 - 1.0F, y2 - 1.0F, x2 + 0.5F, y2 + 0.5F, black);
            int cTop = ColorUtil.getColorStyle(270.0F);
            int cRight = ColorUtil.getColorStyle(90.0F);
            int cBottom = ColorUtil.getColorStyle(180.0F);
            int cLeft = ColorUtil.getColorStyle(0.0F);
            this.drawRect(buffer, matrix, x1 - 0.5F, y1, x1 + 0.5F, y2, cTop, cLeft, cLeft, cTop);
            this.drawRect(buffer, matrix, x1, y2 - 0.5F, x2, y2, cLeft, cBottom, cBottom, cLeft);
            this.drawRect(buffer, matrix, x1 - 0.5F, y1, x2, y1 + 0.5F, cBottom, cRight, cRight, cBottom);
            this.drawRect(buffer, matrix, x2 - 0.5F, y1, x2, y2, cRight, cTop, cTop, cRight);
         }
      }
   }

   private void drawRect(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float x2, float y2, int c1) {
      buffer.vertex(matrix, x1, y2, 0.0F).color(c1);
      buffer.vertex(matrix, x2, y2, 0.0F).color(c1);
      buffer.vertex(matrix, x2, y1, 0.0F).color(c1);
      buffer.vertex(matrix, x1, y1, 0.0F).color(c1);
   }

   private void drawRect(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float x2, float y2, int c1, int c2, int c3, int c4) {
      buffer.vertex(matrix, x1, y2, 0.0F).color(c1);
      buffer.vertex(matrix, x2, y2, 0.0F).color(c2);
      buffer.vertex(matrix, x2, y1, 0.0F).color(c3);
      buffer.vertex(matrix, x1, y1, 0.0F).color(c4);
   }

   @NotNull
   private Vec3d[] getVectors(RenderTickCounter tick, @NotNull Entity ent) {
      double x = ent.prevX + (ent.getX() - ent.prevX) * (double)tick.getTickDelta(true);
      double y = ent.prevY + (ent.getY() - ent.prevY) * (double)tick.getTickDelta(true);
      double z = ent.prevZ + (ent.getZ() - ent.prevZ) * (double)tick.getTickDelta(true);
      Box bb = ent.getBoundingBox();
      double dx = bb.minX - ent.getX() + x;
      double dy = bb.minY - ent.getY() + y;
      double dz = bb.minZ - ent.getZ() + z;
      double dx2 = bb.maxX - ent.getX() + x;
      double dy2 = bb.maxY - ent.getY() + y;
      double dz2 = bb.maxZ - ent.getZ() + z;
      return new Vec3d[]{
         new Vec3d(dx - 0.05, dy, dz - 0.05),
         new Vec3d(dx - 0.05, dy2 + 0.15, dz - 0.05),
         new Vec3d(dx2 + 0.05, dy, dz - 0.05),
         new Vec3d(dx2 + 0.05, dy2 + 0.15, dz - 0.05),
         new Vec3d(dx - 0.05, dy, dz2 + 0.05),
         new Vec3d(dx - 0.05, dy2 + 0.15, dz2 + 0.05),
         new Vec3d(dx2 + 0.05, dy, dz2 + 0.05),
         new Vec3d(dx2 + 0.05, dy2 + 0.15, dz2 + 0.05)
      };
   }
}
