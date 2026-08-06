package dev.just.util.math;

import dev.just.manager.IMinecraft;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class MathUtil implements IMinecraft {
   public static double deltaTick() {
      return mc.getCurrentFps() > 0 ? 1.0 / (double)mc.getCurrentFps() : 1.0;
   }

   public static float fast(float end, float start, float multiple) {
      return (1.0F - MathHelper.clamp((float)(deltaTick() * (double)multiple), 0.0F, 1.0F)) * end
         + MathHelper.clamp((float)(deltaTick() * (double)multiple), 0.0F, 1.0F) * start;
   }

   public static float lerp(float end, float start, float multiple) {
      return (float)((double)end + (double)(start - end) * MathHelper.clamp(deltaTick() * (double)multiple, 0.0, 1.0));
   }

   public static double lerp(double end, double start, double multiple) {
      return end + (start - end) * MathHelper.clamp(deltaTick() * multiple, 0.0, 1.0);
   }

   public static float faster(float current, float target, float speed) {
      float diff = target - current;
      float deltaTime = 1.0F / (float)mc.getCurrentFps();
      deltaTime = Math.min(deltaTime, 0.01F);
      float step = speed * deltaTime;
      return Math.abs(diff) <= step ? target : current + Math.copySign(step, diff);
   }

   public static double random(double min, double max) {
      return ThreadLocalRandom.current().nextDouble() * (max - min) + min;
   }

   public static float random(float min, float max) {
      return (float)(Math.random() * (double)(max - min) + (double)min);
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   private static int interpolateInt(int start, int end, float amount) {
      return Math.round((float)start + (float)(end - start) * amount);
   }

   public static float interpolateFloat(float oldValue, float newValue, double delta) {
      return interpolate((double)oldValue, (double)newValue, delta).floatValue();
   }

   public static Double interpolate(double old, double value, double interpolation) {
      return old + (value - old) * interpolation;
   }

   public static float interpolateFloat(float old, float value, float interpolation) {
      return old + (value - old) * interpolation;
   }

   public static float interpolateAngle(float start, float end, float progress) {
      float difference = MathHelper.wrapDegrees(end - start);
      return start + difference * MathHelper.clamp(progress, 0.0F, 1.0F);
   }

   public static float smoothstep(float e0, float e1, float x) {
      float t = MathHelper.clamp((x - e0) / (e1 - e0), 0.0F, 1.0F);
      return t * t * (3.0F - 2.0F * t);
   }

   public static Vec3d getClosestVec(Entity entity) {
      Vec3d eyePosVec = mc.player.getEyePos();
      return getClosestVec(eyePosVec, entity).subtract(eyePosVec);
   }

   public static Vec3d getClosestVec(Vec3d vec, Entity entity) {
      return getClosestVec(vec, entity.getBoundingBox());
   }

   public static Vec3d getClosestVec(Vec3d vec, Box AABB) {
      return new Vec3d(
         clamp(vec.x, AABB.minX, AABB.maxX),
         clamp(vec.getY(), AABB.minY, AABB.maxY),
         clamp(vec.getZ(), AABB.minZ, AABB.maxZ)
      );
   }

   public static double getStrictDistance(Entity entity) {
      return getClosestVec(entity).length();
   }

   public static int getRandom(int min, int max) {
      return (int)getRandom((float)min, (float)max + 1.0F);
   }

   private static float getRandom(float min, float max) {
      return (float)getRandom((double)min, (double)max);
   }

   private static double getRandom(double min, double max) {
      if (min == max) {
         return min;
      } else {
         if (min > max) {
            double d = min;
            min = max;
            max = d;
         }

         return ThreadLocalRandom.current().nextDouble(min, max);
      }
   }

   public static float interpolateSmooth(double smooth, float prev, float orig) {
      return (float)lerp((double)mc.getRenderTickCounter().getLastDuration() / smooth, (double)prev, (double)orig);
   }

   public static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   public static double clamp(double value, double min, double max) {
      return Math.max(min, Math.min(max, value));
   }

   public static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }
}
