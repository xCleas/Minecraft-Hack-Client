package dev.just.screens.dropdown.impl;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.setting.BindSetting;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;

public class BindSettingRenderer implements SettingRenderer<BindSetting>, IMinecraft {
   private static final int HEIGHT = 15;
   private static final int BUTTON_WIDTH = 12;
   private static final int BUTTON_HEIGHT = 12;
   private static final int BUTTON_RADIUS = 3;
   private static final int PADDING = 0;
   private final Map<BindSetting, Float> nameScrollOffsetMap = new HashMap<>();

   public void render(DrawContext ctx, BindSetting setting, int x, int y, int width, int height) {
      String displayText;
      if (setting.isBinding()) {
         long dots = System.currentTimeMillis() / 400L % 4L;
         displayText = "Binding" + ".".repeat((int)dots);
      } else {
         displayText = setting.getKey() != -1 ? ClientManager.getKey(setting.getKey()) : "NONE";
      }

      int fontSizeName = 13;
      int fontSizeButton = 13;
      int textWidthButton = (int)FontUtils.durman[fontSizeButton].getWidth(displayText);
      int paddingButton = 10;
      int buttonWidth = Math.max(12, textWidthButton + paddingButton);
      int buttonHeight = 12;
      int buttonX = x + width - buttonWidth - 0;
      int buttonY = y + (15 - buttonHeight) / 2;
      int nameAreaX = x + 0;
      int nameAreaWidth = buttonX - nameAreaX - 4;
      String name = setting.getName();
      float nameTextWidth = FontUtils.durman[fontSizeName].getWidth(name);
      double mouseX = mc.mouse.getX() / mc.getWindow().getScaleFactor();
      double mouseY = mc.mouse.getY() / mc.getWindow().getScaleFactor();
      boolean hoveredNameArea = RenderUtil.isInRegion(mouseX, mouseY, (double)nameAreaX, (double)y, (double)nameAreaWidth, 15.0);
      float scrollOffset = this.nameScrollOffsetMap.getOrDefault(setting, 0.0F);
      float overflow = nameTextWidth - (float)nameAreaWidth;
      if (hoveredNameArea && overflow > 0.0F) {
         scrollOffset = Math.min(scrollOffset + 1.0F, overflow);
      } else {
         scrollOffset = Math.max(scrollOffset - 1.0F, 0.0F);
      }

      this.nameScrollOffsetMap.put(setting, scrollOffset);
      Scissor.push();
      Scissor.setFromComponentCoordinates(
         (double)nameAreaX,
         (double)((float)y + (15.0F - FontUtils.durman[fontSizeName].getHeight()) / 2.0F - 1.0F),
         (double)nameAreaWidth,
         (double)(FontUtils.durman[fontSizeName].getHeight() + 2.0F)
      );
      FontUtils.durman[fontSizeName]
         .drawLeftAligned(
            ctx.getMatrices(),
            name,
            (float)nameAreaX - scrollOffset,
            (float)y + (15.0F - FontUtils.durman[fontSizeName].getHeight()) / 2.0F,
            Color.WHITE.getRGB()
         );
      Scissor.pop();
      RenderUtil.drawRoundedRect(
         ctx.getMatrices(), (float)buttonX, (float)buttonY, (float)buttonWidth, (float)buttonHeight, 3.0F, new Color(40, 40, 40, 200).getRGB()
      );
      FontUtils.durman[fontSizeButton]
         .centeredDraw(
            ctx.getMatrices(),
            displayText,
            (float)buttonX + (float)buttonWidth / 2.0F,
            (float)buttonY + ((float)buttonHeight - FontUtils.durman[fontSizeButton].getHeight()) / 2.0F,
            Color.WHITE.getRGB()
         );
   }

   public boolean mouseClicked(BindSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         String displayText = setting.isBinding() ? "Binding" : (setting.getKey() != -1 ? ClientManager.getKey(setting.getKey()) : "NONE");
         int fontSizeButton = 13;
         int textWidthButton = (int)FontUtils.durman[fontSizeButton].getWidth(displayText);
         int paddingButton = 10;
         int buttonWidth = Math.max(12, textWidthButton + paddingButton);
         int buttonHeight = 12;
         int buttonX = x + width - buttonWidth - 0;
         int buttonY = y + (15 - buttonHeight) / 2;
         if (RenderUtil.isInRegion(mouseX, mouseY, (double)buttonX, (double)buttonY, (double)buttonWidth, (double)buttonHeight)) {
            setting.setBinding(!setting.isBinding());
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public int getHeight() {
      return 15;
   }
}
