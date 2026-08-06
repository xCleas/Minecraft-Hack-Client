package dev.just.modules.render;

import dev.just.JustClient;
import dev.just.events.Event;
import dev.just.events.impl.render.EventRender2D;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.dragManager.Dragging;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.themeManager.StyleManager;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.protect.runtime.O1lI0O1l;
import dev.just.protect.runtime.l1O0I1lO;
import dev.just.protect.runtime.lO1I0l1O;
import dev.just.modules.setting.SliderSetting;
import dev.just.util.animations.Animation;
import dev.just.util.animations.impl.EaseBackIn;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.player.ServerUtil;
import dev.just.util.render.RenderAddon;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.EntityHitResult;
import org.joml.Vector4f;
import org.joml.Vector4i;

@FunctionAnnotation(
   name = "TargetHUD",
   desc = "Hedef bilgilerini gosterir",
   type = Type.Render
)
public class TargetHUD extends Function {
   private final SliderSetting headRounding = new SliderSetting("Kafa Yuvarlama", 4.0, 0.0, 12.0, 1.0);

   public final Dragging drag = JustClient.getInstance().createDrag(this, "TargetHUD", 100.0F, 100.0F);
   private final Animation animation = new EaseBackIn(300, 1.0, 1.5F);

   private LivingEntity target = null;
   private float lastHealth = 0.0F;
   private float lastAbsorption = 0.0F;
   private double scale = 0.0;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public TargetHUD() {
      this.addSettings(headRounding);
      this.state = true; // Varsayılan olarak açık
   }

   @Override
   public void toggle() {
      // Kapatılamaz - her zaman açık kalır
      this.state = true;
   }

   @Override
   protected void onDisable() {
      // Kapatılamaz - hemen tekrar aç
      this.state = true;
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
               if (mc == null || mc.player == null || mc.world == null) {
                  _s = 5;
                  break;
               }
               _s = 2;
               break;

            case 2:
               if (event instanceof EventRender2D e) {
                  render(e);
               }
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

   private void render(EventRender2D e) {
      float x = drag.getX();
      float y = drag.getY();

      target = getTarget(target);
      scale = animation.getOutput();

      if (scale == 0.0 || target == null) {
         drag.setWidth(130);
         drag.setHeight(45);
         return;
      }

      float rawHealth = MathHelper.clamp(ServerUtil.getHealth(target), 0.0F, 1.0F);
      float rawAbsorption = MathHelper.clamp(target.getAbsorptionAmount(), 0.0F, 10.0F);
      lastHealth = MathUtil.fast(lastHealth, rawHealth, 8.0F);
      lastAbsorption = MathUtil.fast(lastAbsorption, rawAbsorption, 8.0F);

      String healthText = String.format(Locale.ENGLISH, "%.1f", lastHealth * 20.0F);

      e.getMatrixStack().push();
      RenderAddon.sizeAnimation(e.getMatrixStack(), (double)(x + 65.0F), (double)(y + 22.5F), scale);

      // Arka plan
      int bgColor = new Color(20, 20, 28, 200).getRGB();
      int accentColor = new Color(255, 140, 50).getRGB();

      RenderUtil.drawRoundedRect(e.getDrawContext().getMatrices(), x, y, 130, 45, 6, bgColor);
      RenderUtil.drawRoundedRect(e.getDrawContext().getMatrices(), x, y, 4, 45, new Vector4f(6, 0, 0, 6), accentColor);

      // Kafa
      RenderAddon.drawHead(e.getDrawContext().getMatrices(), target, x + 8, y + 6, 32, headRounding.get().floatValue());

      // Isim
      String name = target.getName().getString();
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.nameProtect != null) {
         name = Manager.FUNCTION_MANAGER.nameProtect.getProtectedName(name);
      }
      if (name.length() > 10) {
         name = name.substring(0, 10) + "..";
      }
      FontUtils.sf_bold[14].drawLeftAligned(e.getDrawContext().getMatrices(), name, x + 45, y + 8, Color.WHITE.getRGB());

      // Can bar
      float barWidth = 78;
      float barX = x + 45;
      float barY = y + 28;

      RenderUtil.drawRoundedRect(e.getDrawContext().getMatrices(), barX, barY, barWidth, 8, 3, new Color(40, 40, 50).getRGB());

      // Can rengi (yesil -> kirmizi)
      int healthColor = ColorUtil.interpolateColor(new Color(220, 60, 60).getRGB(), new Color(60, 220, 100).getRGB(), lastHealth);
      RenderUtil.drawRoundedRect(e.getDrawContext().getMatrices(), barX, barY, barWidth * lastHealth, 8, 3, healthColor);

      // Absorption (sari)
      if (lastAbsorption > 0) {
         float absWidth = Math.min(barWidth, barWidth * (lastAbsorption / 10.0F));
         RenderUtil.drawRoundedRect(e.getDrawContext().getMatrices(), barX, barY, absWidth, 8, 3, new Color(255, 200, 50, 150).getRGB());
      }

      // HP yazi
      FontUtils.sf_medium[11].drawLeftAligned(e.getDrawContext().getMatrices(), healthText + " HP", barX + barWidth - FontUtils.sf_medium[11].getWidth(healthText + " HP"), y + 10, new Color(150, 150, 160).getRGB());

      // Elindeki item
      List<ItemStack> items = new ArrayList<>();
      items.add(target.getMainHandStack());
      items.add(target.getOffHandStack());
      items.removeIf(i -> i.getItem() instanceof AirBlockItem || i.isEmpty());

      float itemX = x + 45;
      for (ItemStack stack : items) {
         e.getDrawContext().getMatrices().push();
         e.getDrawContext().getMatrices().translate(itemX, y + 18, 0);
         e.getDrawContext().getMatrices().scale(0.5F, 0.5F, 1);
         e.getDrawContext().drawItem(stack, 0, 0);
         e.getDrawContext().getMatrices().pop();
         itemX += 10;
      }

      e.getMatrixStack().pop();

      drag.setWidth(130);
      drag.setHeight(45);
   }

   private LivingEntity getTarget(LivingEntity current) {
      // AttackAura hedefi
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.attackAura != null
          && Manager.FUNCTION_MANAGER.attackAura.state && Manager.FUNCTION_MANAGER.attackAura.getTarget() != null) {
         animation.setDirection(net.minecraft.util.math.Direction.AxisDirection.POSITIVE);
         return Manager.FUNCTION_MANAGER.attackAura.getTarget();
      }

      // Crosshair hedefi
      if (mc.crosshairTarget instanceof EntityHitResult hit) {
         Entity e = hit.getEntity();
         if (e instanceof PlayerEntity p && p != mc.player) {
            animation.setDirection(net.minecraft.util.math.Direction.AxisDirection.POSITIVE);
            return p;
         }
      }

      // Hedef yok (sınırsız mesafe)
      if (current != null && (current.isDead() || current.getHealth() <= 0)) {
         animation.setDirection(net.minecraft.util.math.Direction.AxisDirection.NEGATIVE);
         return null;
      }

      if (current == null) {
         animation.setDirection(net.minecraft.util.math.Direction.AxisDirection.NEGATIVE);
      }

      return current;
   }
}
