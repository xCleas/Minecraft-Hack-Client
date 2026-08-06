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
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.modules.setting.ModeSetting;
import dev.just.modules.setting.MultiSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.animations.Animation;
import dev.just.util.animations.impl.EaseBackIn;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.player.TimerUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import java.util.ArrayList;
import java.util.Arrays;
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
   name = "JumpCircles",
   type = Type.Render,
   desc = "WsSxcGxhbWEgYW7EsW5kYSB5ZXJkZSBlc3RldGlrIGhhbGthbGFyIG9sdcWfdHVydXI="
)
public class JumpCircles extends Function {
   private final ModeSetting circleType = new ModeSetting(I0O1l0I1.b("SGFsa2EgVGlwaQ=="), "Tip-2", "Tip-1", "Tip-2", "Tip-3");
   private final SliderSetting rotateSpeed = new SliderSetting(I0O1l0I1.b("RMO2bsO8xZ8gSMSxesSx"), 0.1F, 0.1F, 5.0, 0.1F);
   private final SliderSetting circleScale = new SliderSetting(I0O1l0I1.b("Qm95dXQ="), 0.7F, 0.6F, 5.0, 0.1F);
   private final MultiSetting targets = new MultiSetting(I0O1l0I1.b("R8O2csO8bsO8bQ=="), Arrays.asList(I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVuaQ==")), new String[]{I0O1l0I1.b("T3l1bmN1bGFy"), I0O1l0I1.b("QXJrYWRhxZ9sYXI="), I0O1l0I1.b("QmVuaQ==")});
   private final Identifier CIRCLE_TEXTURE = Identifier.of("justclient", "images/circles/circle.png");
   private final Identifier CIRCLE2_TEXTURE = Identifier.of("justclient", "images/circles/circle2.png");
   private final Identifier CIRCLE3_TEXTURE = Identifier.of("justclient", "images/circles/circle3.png");
   private final List<JumpCircles.Circle> circles = new ArrayList<>();
   private final Map<PlayerEntity, Boolean> wasOnGround = new HashMap<>();
   private Identifier texture = null;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   private static final int MAX_CIRCLES = 100;

   public JumpCircles() {
      this.addSettings(new Setting[]{this.circleType, this.targets, this.rotateSpeed, this.circleScale});
   }

   @Override
   protected void onDisable() {
      this.circles.clear();
      this.wasOnGround.clear();
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
                  this.renderCircles(render3D);
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

      for (PlayerEntity player : Manager.SYNC_MANAGER.getPlayers()) {
         if (l1O0I1lO.opaqueTrue() && player != null && !player.isRemoved()) {
            boolean isFriend = Manager.FRIEND_MANAGER != null
               && player.getName() != null
               && Manager.FRIEND_MANAGER.isFriend(player.getName().getString());
            boolean isMe = player == mc.player;
            boolean showPlayers = this.targets.get(I0O1l0I1.b("T3l1bmN1bGFy"));
            boolean showFriends = this.targets.get(I0O1l0I1.b("QXJrYWRhxZ9sYXI="));
            boolean showMe = this.targets.get(I0O1l0I1.b("QmVuaQ=="));
            boolean shouldTrack = isMe && showMe || !isMe && isFriend && showFriends || !isMe && !isFriend && showPlayers;
            if (!shouldTrack) {
               this.wasOnGround.remove(player);
            } else {
               boolean previouslyOnGround = this.wasOnGround.getOrDefault(player, true);
               boolean currentlyOnGround = player.isOnGround();
               if (previouslyOnGround && !currentlyOnGround) {
                  JumpCircles.Circle circle = new JumpCircles.Circle(
                     new Vec3d(player.getX(), Math.floor(player.getY()) + lO1I0l1O.d(0.001), player.getZ()),
                     new TimerUtil(),
                     new EaseBackIn(lO1I0l1O.i(400), 1.0, lO1I0l1O.f(1.3F))
                  );
                  circle.animation.setDirection(AxisDirection.POSITIVE);
                  this.circles.add(circle);
               }

               this.wasOnGround.put(player, currentlyOnGround);
            }
         }
      }
   }

   private void renderCircles(EventRender3D eventRender3D) {
      Collections.reverse(this.circles);
      eventRender3D.getMatrixStack().push();
      RenderUtil.enableRender(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.depthMask(false);
      String circleTypeValue = this.circleType.get();
      switch (circleTypeValue) {
         case "Tip-1":
            this.texture = this.CIRCLE_TEXTURE;
            break;
         case "Tip-2":
            this.texture = this.CIRCLE2_TEXTURE;
            break;
         case "Tip-3":
            this.texture = this.CIRCLE3_TEXTURE;
      }

      RenderSystem.setShaderTexture(0, this.texture);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      BufferBuilder buffer = IMinecraft.tessellator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      for (JumpCircles.Circle c : this.circles) {
         float elapsed = (float)c.timer.getTime();
         float alphaFade = MathUtil.clamp(1.0F - elapsed / 8000.0F, 0.0F, 1.0F);
         float animScale = (float)c.animation.getOutput();
         eventRender3D.getMatrixStack().push();
         eventRender3D.getMatrixStack()
            .translate(
               c.pos().x - mc.getEntityRenderDispatcher().camera.getPos().getX(),
               c.pos().y - mc.getEntityRenderDispatcher().camera.getPos().getY(),
               c.pos().z - mc.getEntityRenderDispatcher().camera.getPos().getZ()
            );
         eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
         eventRender3D.getMatrixStack().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(elapsed / 50.0F * this.rotateSpeed.get().floatValue()));
         RenderAddon.sizeAnimation(eventRender3D.getMatrixStack(), 0.0, 0.0, (double)(animScale * this.circleScale.get().floatValue()));
         float size = 1.0F;
         Matrix4f matrix = eventRender3D.getMatrixStack().peek().getPositionMatrix();
         buffer.vertex(matrix, -size, size, 0.0F)
            .texture(0.0F, 1.0F)
            .color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(270.0F), alphaFade));
         buffer.vertex(matrix, size, size, 0.0F).texture(1.0F, 1.0F).color(RenderUtil.applyOpacity(ColorUtil.getColorStyle(0.0F), alphaFade));
         buffer.vertex(matrix, size, -size, 0.0F)
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

   static record Circle(Vec3d pos, TimerUtil timer, Animation animation) {
   }
}
