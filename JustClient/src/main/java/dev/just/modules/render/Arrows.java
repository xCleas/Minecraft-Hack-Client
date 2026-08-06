package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.events.impl.render.EventRender2D;
import dev.just.manager.Manager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.BooleanSetting;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector2f;
import dev.just.protect.runtime.I0O1l0I1;

@FunctionAnnotation(
   name = "Arrows",
   desc = "RWtyYW7EsW4gb3J0YXPEsW5kYSBveXVuY3VsYXLEsW4gecO2bsO8bsO8IGfDtnN0ZXJlbiBva2xhciDDp2l6ZXI=",
   type = Type.Render
)
public class Arrows extends Function {
   private final SliderSetting radius = new SliderSetting(I0O1l0I1.b("WWFyxLHDp2Fw"), 70.0, 50.0, 160.0, 1.0);
   private final BooleanSetting dynamic = new BooleanSetting(I0O1l0I1.b("RGluYW1paw=="), true);
   private float animatedRadius = this.radius.get().floatValue();
   private final Map<UUID, Float> smoothedAngles = new HashMap<>();

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile long entropy = System.nanoTime();

   public Arrows() {
      this.addSettings(new Setting[]{this.radius, this.dynamic});
   }

   @Override
   public void onEvent(Event event) {
      int _s = O1lI0O1l.next(hashCode(), 6);
      while (true) {
         switch (_s) {
            case 0:
               l1O0I1lO.fakeHandler();
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 5;
                  break;
               }
               _s = O1lI0O1l.next(hashCode(), 6);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (!(event instanceof EventRender2D)) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               handleRenderInternal((EventRender2D) event);
               _s = 5;
               break;

            case 3:
               if (l1O0I1lO.opaqueFalse()) {
                  entropy ^= event.hashCode();
                  l1O0I1lO.fakeBranch(event, entropy);
               }
               _s = 5;
               break;

            case 4:
               l1O0I1lO.fakeHandler();
               _s = 5;
               break;

            case 5:
               return;

            default:
               _s = 5;
               break;
         }
      }
   }

   private void handleRenderInternal(EventRender2D render2D) {
      l1O0I1lO.fakeHandler();
      ClientPlayerEntity player = mc.player;
      if (player == null) return;

      float targetRadius = this.radius.get().floatValue();
      if (this.dynamic.get() && player.isSprinting()) {
         targetRadius += lO1I0l1O.f(20.0F);
      }

      this.animatedRadius = this.animatedRadius + (targetRadius - this.animatedRadius) * lO1I0l1O.f(0.1F);

      for (PlayerEntity other : Manager.SYNC_MANAGER.getPlayers()) {
         if (l1O0I1lO.opaqueTrue() && !other.equals(player) && !Manager.FUNCTION_MANAGER.antiBot.check(other)) {
            this.drawArrow(render2D.getMatrixStack(), other, other.getX(), other.getZ(), ColorUtil.getColorStyle(lO1I0l1O.f(360.0F)));
         }
      }
   }

   private void drawArrow(MatrixStack stack, PlayerEntity target, double x, double z, int baseColor) {
      ClientPlayerEntity player = mc.player;
      Window window = mc.getWindow();
      int width = window.getScaledWidth();
      int height = window.getScaledHeight();
      float centerX = (float)width / 2.0F;
      float centerY = (float)height / 2.0F;
      float desiredAngle = MathHelper.wrapDegrees(this.getRotationTo(new Vector2f((float)x, (float)z)) - player.getYaw());
      float angle = this.smoothAngle(target.getUuid(), desiredAngle);
      stack.push();
      stack.translate(centerX, centerY, 0.0F);
      stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
      stack.translate(-centerX, -centerY, 0.0F);
      int color = Manager.FRIEND_MANAGER.isFriend(target.getName().getString())
         ? ColorUtil.rgba(20, 255, 20, 255)
         : (Manager.FUNCTION_MANAGER.attackAura.getTarget() == target ? ColorUtil.rgba(255, 50, 50, 255) : baseColor);
      RenderUtil.drawTexture(stack, "images/triangle.png", centerX - 7.0F, centerY - this.animatedRadius, 20.0F, 20.0F, 1.0F, color);
      stack.pop();
   }

   private float smoothAngle(UUID id, float targetAngle) {
      float prev = this.smoothedAngles.getOrDefault(id, targetAngle);
      float delta = MathHelper.wrapDegrees(targetAngle - prev);
      float factor = 0.08F;
      float result = prev + delta * factor;
      this.smoothedAngles.put(id, result);
      return result;
   }

   private float getRotationTo(Vector2f vec) {
      ClientPlayerEntity player = mc.player;
      if (player == null) {
         return 0.0F;
      } else {
         double dx = (double)vec.x - player.getX();
         double dz = (double)vec.y - player.getZ();
         return (float)(-(Math.atan2(dx, dz) * (180.0 / Math.PI)));
      }
   }
}
