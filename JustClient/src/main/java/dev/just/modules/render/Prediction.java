package dev.just.modules.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.just.events.Event;
import dev.just.events.impl.render.EventRender2D;
import dev.just.events.impl.render.EventRender3D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import dev.just.util.shader.ShaderManager;
import dev.just.util.vector.VectorUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.world.RaycastContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Prediction",
   type = Type.Render,
   desc = "RW5kZXIgaW5jaXNpbmluIGTDvMWfZWNlxJ9pIHlvbHUgdmUgc8O8cmV5aSB0YWhtaW4gZWRlcg=="
)
public class Prediction extends Function {
   private final BooleanSetting box = new BooleanSetting(I0O1l0I1.b("S3V0dSDDh2l6"), false);
   private final BooleanSetting rect = new BooleanSetting(I0O1l0I1.b("xLBuY2kgQWx0xLEgUGFuZWw="), false);
   private static final ItemStack ENDER_PEARL_STACK = new ItemStack(Items.ENDER_PEARL);
   private static final Color BOX_COLOR = new Color(255, 255, 255, 255);
   private static final int MAX_STEPS = 150;
   private static final float FADE_LEN = 6.0F;
   private final List<Prediction.PearlPoint> pearlPoints = new ArrayList<>();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Prediction() {
      this.addSettings(new Setting[]{this.box, this.rect});
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
               if (event instanceof EventRender2D render2D) {
                  handleRender2D(render2D);
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender3D e3d) {
                  this.renderTrajectories(e3d);
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

   private void handleRender2D(EventRender2D render2D) {
      l1O0I1lO.fakeHandler();
      for (Prediction.PearlPoint pearlPoint : this.pearlPoints) {
         Vector3d projection = VectorUtil.toScreen(pearlPoint.position.x, pearlPoint.position.y - lO1I0l1O.f(0.3F), pearlPoint.position.z);
         if (l1O0I1lO.opaqueTrue() && projection != null && !(projection.z < 0.0)) {
            double time = (double)pearlPoint.ticks * lO1I0l1O.d(0.05);
            String text = String.format("%.1f sn", time);
            float fontHeight = FontUtils.durman[15].getHeight();
            float textWidth = FontUtils.durman[15].getWidth(text);
            float paddingX = lO1I0l1O.f(3.0F);
            float bgWidth = textWidth + paddingX * lO1I0l1O.f(2.0F);
            float bgHeight = fontHeight + lO1I0l1O.f(1.0F);
            float centerX = (float)projection.x;
            float centerY = (float)projection.y;
            float bgX = centerX - bgWidth / lO1I0l1O.f(2.0F);
            RenderUtil.drawRoundedRect(render2D.getMatrixStack(), bgX, centerY, bgWidth, bgHeight, lO1I0l1O.f(2.0F), -1308227822);
            float textX = centerX - textWidth / lO1I0l1O.f(2.0F);
            float textY = centerY + (bgHeight - fontHeight) / lO1I0l1O.f(2.0F);
            FontUtils.durman[15].drawLeftAligned(render2D.getDrawContext().getMatrices(), text, textX, textY, -1);
            float pearlSize = lO1I0l1O.f(11.0F);
            float pearlX = centerX - pearlSize / lO1I0l1O.f(2.0F);
            float pearlY = centerY - pearlSize - lO1I0l1O.f(2.0F);
            if (this.rect.get()) {
               RenderUtil.drawRoundedRect(render2D.getMatrixStack(), pearlX - lO1I0l1O.f(0.5F), pearlY - lO1I0l1O.f(0.2F), lO1I0l1O.f(12.0F), lO1I0l1O.f(12.0F), lO1I0l1O.f(2.0F), -1308227822);
            }

            RenderAddon.renderItem(render2D.getDrawContext(), ENDER_PEARL_STACK, pearlX, pearlY, pearlSize / lO1I0l1O.f(16.0F), false);
         }
      }
   }

   private void renderTrajectories(EventRender3D event) {
      MatrixStack stack = event.getMatrixStack();
      Vec3d cameraPos = mc.getEntityRenderDispatcher().camera.getPos();
      stack.push();
      stack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
      GL11.glEnable(2881);
      RenderSystem.disableDepthTest();
      RenderSystem.disableCull();
      RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
      RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
      RenderSystem.lineWidth(3.0F);
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
      this.pearlPoints.clear();

      for (Entity entity : Manager.SYNC_MANAGER.getEntities()) {
         if (entity instanceof EnderPearlEntity enderPearlEntity) {
            this.simulatePearl(stack, buffer, enderPearlEntity);
         }
      }

      RenderUtil.render3D.endBuilding(buffer);
      if (this.box.get()) {
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

         for (Prediction.PearlPoint pearlPoint : this.pearlPoints) {
            Vec3d pos = pearlPoint.position;
            Box outlineBox = new Box(
               pos.x - 0.15, pos.y - 0.15, pos.z - 0.15, pos.x + 0.15, pos.y + 0.15, pos.z + 0.15
            );
            RenderUtil.render3D.drawHoleOutline(outlineBox, BOX_COLOR.getRGB(), 1.0F);
         }
      }

      RenderUtil.disableRender();
      RenderSystem.enableDepthTest();
      RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2881);
      stack.pop();
   }

   private void simulatePearl(MatrixStack stack, BufferBuilder buffer, EnderPearlEntity pearl) {
      Vec3d motion = pearl.getVelocity();
      Vec3d pos = pearl.getPos();
      int ticks = 0;
      float dist = 0.0F;
      int baseRGB = ColorUtil.getColorStyle(360.0F) & 16777215;

      for (int i = 0; i < 150; i++) {
         Vec3d prevPos = pos;
         pos = pos.add(motion);
         motion = this.getNextMotion(pearl, prevPos, motion);
         HitResult hitResult = mc.world.raycast(new RaycastContext(prevPos, pos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, pearl));
         if (hitResult.getType() == HitResult.Type.BLOCK) {
            pos = hitResult.getPos();
         }

         float segLen = (float)prevPos.distanceTo(pos);
         float a1 = MathUtil.smoothstep(0.0F, 6.0F, dist);
         float a2 = MathUtil.smoothstep(0.0F, 6.0F, dist + segLen);
         int c1 = ColorUtil.withAlpha(baseRGB, (float)((int)(255.0F * a1)));
         int c2 = ColorUtil.withAlpha(baseRGB, (float)((int)(255.0F * a2)));
         this.vertexLineGradient(
            stack,
            buffer,
            (float)prevPos.x,
            (float)prevPos.y,
            (float)prevPos.z,
            (float)pos.x,
            (float)pos.y,
            (float)pos.z,
            c1,
            c2
         );
         dist += segLen;
         if (hitResult.getType() == HitResult.Type.BLOCK || pos.y < -128.0) {
            this.pearlPoints.add(new Prediction.PearlPoint(pos, ticks));
            break;
         }

         ticks++;
      }
   }

   private void vertexLineGradient(MatrixStack matrices, VertexConsumer buffer, float x1, float y1, float z1, float x2, float y2, float z2, int color1, int color2) {
      Matrix4f model = matrices.peek().getPositionMatrix();
      float[] col1 = ColorUtil.rgba(color1);
      float[] col2 = ColorUtil.rgba(color2);
      Vector3f normalVec = ShaderManager.getNormal(x1, y1, z1, x2, y2, z2);
      buffer.vertex(model, x1, y1, z1)
         .color(col1[0], col1[1], col1[2], col1[3])
         .normal(matrices.peek(), normalVec.x(), normalVec.y(), normalVec.z());
      buffer.vertex(model, x2, y2, z2)
         .color(col2[0], col2[1], col2[2], col2[3])
         .normal(matrices.peek(), normalVec.x(), normalVec.y(), normalVec.z());
   }

   private Vec3d getNextMotion(ThrownEntity throwable, Vec3d prevPos, Vec3d motion) {
      boolean isInWater = mc.world.getBlockState(BlockPos.ofFloored(prevPos)).getFluidState().isIn(FluidTags.WATER);
      motion = motion.multiply(isInWater ? 0.8 : 0.99);
      if (!throwable.hasNoGravity()) {
         motion = motion.add(0.0, -0.03F, 0.0);
      }

      return motion;
   }

   static record PearlPoint(Vec3d position, int ticks) {
   }
}
