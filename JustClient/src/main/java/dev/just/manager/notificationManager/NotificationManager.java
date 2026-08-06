package dev.just.manager.notificationManager;

import dev.just.manager.IMinecraft;
import dev.just.util.math.MathUtil;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Direction.AxisDirection;

public class NotificationManager implements IMinecraft {
   private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

   public void add(NotificationType type, String name, String desc, int time) {
      this.notifications.add(new Notification(type, name, desc, time));
   }

   public void draw(DrawContext context) {
      if (mc.getWindow() == null) return;

      int yoffset = 0;
      float notificationHeight = 24.0F;

      for (Notification notification : this.notifications) {
         long timePassed = System.currentTimeMillis() - notification.getTime();
         long totalDuration = (long)notification.times * 1000L;
         if (timePassed > totalDuration - 222L) {
            notification.animation.setDirection(AxisDirection.NEGATIVE);
         }

         notification.alpha = (float)notification.animation.getOutput();
         if (timePassed > totalDuration) {
            notification.animationy.setDirection(AxisDirection.NEGATIVE);
         }

         if (notification.animationy.finished(AxisDirection.NEGATIVE)) {
            this.notifications.remove(notification);
         } else {
            float fixedRightPadding = 8.0F;
            // İlk width hesapla - minimum 150 piksel
            float width = Math.max(150.0F, notification.getWidth());
            float baseX = (float)mc.getWindow().getScaledWidth() - width - fixedRightPadding;
            float baseY = (float)(mc.getWindow().getScaledHeight() - 30);
            notification.animationy.setEndPoint((double)((float)yoffset * 1.1F));
            notification.animationy.setDuration(300);

            // ÖNCE pozisyonları hesapla
            float y;
            if (notification.animationy.getDirection() == AxisDirection.NEGATIVE) {
               y = baseY - (float)((double)notificationHeight * notification.animationy.getOutput());
            } else {
               y = baseY - notificationHeight * (float)yoffset * 1.1F;
            }

            float x = baseX;
            if (notification.animation.getDirection() == AxisDirection.NEGATIVE) {
               double output = notification.animation.getOutput();
               x = baseX + (float)((double)width * (1.0 - output));
            }

            // Pozisyonları ayarla
            notification.setX(x);
            if (y <= notification.getY()) {
               notification.setY(y);
            } else {
               notification.setY(MathUtil.fast(notification.getY(), y, 10.0F));
            }

            // SONRA çiz
            notification.draw(context);

            yoffset++;
         }
      }
   }
}
