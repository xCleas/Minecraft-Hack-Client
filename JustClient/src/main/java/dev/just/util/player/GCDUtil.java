package dev.just.util.player;

import dev.just.manager.IMinecraft;

public class GCDUtil implements IMinecraft {
   public static float getSensitivity(float rotation) {
      float gcdValue = getGCDValue();
      return getDeltaMouse(rotation, gcdValue) * gcdValue;
   }

   public static float getGCDValue() {
      return getGCD() * 0.15F;
   }

   public static float getGCD() {
      float sens = (float)((Double)mc.options.getMouseSensitivity().getValue() * 0.6 + 0.2);
      return sens * sens * sens * 8.0F;
   }

   public static float getDeltaMouse(float delta, float gcdValue) {
      return (float)Math.round(delta / gcdValue);
   }
}
