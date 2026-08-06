package dev.just.screens.dropdown.impl;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.modules.setting.BindBooleanSetting;
import dev.just.screens.dropdown.DescriptionRenderQueue;
import dev.just.screens.dropdown.SettingRenderer;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import dev.just.util.render.Scissor;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;

public class BindBooleanSettingRenderer implements SettingRenderer<BindBooleanSetting>, IMinecraft {
   private static final int HEIGHT = 16;
   private static final int SWITCH_WIDTH = 22;
   private static final int SWITCH_HEIGHT = 12;
   private final Map<BindBooleanSetting, Float> toggleProgressMap = new HashMap<>();
   private final Map<BindBooleanSetting, Float> scrollOffsetMap = new HashMap<>();
   private final Map<BindBooleanSetting, Float> bindBoxProgressMap = new HashMap<>();

   @Override
   public int getHeight() {
      return 16;
   }

   public void render(DrawContext ctx, BindBooleanSetting setting, int x, int y, int width, int height) {
      float progress = this.toggleProgressMap.getOrDefault(setting, setting.get() ? 1.0F : 0.0F);
      float target = setting.get() ? 1.0F : 0.0F;
      progress += (target - progress) * 0.15F;
      this.toggleProgressMap.put(setting, progress);
      double mouseX = mc.mouse.getX() / mc.getWindow().getScaleFactor();
      double mouseY = mc.mouse.getY() / mc.getWindow().getScaleFactor();
      if (setting.expanded) {
         float bindBoxProgress = this.bindBoxProgressMap.getOrDefault(setting, 0.0F);
         bindBoxProgress += (1.0F - bindBoxProgress) * 0.2F;
         if (bindBoxProgress > 0.99F) {
            bindBoxProgress = 1.0F;
         }

         this.bindBoxProgressMap.put(setting, bindBoxProgress);
         long now = System.currentTimeMillis();
         if (now - setting.lastDotUpdate >= 400L) {
            setting.dotState = (setting.dotState + 1) % 4;
            setting.lastDotUpdate = now;
         }

         String dots = setting.isListeningForBind() ? ".".repeat(setting.dotState) : "";
         String displayText = setting.isListeningForBind()
            ? "Binding" + dots
            : (setting.getBindKey() != -1 ? ClientManager.getKey(setting.getBindKey()) : "NONE" + dots);
         float textWidth = FontUtils.durman[13].getWidth(displayText);
         int bindBoxWidth = (int)(10.0F + textWidth);
         int bindBoxHeight = 14;
         int bindBoxTargetX = x - 2;
         int bindBoxY = y + (16 - bindBoxHeight) / 2;
         int bindBoxX = (int)((float)bindBoxTargetX - (float)(bindBoxWidth + 10) * (1.0F - bindBoxProgress));
         RenderUtil.drawRoundedRect(
            ctx.getMatrices(), (float)bindBoxX, (float)bindBoxY, (float)bindBoxWidth, (float)bindBoxHeight, 3.0F, new Color(40, 40, 40, 200).getRGB()
         );
         float maxTextWidth = (float)(bindBoxWidth - 10);
         boolean isTextHovered = RenderUtil.isInRegion(mouseX, mouseY, (double)bindBoxX, (double)bindBoxY, (double)bindBoxWidth, (double)bindBoxHeight);
         float offset = this.scrollOffsetMap.getOrDefault(setting, 0.0F);
         float overflow = textWidth - maxTextWidth;
         if (isTextHovered && overflow > 0.0F) {
            offset = Math.min(offset + 0.1F, overflow);
         } else {
            offset = Math.max(offset - 0.1F, 0.0F);
         }

         this.scrollOffsetMap.put(setting, offset);
         FontUtils.durman[13]
            .drawLeftAligned(
               ctx.getMatrices(),
               displayText,
               (float)(bindBoxX + 5) - offset,
               (float)bindBoxY + ((float)bindBoxHeight - FontUtils.durman[13].getHeight()) / 2.0F,
               Color.WHITE.getRGB()
            );
         int closeSize = 12;
         int paddingRightClose = 8;
         int closeX = x + width - closeSize - paddingRightClose + 10;
         int closeY = bindBoxY + (bindBoxHeight - closeSize) / 2 - 1;
         RenderUtil.drawTexture(
            ctx.getMatrices(), "images/gui/fl.png", (float)closeX, (float)closeY, (float)closeSize, (float)closeSize, 0.0F, Color.RED.getRGB()
         );
      } else {
         float bindBoxProgressx = this.bindBoxProgressMap.getOrDefault(setting, 0.0F);
         bindBoxProgressx += (0.0F - bindBoxProgressx) * 0.2F;
         if (bindBoxProgressx < 0.01F) {
            bindBoxProgressx = 0.0F;
         }

         this.bindBoxProgressMap.put(setting, bindBoxProgressx);
         int switchX = x + width - 22 + 4;
         int switchY = y + 2 - 2;
         Color offColor = new Color(50, 50, 50, 200);
         int onColor = Manager.STYLE_MANAGER.getFirstColor();
         int bgColor = ColorUtil.interpolateColor(offColor.getRGB(), onColor, progress);
         RenderUtil.drawRoundedRect(ctx.getMatrices(), (float)switchX, (float)switchY, 22.0F, 12.0F, 5.0F, bgColor);
         int knobRadius = 8;
         float knobX = (float)(switchX + 3) + (float)(22 - knobRadius - 5) * progress;
         float knobY = (float)switchY + (float)(12 - knobRadius) / 2.0F;
         RenderUtil.drawCircle(ctx.getMatrices(), knobX + (float)knobRadius / 2.0F, knobY + (float)knobRadius / 2.0F, (float)knobRadius, Color.WHITE.getRGB());
         int textY = (int)((float)y + (16.0F - FontUtils.durman[13].getHeight()) / 2.0F) - 2;
         String text = setting.getName();
         float textWidth = FontUtils.durman[13].getWidth(text);
         float maxTextWidth = (float)(switchX - x - 15);
         float overflow = textWidth - maxTextWidth;
         boolean shouldScroll = RenderUtil.isInRegion(mouseX, mouseY, (double)x, (double)y, (double)width, (double)height) && overflow > 0.0F;
         float offset = this.scrollOffsetMap.getOrDefault(setting, 0.0F);
         if (shouldScroll) {
            offset = Math.min(offset + 0.5F, overflow);
         } else {
            offset = Math.max(offset - 0.5F, 0.0F);
         }

         this.scrollOffsetMap.put(setting, offset);
         Scissor.push();
         Scissor.setFromComponentCoordinates((double)x, (double)(textY - 1), (double)maxTextWidth, (double)(FontUtils.durman[13].getHeight() + 2.0F));
         FontUtils.durman[13].drawLeftAligned(ctx.getMatrices(), text, (float)x - offset, (float)textY, Color.WHITE.getRGB());
         Scissor.pop();
         FontUtils.iconsWex[24].centeredDraw(ctx.getMatrices(), "H", (float)(x + width - 25), (float)y + 8.0F - 6.0F, Color.WHITE.getRGB());
         double scale = mc.getWindow().getScaleFactor();
         double mX = mc.mouse.getX() / scale;
         double mY = mc.mouse.getY() / scale;
         boolean isHovered = mX >= (double)switchX && mX <= (double)(switchX + 22) && mY >= (double)switchY && mY <= (double)(switchY + 12);
         if (isHovered && setting.getDesc() != null && !setting.getDesc().isEmpty()) {
            DescriptionRenderQueue.add(setting.getDesc(), (float)mX + 6.0F, (float)mY + 6.0F);
         }
      }
   }

