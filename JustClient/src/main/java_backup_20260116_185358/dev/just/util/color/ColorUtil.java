package dev.just.util.color;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.manager.themeManager.StyleManager;
import dev.just.util.math.MathUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import net.minecraft.util.Identifier;
import net.minecraft.resource.Resource;
import net.minecraft.util.math.MathHelper;

public class ColorUtil implements IMinecraft {
   public static final int hud_color = new Color(45, 30, 20, 220).getRGB();
   public static final int hud_color2 = new Color(30, 20, 15, 255).getRGB();
   private static final Map<Identifier, BufferedImage> CACHED_IMAGES = new HashMap<>();

   public static void loadImage(Identifier identifier) {
      if (!CACHED_IMAGES.containsKey(identifier)) {
         try {
            Optional<Resource> resourceOptional = mc.getResourceManager().getResource(identifier);
            if (resourceOptional.isPresent()) {
               CACHED_IMAGES.put(identifier, ImageIO.read(resourceOptional.get().getInputStream()));
            }
         } catch (IOException var2) {
         }
      }
   }

   public static int applyAlpha(int color, float alpha) {
      int a = (int)((float)(color >> 24 & 0xFF) * alpha);
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      return a << 24 | r << 16 | g << 8 | b;
   }

   public static int getPixelColor(Identifier id, float pixelX, float pixelY) {
      BufferedImage bufferedImage = CACHED_IMAGES.get(id);
      int x = Math.max(0, Math.min((int)(pixelX * (float)bufferedImage.getWidth()), bufferedImage.getWidth() - 1));
      int y = Math.max(0, Math.min((int)(pixelY * (float)bufferedImage.getHeight()), bufferedImage.getHeight() - 1));
      return bufferedImage.getRGB(x, y);
   }

   public static int blendColors(int color1, int color2, float ratio) {
      int r1 = color1 >> 16 & 0xFF;
      int g1 = color1 >> 8 & 0xFF;
      int b1 = color1 & 0xFF;
      int r2 = color2 >> 16 & 0xFF;
      int g2 = color2 >> 8 & 0xFF;
      int b2 = color2 & 0xFF;
      int r = (int)((float)r1 * (1.0F - ratio) + (float)r2 * ratio);
      int g = (int)((float)g1 * (1.0F - ratio) + (float)g2 * ratio);
      int b = (int)((float)b1 * (1.0F - ratio) + (float)b2 * ratio);
      return r << 16 | g << 8 | b;
   }

   public static int blendColorsInt(int color1, int color2, float ratio) {
      float ir = 1.0F - ratio;
      int a1 = color1 >> 24 & 0xFF;
      int r1 = color1 >> 16 & 0xFF;
      int g1 = color1 >> 8 & 0xFF;
      int b1 = color1 & 0xFF;
      int a2 = color2 >> 24 & 0xFF;
      int r2 = color2 >> 16 & 0xFF;
      int g2 = color2 >> 8 & 0xFF;
      int b2 = color2 & 0xFF;
      int a = (int)((float)a1 * ir + (float)a2 * ratio);
      int r = (int)((float)r1 * ir + (float)r2 * ratio);
      int g = (int)((float)g1 * ir + (float)g2 * ratio);
      int b = (int)((float)b1 * ir + (float)b2 * ratio);
      return a << 24 | r << 16 | g << 8 | b;
   }

   public static int withAlpha(int rgb, float a) {
      int ai = MathHelper.clamp((int)(a * 255.0F), 0, 255);
      return rgb & 16777215 | ai << 24;
   }

   public static int rgba(int r, int g, int b, int a) {
      return a << 24 | r << 16 | g << 8 | b;
   }

   public static int getRed(int hex) {
      return hex >> 16 & 0xFF;
   }

   public static int getGreen(int hex) {
      return hex >> 8 & 0xFF;
   }

   public static int getBlue(int hex) {
      return hex & 0xFF;
   }

   public static int getAlpha(int hex) {
      return hex >> 24 & 0xFF;
   }

   public static int getColorStyle(float index) {
      return getColorHud((int)index);
   }

   public static int getColorStyle(float index, float alpha) {
      return getColorHud((int)index, (int)alpha);
   }

   public static int getColorHud(int index) {
      StyleManager theme = Manager.STYLE_MANAGER;
      Color upColor = new Color(theme.getFirstColor());
      Color downColor = new Color(theme.getSecondColor());
      return gradient(5, index, upColor.getRGB(), downColor.getRGB());
   }

   public static int getColorHud(int index, int alpha) {
      StyleManager theme = Manager.STYLE_MANAGER;
      Color upColor = new Color(theme.getFirstColor());
      Color downColor = new Color(theme.getSecondColor());
      int gradientColor = gradient(5, index, upColor.getRGB(), downColor.getRGB());
      int red = gradientColor >> 16 & 0xFF;
      int green = gradientColor >> 8 & 0xFF;
      int blue = gradientColor & 0xFF;
      return new Color(red, green, blue, alpha).getRGB();
   }

   public static float[] rgba(int color) {
      return new float[]{
         (float)(color >> 16 & 0xFF) / 255.0F, (float)(color >> 8 & 0xFF) / 255.0F, (float)(color & 0xFF) / 255.0F, (float)(color >> 24 & 0xFF) / 255.0F
      };
   }

   public static int gradient(int speed, int index, int... colors) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      int colorIndex = (int)((float)angle / 360.0F * (float)colors.length);
      if (colorIndex == colors.length) {
         colorIndex--;
      }

      int color1 = colors[colorIndex];
      int color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
      return interpolateColor(color1, color2, (float)angle / 360.0F * (float)colors.length - (float)colorIndex);
   }

   public static int interpolateColor(int color1, int color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      int red1 = getRed(color1);
      int green1 = getGreen(color1);
      int blue1 = getBlue(color1);
      int alpha1 = getAlpha(color1);
      int red2 = getRed(color2);
      int green2 = getGreen(color2);
      int blue2 = getBlue(color2);
      int alpha2 = getAlpha(color2);
      int interpolatedRed = MathUtil.interpolateInt(red1, red2, (double)amount);
      int interpolatedGreen = MathUtil.interpolateInt(green1, green2, (double)amount);
      int interpolatedBlue = MathUtil.interpolateInt(blue1, blue2, (double)amount);
      int interpolatedAlpha = MathUtil.interpolateInt(alpha1, alpha2, (double)amount);
      return interpolatedAlpha << 24 | interpolatedRed << 16 | interpolatedGreen << 8 | interpolatedBlue;
   }

   public static int reAlphaInt(int color, int alpha) {
      return MathHelper.clamp(alpha, 0, 255) << 24 | color & 16777215;
   }

   public static int multRed(int color, float percent01) {
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      int a = color >> 24 & 0xFF;
      g = Math.min(255, Math.round((float)g / percent01));
      b = Math.min(255, Math.round((float)b / percent01));
      return a << 24 | r << 16 | g << 8 | b;
   }
}
