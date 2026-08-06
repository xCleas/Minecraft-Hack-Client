package dev.just.screens.dropdown.impl;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.fontManager.RenderFonts;
import dev.just.modules.setting.BooleanSetting;
import dev.just.screens.dropdown.DescriptionRenderQueue;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;

public class BooleanSettingRenderer implements SettingRenderer<BooleanSetting>, IMinecraft {
   private static final int HEIGHT = 16;
   private static final int WIDTH = 22;
   private static final int SWITCH_HEIGHT = 12;
   private static final int KNOB_RADIUS = 8;
   private final Map<BooleanSetting, Float> toggleMap = new HashMap<>();
   private final Map<BooleanSetting, Float> scrollMap = new HashMap<>();

   @Override
   public int getHeight() {
      return 16;
   }

   public void render(DrawContext ctx, BooleanSetting setting, int x, int y, int width, int height) {
      float progress = this.toggleMap.getOrDefault(setting, setting.get() ? 1.0F : 0.0F);
      progress += ((setting.get() ? 1.0F : 0.0F) - progress) * 0.15F;
      this.toggleMap.put(setting, progress);
      int switchX = x + width - 22 + 4;
      int switchY = y + 2 - 2;
      Color offColor = new Color(50, 50, 50, 200);
      int onColor = Manager.STYLE_MANAGER.getFirstColor();
      int bgColor = ColorUtil.interpolateColor(offColor.getRGB(), onColor, progress);
      RenderUtil.drawRoundedRect(ctx.getMatrices(), (float)switchX, (float)switchY, 22.0F, 12.0F, 5.0F, bgColor);
      RenderUtil.drawCircle(ctx.getMatrices(), (float)(switchX + 3) + 9.0F * progress + 4.0F, (float)switchY + 2.0F + 4.0F, 8.0F, Color.WHITE.getRGB());
      RenderFonts font = FontUtils.durman[13];
      String text = setting.getName();
      int textY = (int)((float)y + (16.0F - font.getHeight()) / 2.0F) - 2;
      float maxTextWidth = (float)(switchX - x - 4);
      float textWidth = font.getWidth(text);
      double scale = mc.getWindow().getScaleFactor();
      double mX = mc.mouse.getX() / scale;
      double mY = mc.mouse.getY() / scale;
      float overflow = textWidth - maxTextWidth;
      float offset = this.scrollMap.getOrDefault(setting, 0.0F);
      boolean textHovered = RenderUtil.isHovered((int)mX, (int)mY, (double)x, (double)(textY - 1), (double)maxTextWidth, (double)(font.getHeight() + 2.0F));
      if (textHovered && overflow > 0.0F) {
         offset = Math.min(offset + 0.5F, overflow);
      } else {
         offset = Math.max(offset - 0.5F, 0.0F);
      }

      this.scrollMap.put(setting, offset);
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)x, (double)(textY - 1), (double)maxTextWidth, (double)(font.getHeight() + 2.0F));
      font.drawLeftAligned(ctx.getMatrices(), text, (float)x - offset, (float)textY, Color.WHITE.getRGB());
      Scissor.pop();
      boolean isHovered = mX >= (double)switchX && mX <= (double)(switchX + 22) && mY >= (double)switchY && mY <= (double)(switchY + 12);
      if (isHovered && setting.getDesc() != null && !setting.getDesc().isEmpty()) {
         DescriptionRenderQueue.add(setting.getDesc(), (float)mX + 6.0F, (float)mY + 6.0F);
      }
   }

   public boolean mouseClicked(BooleanSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         int switchX = x + width - 22 + 4;
         int switchY = y + 2 - 2;
         if (mouseX >= (double)switchX && mouseX <= (double)(switchX + 22) && mouseY >= (double)switchY && mouseY <= (double)(switchY + 12)) {
            setting.set(!setting.get());
            return true;
         } else {
            return false;
         }
      }
   }
}
