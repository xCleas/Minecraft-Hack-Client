package dev.just.screens.dropdown.impl;

import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.setting.SliderSetting;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class SliderSettingRenderer implements SettingRenderer<SliderSetting> {
   private static final int HEIGHT = 20;
   private static final int BAR_HEIGHT = 4;
   private static final int PADDING = 0;
   private static final float CIRCLE_RADIUS = 6.0F;
   private static final float CIRCLE_SCALE_MAX = 1.2F;
   private static final float CIRCLE_SCALE_MIN = 1.0F;
   private static final float SCALE_STEP = 0.05F;

   public void render(DrawContext ctx, SliderSetting setting, int x, int y, int width, int height) {
      int barWidth = width - 0;
      int barX = x + 0;
      int barY = y + height - 4 - 4;
      RenderUtil.drawRoundedRect(ctx.getMatrices(), (float)barX, (float)barY, (float)barWidth, 4.0F, 1.0F, new Color(50, 50, 50, 180).getRGB());
      double increment = setting.getIncrement();
      double rawValue = setting.get().doubleValue();
      double roundedValue = (double)Math.round(rawValue / increment) * increment;
      double progress = (roundedValue - setting.getMin()) / (setting.getMax() - setting.getMin());
      int targetProgressWidth = (int)((double)barWidth * progress);
      if (setting.circlePos == -1.0) {
         setting.circlePos = (double)targetProgressWidth;
      }

      setting.circlePos = setting.circlePos + ((double)targetProgressWidth - setting.circlePos) * 0.2;
      RenderUtil.drawRoundedRect(
         ctx.getMatrices(), (float)barX, (float)barY, (float)((int)setting.circlePos), 4.0F, 1.0F, Manager.STYLE_MANAGER.getFirstColor()
      );
      FontUtils.durman[13].drawLeftAligned(ctx.getMatrices(), setting.getName(), (float)(x + 0), (float)(y + 2), Color.WHITE.getRGB());
      String valueText = this.formatValue(roundedValue, increment);
      int valueWidth = (int)FontUtils.durman[13].getWidth(valueText);
      FontUtils.durman[13].drawLeftAligned(ctx.getMatrices(), valueText, (float)(x + width - valueWidth - 0), (float)(y + 2), Color.WHITE.getRGB());
      if (setting.dragging) {
         setting.circleScale += 0.05F;
         if (setting.circleScale > 1.2F) {
            setting.circleScale = 1.2F;
         }
      } else {
         setting.circleScale -= 0.05F;
         if (setting.circleScale < 1.0F) {
            setting.circleScale = 1.0F;
         }
      }

      float circleX = (float)barX + (float)setting.circlePos;
      float circleY = (float)barY + 2.0F;
      MatrixStack matrices = ctx.getMatrices();
      matrices.push();
      matrices.translate(circleX, circleY, 0.0F);
      matrices.scale(setting.circleScale, setting.circleScale, 1.0F);
      matrices.translate(-circleX, -circleY, 0.0F);
      RenderUtil.drawCircle(matrices, circleX, circleY, 6.0F, Color.WHITE.getRGB());
      matrices.pop();
   }

   private String formatValue(double val, double increment) {
      if (increment >= 1.0) {
         return String.format(Locale.US, "%d", (long)val);
      } else {
         return increment >= 0.1 ? String.format(Locale.US, "%.1f", val) : String.format(Locale.US, "%.2f", val);
      }
   }

   public boolean mouseClicked(SliderSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         int barWidth = width - 0;
         int barX = x + 0;
         int barY = y + height - 4 - 4;
         if (RenderUtil.isInRegion(mouseX, mouseY, (double)barX, (double)(barY - 3), (double)barWidth, 10.0)) {
            this.updateValue(setting, mouseX, barX, barWidth);
            setting.dragging = true;
            return true;
         } else {
            return false;
         }
      }
   }

   public void mouseReleased(SliderSetting setting) {
      setting.dragging = false;
   }

   public void mouseDragged(SliderSetting setting, double mouseX, int x, int width) {
      if (setting.dragging) {
         int barWidth = width - 0;
         int barX = x + 0;
         this.updateValue(setting, mouseX, barX, barWidth);
      }
   }

   private void updateValue(SliderSetting setting, double mouseX, int barX, int barWidth) {
      double relX = mouseX - (double)barX;
      double percent = Math.min(Math.max(relX / (double)barWidth, 0.0), 1.0);
      double newValue = setting.getMin() + percent * (setting.getMax() - setting.getMin());
      double increment = setting.getIncrement();
      newValue = (double)Math.round(newValue / increment) * increment;
      newValue = Math.min(Math.max(newValue, setting.getMin()), setting.getMax());
      setting.set(newValue);
   }

   @Override
   public int getHeight() {
      return 20;
   }
}
