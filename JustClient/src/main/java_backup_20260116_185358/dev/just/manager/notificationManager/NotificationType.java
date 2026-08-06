package dev.just.manager.notificationManager;

import dev.just.util.render.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;

public enum NotificationType {
   INFO("info.png"),
   SUCCESS("add.png"),
   REMOVED("remove.png");

   private final String texture;

   private NotificationType(String texture) {
      this.texture = texture;
   }

   public int renderIcon(MatrixStack matrixStack, float x, float y, int color) {
      RenderUtil.drawTexture(matrixStack, "images/state/" + this.texture, x, y, 16.0F, 16.0F, 8.0F, color);
      return 0;
   }

   public String getTexture() {
      return this.texture;
   }
}
