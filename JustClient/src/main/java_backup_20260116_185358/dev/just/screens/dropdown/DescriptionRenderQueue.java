package dev.just.screens.dropdown;

import dev.just.manager.fontManager.FontUtils;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;

public class DescriptionRenderQueue {
   private static final List<DescriptionRenderQueue.QueuedDescription> DESCRIPTIONS = new ArrayList<>();

   public static void add(String text, float x, float y) {
      if (text != null && !text.isEmpty()) {
         DESCRIPTIONS.add(new DescriptionRenderQueue.QueuedDescription(text, x, y));
      }
   }

   public static void renderAll(DrawContext context) {
      for (DescriptionRenderQueue.QueuedDescription desc : DESCRIPTIONS) {
         float width = FontUtils.durman[14].getWidth(desc.text) + 8.0F;
         float height = 12.0F;
         RenderUtil.drawRoundedRect(context.getMatrices(), desc.x, desc.y, width, 12.0F, 2.0F, new Color(0, 0, 0, 255).getRGB());
         FontUtils.durman[14].drawLeftAligned(context.getMatrices(), desc.text, desc.x + 4.0F, desc.y + 1.5F, Color.WHITE.getRGB());
      }

      DESCRIPTIONS.clear();
   }

   private static class QueuedDescription {
      final String text;
      final float x;
      final float y;

      QueuedDescription(String text, float x, float y) {
         this.text = text;
         this.x = x;
         this.y = y;
      }
   }
}
