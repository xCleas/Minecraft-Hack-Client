package dev.just.modules.render;

import dev.just.events.Event;
import dev.just.modules.Function;
import dev.just.modules.FunctionAnnotation;
import dev.just.modules.Type;
import dev.just.modules.setting.Setting;
import dev.just.modules.setting.SliderSetting;
import dev.just.protect.runtime.ControlFlow;
import dev.just.protect.runtime.FlowObfuscator;
import dev.just.protect.runtime.NumberGuard;
import dev.just.util.color.ColorUtil;
import dev.just.util.math.MathUtil;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.hit.EntityHitResult;
import dev.just.protect.runtime.Strings;

@FunctionAnnotation(
   name = "CrossHair",
   type = Type.Render,
   desc = "w5Z6ZWxsZcWfdGlyaWxlYmlsaXIgdmUgZGluYW1payBuacWfYW5nYWg="
)
public class CrossHair extends Function {
   private final SliderSetting attackSetting = new SliderSetting(Strings.b("VnVydcWfIEdlbmnFn2xlbWVzaQ=="), 6.0, 0.0, 20.0, 1.0);
   private final SliderSetting indentSetting = new SliderSetting(Strings.b("TWVya2V6ZSBZYWvEsW5sxLFr"), 2.0, 0.0, 5.0, 1.0);
   private final SliderSetting size1Setting = new SliderSetting(Strings.b("w4dpemkgVXp1bmx1xJ91"), 6.0, 2.0, 10.0, 1.0);
   private final SliderSetting size2Setting = new SliderSetting(Strings.b("w4dpemkgS2FsxLFubMSxxJ/EsQ=="), 2.0, 2.0, 4.0, 1.0);
   private float red = 0.0F;

   private static final int FAKE_STATE = 0xDEAD ^ 0xBEEF;
   private volatile int entropy = (int) System.nanoTime();

   public CrossHair() {
      this.addSettings(new Setting[]{this.attackSetting, this.indentSetting, this.size1Setting, this.size2Setting});
   }

   @Override
   public void onEvent(Event event) {
      int _s = ControlFlow.next(hashCode(), 5);
      while (true) {
         switch (_s) {
            case 0:
               FlowObfuscator.fakeHandler();
               if (FlowObfuscator.opaqueFalse()) {
                  entropy ^= FAKE_STATE;
                  _s = 4;
                  break;
               }
               _s = ControlFlow.next(hashCode(), 5);
               if (_s == 0) _s = 1;
               break;

            case 1:
               if (FlowObfuscator.opaqueFalse()) {
                  entropy = NumberGuard.c(entropy, event.hashCode());
               }
               _s = 4;
               break;

            case 2:
               if (FlowObfuscator.opaqueFalse()) {
                  FlowObfuscator.fakeBranch(event, entropy);
               }
               _s = 4;
               break;

            case 3:
               FlowObfuscator.fakeHandler();
               _s = 4;
               break;

            case 4:
               return;

            default:
               _s = 4;
               break;
         }
      }
   }

   public void render(DrawContext drawContext) {
      this.red = MathUtil.interpolateSmooth(2.0, this.red, mc.crosshairTarget instanceof EntityHitResult ? 5.0F : 1.0F);
      int firstColor = ColorUtil.multRed(Color.WHITE.getRGB(), this.red);
      int secondColor = Color.BLACK.getRGB();
      float x = (float)mc.getWindow().getScaledWidth() / 2.0F;
      float y = (float)mc.getWindow().getScaledHeight() / 2.0F;
      float cooldown = (float)this.attackSetting.get().intValue()
         - (float)this.attackSetting.get().intValue() * mc.player.getAttackCooldownProgress(mc.getRenderTickCounter().getTickDelta(true));
      float size = this.size1Setting.get().floatValue();
      float size2 = this.size2Setting.get().floatValue();
      float offset = size2 / 2.0F;
      float indent = (float)this.indentSetting.get().intValue() + cooldown;
      this.renderMain(drawContext, x, y, size, size2, 1.0F, indent, offset, secondColor);
      this.renderMain(drawContext, x, y, size, size2, 0.0F, indent, offset, firstColor);
   }

   private void renderMain(DrawContext drawContext, float x, float y, float size, float size2, float padding, float indent, float offset, int color) {
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), x - offset - padding / 2.0F, y - size - indent - padding / 2.0F, size2 + padding, size + padding, 0.0F, color
      );
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), x - offset - padding / 2.0F, y + indent - padding / 2.0F, size2 + padding, size + padding, 0.0F, color
      );
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), x - size - indent - padding / 2.0F, y - offset - padding / 2.0F, size + padding, size2 + padding, 0.0F, color
      );
      RenderUtil.drawRoundedRect(
         drawContext.getMatrices(), x + indent - padding / 2.0F, y - offset - padding / 2.0F, size + padding, size2 + padding, 0.0F, color
      );
   }
}