   public boolean mouseClicked(BindBooleanSetting setting, double mouseX, double mouseY, int button, int x, int y, int width, int height) {
      if (button != 0) {
         return false;
      } else {
         int switchX = x + width - 22 + 4;
         int switchY = y + 2 - 2;
         if (!setting.expanded && RenderUtil.isInRegion(mouseX, mouseY, (double)switchX, (double)switchY, 22.0, 12.0)) {
            setting.set(!setting.get());
            return true;
         } else {
            int gearSize = 16;
            int gearX = x + width - 25 - gearSize / 2;
            int gearY = y + 8 - gearSize / 2 - 2;
            if (RenderUtil.isInRegion(mouseX, mouseY, (double)gearX, (double)gearY, (double)gearSize, (double)gearSize)) {
               setting.expanded = true;
               setting.setListeningForBind(false);
               return true;
            } else {
               if (setting.expanded) {
                  float bindBoxProgress = this.bindBoxProgressMap.getOrDefault(setting, 1.0F);
                  float textWidth = FontUtils.durman[13]
                     .getWidth(setting.isListeningForBind() ? "Binding" : (setting.getBindKey() != -1 ? ClientManager.getKey(setting.getBindKey()) : "NONE"));
                  int bindBoxWidth = (int)(10.0F + textWidth);
                  int bindBoxHeight = 14;
                  int bindBoxTargetX = x - 2;
                  int bindBoxX = (int)((float)bindBoxTargetX - (float)(bindBoxWidth + 10) * (1.0F - bindBoxProgress));
                  int bindBoxY = y + (16 - bindBoxHeight) / 2 - 2;
                  if (RenderUtil.isInRegion(mouseX, mouseY, (double)bindBoxX, (double)bindBoxY, (double)bindBoxWidth, (double)bindBoxHeight)) {
                     setting.setListeningForBind(true);
                     return true;
                  }

                  int closeSize = 12;
                  int paddingRightClose = 8;
                  int closeX = x + width - closeSize - paddingRightClose + 10;
                  int closeY = bindBoxY + (bindBoxHeight - closeSize) / 2 - 2;
                  if (RenderUtil.isInRegion(mouseX, mouseY, (double)closeX, (double)closeY, (double)closeSize, (double)closeSize)) {
                     setting.expanded = false;
                     setting.setListeningForBind(false);
                     return true;
                  }
               }

               return false;
            }
         }
      }
   }
}
