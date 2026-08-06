package dev.just.screens.dropdown.impl;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.setting.MultiSetting;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;

public class MultiSettingRenderer implements SettingRenderer<MultiSetting>, IMinecraft {
   private static final int TITLE_HEIGHT = 14;
   private static final int MODS_Y_OFFSET = 1;
   private static final int BOX_HEIGHT = 12;
   private static final int LINE_SPACING = 2;
   private static final int PADDING_HORIZONTAL = 3;
   private static final int START_X_OFFSET = 0;
   private final Map<String, Float> scrollOffsets = new HashMap<>();
   private final Map<String, Float> hoverProgress = new HashMap<>();

   public int getHeight(MultiSetting setting, int width) {
      int lineHeight = 14;
      int currentX = 0;
      int lines = 1;

      for (String mode : setting.getAvailableModes()) {
         int textWidth = (int)FontUtils.durman[13].getWidth(mode);
         int boxWidth = textWidth + 6;
         if (currentX + boxWidth > width) {
            currentX = 0;
            lines++;
         }

         currentX += boxWidth + 4;
      }

      return 14 + lines * lineHeight;
   }

   @Override
   public int getHeight() {
      return 28;
   }

   public void render(DrawContext ctx, MultiSetting setting, int x, int y, int width, int height) {
      double mouseX = mc.mouse.getX() / mc.getWindow().getScaleFactor();
      double mouseY = mc.mouse.getY() / mc.getWindow().getScaleFactor();
      FontUtils.durman[13].drawLeftAligned(ctx.getMatrices(), setting.getName(), (float)x, (float)y, Color.WHITE.getRGB());
      String counter = setting.getAllSelected() + "/" + setting.getAvailableModes().size();
      float counterWidth = FontUtils.durman[13].getWidth(counter);
      FontUtils.durman[13].drawLeftAligned(ctx.getMatrices(), counter, (float)(x + width) - counterWidth - 2.0F, (float)y, new Color(180, 180, 180).getRGB());
      int startX = x + 0;
      int spacing = 4;
      int lineHeight = 14;
      int currentX = startX;
      int currentY = y + 14 - 1;

      for (String mode : setting.getAvailableModes()) {
         int textWidth = (int)FontUtils.durman[13].getWidth(mode);
         int boxWidth = textWidth + 6;
         if (currentX + boxWidth > x + width) {
            currentX = startX;
            currentY += lineHeight;
         }

         boolean selected = setting.get(mode);
         boolean hovered = RenderUtil.isInRegion(mouseX, mouseY, (double)currentX, (double)currentY, (double)boxWidth, 12.0);
         float hoverProg = this.hoverProgress.getOrDefault(mode, 0.0F);
         if (selected) {
            hoverProg = 1.0F;
         } else {
            hoverProg += (hovered ? 1.0F : 0.0F - hoverProg) * 0.08F;
         }

         this.hoverProgress.put(mode, hoverProg);
         Color baseColor = new Color(30, 30, 30, 180);
         int selectedColor = Manager.STYLE_MANAGER.getFirstColor();
         int bgColor = selected ? selectedColor : baseColor.getRGB();
         Color baseTextColor = new Color(200, 200, 200);
         Color hoverTextColor = Color.WHITE;
         int textColor = ColorUtil.interpolateColor(baseTextColor.getRGB(), hoverTextColor.getRGB(), hoverProg);
         RenderUtil.drawRoundedRect(ctx.getMatrices(), (float)currentX, (float)currentY, (float)boxWidth, 12.0F, 1.0F, bgColor);
         float maxTextWidth = (float)(boxWidth - 6);
         float overflow = (float)textWidth - maxTextWidth;
         float offset = this.scrollOffsets.getOrDefault(mode, 0.0F);
         if (hovered && overflow > 0.0F) {
            offset = Math.min(offset + 0.5F, overflow);
         } else {
            offset = Math.max(offset - 0.5F, 0.0F);
         }

         this.scrollOffsets.put(mode, offset);
         Scissor.push();
         Scissor.setFromComponentCoordinates((double)(currentX + 3), (double)currentY, (double)maxTextWidth, 12.0);
         FontUtils.durman[13]
            .drawLeftAligned(
               ctx.getMatrices(), mode, (float)(currentX + 3) - offset, (float)currentY + (12.0F - FontUtils.durman[13].getHeight()) / 2.0F, textColor
            );
         Scissor.pop();
         currentX += boxWidth + spacing;
      }
   }

   public boolean mouseClicked(MultiSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         int startX = x + 0;
         int spacing = 4;
         int lineHeight = 14;
         int currentX = startX;
         int currentY = y + 14 - 1;

         for (String mode : setting.getAvailableModes()) {
            int textWidth = (int)FontUtils.durman[13].getWidth(mode);
            int boxWidth = textWidth + 6;
            if (currentX + boxWidth > x + width) {
               currentX = startX;
               currentY += lineHeight;
            }

            if (RenderUtil.isInRegion(mouseX, mouseY, (double)currentX, (double)currentY, (double)boxWidth, 12.0)) {
               setting.toggle(mode);
               return true;
            }

            currentX += boxWidth + spacing;
         }

         return false;
      }
   }
}
