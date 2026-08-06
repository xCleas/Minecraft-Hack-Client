package dev.just.manager.notificationManager;

import dev.just.manager.IMinecraft;
import dev.just.manager.fontManager.FontUtils;
import dev.just.util.animations.Animation;
import dev.just.util.animations.impl.DecelerateAnimation;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Direction.AxisDirection;

public class Notification implements IMinecraft {
   private float x;
   private float y;
   private String name;
   private String desc;
   private NotificationType type;
   private long time = System.currentTimeMillis();
   public Animation animation = new DecelerateAnimation(500, 1.0, AxisDirection.POSITIVE);
   public Animation animationy = new DecelerateAnimation(500, 1.0, AxisDirection.POSITIVE);
   float alpha;
   int times;
   private float width;

   public Notification(NotificationType type, String name, String desc, int time) {
      this.type = type;
      this.name = name;
      this.desc = desc;
      this.times = time;
   }

   public float draw(DrawContext context) {
      float widthName = FontUtils.durman[13].getWidth(this.name);
      float widthDesc = FontUtils.durman[12].getWidth(this.desc);
      this.width = Math.max(widthName, widthDesc) + 40.0F;
      RenderUtil.drawRoundedRect(
         context.getMatrices(),
         this.x,
         this.y,
         this.width,
         24.0F,
         4.0F,
         ColorUtil.reAlphaInt(
            ColorUtil.interpolateColor(new Color(17, 15, 28, 255).getRGB(), new Color(17, 15, 28, 255).getRGB(), 0.05F), (int)(170.0F * this.alpha)
         )
      );
      RenderUtil.drawRoundedRect(
         context.getMatrices(),
         this.x + 22.0F,
         this.y + 3.0F,
         1.0F,
         18.0F,
         0.0F,
         ColorUtil.reAlphaInt(
            ColorUtil.interpolateColor(new Color(255, 255, 255, 255).getRGB(), new Color(255, 255, 255, 255).getRGB(), 0.05F), (int)(170.0F * this.alpha)
         )
      );
      this.type.renderIcon(context.getMatrices(), this.x + 3.5F, this.y + 4.0F, ColorUtil.rgba(255, 255, 255, (int)(240.0F * this.alpha)));
      FontUtils.durman[13]
         .drawLeftAligned(context.getMatrices(), this.name, this.x + 28.0F, this.y + 3.0F, ColorUtil.rgba(255, 255, 255, (int)(240.0F * this.alpha)));
      FontUtils.durman[12]
         .drawLeftAligned(context.getMatrices(), this.desc, this.x + 28.0F, this.y + 13.0F, ColorUtil.rgba(210, 210, 210, (int)(240.0F * this.alpha)));
      return 24.0F;
   }

   public float getWidth() {
      return this.width;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public void setX(float x) {
      this.x = x;
   }

   public void setY(float y) {
      this.y = y;
   }

   public String getName() {
      return this.name;
   }

   public String getDesc() {
      return this.desc;
   }

   public NotificationType getType() {
      return this.type;
   }

   public long getTime() {
      return this.time;
   }
}
