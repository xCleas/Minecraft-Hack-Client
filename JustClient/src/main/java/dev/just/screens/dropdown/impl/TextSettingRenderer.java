package dev.just.screens.dropdown.impl;

import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.setting.TextSetting;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;

public class TextSettingRenderer implements SettingRenderer<TextSetting> {
   private static final int PADDING = 0;
   private static final int HORIZONTAL_PADDING = 6;
   private static final int VERTICAL_PADDING = 3;
   private static final int FIELD_RADIUS = 3;
   private static final int MIN_FIELD_WIDTH = 60;
   private static final int MAX_FIELD_WIDTH = 105;
   private long lastBlinkTime = 0L;
   private float scrollOffset = 0.0F;

   public void render(DrawContext ctx, TextSetting setting, int x, int y, int width, int height) {
      int fontSizeName = 13;
      int fontSizeField = 13;
      int nameHeight = (int)FontUtils.durman[fontSizeName].getHeight();
      FontUtils.durman[fontSizeName].drawLeftAligned(ctx.getMatrices(), setting.getName(), (float)(x + 0), (float)y, Color.WHITE.getRGB());
      String value = setting.getValue();
      int textWidth = (int)FontUtils.durman[fontSizeField].getWidth(value) + 12;
      int fieldWidth = Math.max(60, Math.min(textWidth, 105));
      int fieldHeight = (int)(FontUtils.durman[fontSizeField].getHeight() + 6.0F);
      int fieldX = x + 0;
      int fieldY = y + nameHeight + 3;
      int bgColor = setting.isFocused() ? new Color(60, 60, 60, 200).getRGB() : new Color(40, 40, 40, 200).getRGB();
      RenderUtil.drawRoundedRect(ctx.getMatrices(), (float)fieldX, (float)fieldY, (float)fieldWidth, (float)(fieldHeight - 1), 3.0F, bgColor);
      float targetScroll = 0.0F;
      int cursorX = (int)FontUtils.durman[fontSizeField].getWidth(value.substring(0, setting.getCursorPosition()));
      if (cursorX + 6 > fieldWidth) {
         targetScroll = (float)(fieldWidth - (cursorX + 6));
      } else if (cursorX + 6 < 0) {
         targetScroll = (float)(-cursorX + 6);
      }

      this.scrollOffset = this.scrollOffset + (targetScroll - this.scrollOffset) * 0.2F;
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)(fieldX + 6), (double)fieldY, (double)(fieldWidth - 12), (double)fieldHeight);
      FontUtils.durman[fontSizeField]
         .drawLeftAligned(ctx.getMatrices(), value, (float)(fieldX + 6) + this.scrollOffset, (float)(fieldY + 3) - 0.6F, Color.WHITE.getRGB());
      Scissor.pop();
      if (setting.isFocused()) {
         long now = System.currentTimeMillis();
         if (now - this.lastBlinkTime > 500L) {
            setting.cursorVisible = !setting.cursorVisible;
            this.lastBlinkTime = now;
         }

         if (setting.cursorVisible) {
            float cursorPos = FontUtils.durman[fontSizeField].getWidth(value.substring(0, setting.getCursorPosition()));
            RenderUtil.drawRoundedRect(
               ctx.getMatrices(),
               (float)(fieldX + 6) + cursorPos + this.scrollOffset,
               (float)(fieldY + 3),
               1.0F,
               FontUtils.durman[fontSizeField].getHeight(),
               0.0F,
               Color.WHITE.getRGB()
            );
         }
      }
   }

   public boolean mouseClicked(TextSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         int fontSizeField = 13;
         int nameHeight = (int)FontUtils.durman[fontSizeField].getHeight();
         int fieldY = y + nameHeight + 2;
         String value = setting.getValue();
         int fieldWidth = Math.max(60, Math.min((int)FontUtils.durman[fontSizeField].getWidth(value) + 12, 105));
         int fieldHeight = (int)(FontUtils.durman[fontSizeField].getHeight() + 6.0F);
         int fieldX = x + 0;
         boolean inside = mouseX >= (double)fieldX
            && mouseX <= (double)(fieldX + fieldWidth)
            && mouseY >= (double)fieldY
            && mouseY <= (double)(fieldY + fieldHeight);
         setting.setFocused(inside);
         if (inside) {
            setting.setCursorPosition(value.length());
         }

         return inside;
      }
   }

   public boolean keyPressed(TextSetting setting, int keyCode, int scanCode, int modifiers) {
      if (!setting.isFocused()) {
         return false;
      } else {
         String value = setting.getValue();
         if (keyCode == 257) {
            setting.setFocused(false);
         } else {
            if (keyCode == 259 && setting.getCursorPosition() > 0) {
               value = value.substring(0, setting.getCursorPosition() - 1) + value.substring(setting.getCursorPosition());
               setting.setCursorPosition(setting.getCursorPosition() - 1);
               setting.setValue(value);
               return true;
            }

            if (keyCode == 261 && setting.getCursorPosition() < value.length()) {
               value = value.substring(0, setting.getCursorPosition()) + value.substring(setting.getCursorPosition() + 1);
               setting.setValue(value);
               return true;
            }

            if (keyCode == 263) {
               setting.setCursorPosition(Math.max(0, setting.getCursorPosition() - 1));
            } else if (keyCode == 262) {
               setting.setCursorPosition(Math.min(value.length(), setting.getCursorPosition() + 1));
            } else if (keyCode == 256) {
               setting.setFocused(false);
            }
         }

         return true;
      }
   }

   public boolean charTyped(TextSetting setting, char c, int modifiers) {
      if (setting.isFocused() && !Character.isISOControl(c)) {
         String value = setting.getValue();
         value = value.substring(0, setting.getCursorPosition()) + c + value.substring(setting.getCursorPosition());
         setting.setCursorPosition(setting.getCursorPosition() + 1);
         setting.setValue(value);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getHeight() {
      int fontSizeName = 13;
      int nameHeight = (int)FontUtils.durman[fontSizeName].getHeight();
      int fieldHeight = (int)(FontUtils.durman[13].getHeight() + 10.5F);
      return nameHeight + 2 + fieldHeight;
   }
}
