package dev.just.util.player;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.attribute.EntityAttributes;
import org.joml.Vector3d;

public class AuraUtil implements IMinecraft {
   public static Vec3d getVelocityTowards(Entity target, double speed, boolean defaultSpeed, boolean predict) {
      if (target != null && mc.player != null) {
         double finalSpeed = defaultSpeed ? mc.player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).getValue() : speed;
         Vec3d look = target.getRotationVector();
         double predictDistance = predict ? (double)Manager.FUNCTION_MANAGER.targetStrafe.predict.get().floatValue() : 0.0;
         double dx = target.getX() + look.x * predictDistance - mc.player.getX();
         double dz = target.getZ() + look.z * predictDistance - mc.player.getZ();
         double distance = Math.sqrt(dx * dx + dz * dz);
         if (distance == 0.0) {
            return Vec3d.ZERO;
         } else {
            double vx = dx / distance * finalSpeed;
            double vz = dz / distance * finalSpeed;
            return new Vec3d(vx, 0.0, vz);
         }
      } else {
         return Vec3d.ZERO;
      }
   }

   public static float getArmor(LivingEntity entity) {
      return entity instanceof PlayerEntity player ? (float)player.getArmor() : 0.0F;
   }

   public static double getDistance(LivingEntity entity) {
      return getVector(entity).length();
   }

   public static Vector3d getVector(LivingEntity target) {
      Vec3d basePos = target.getPos();
      double wHalf = (double)target.getWidth() / 2.0;
      double yMin = basePos.y;
      double yMax = basePos.y + (double)target.getHeight();
      double playerEyeY = mc.player.getEyeY();
      int steps = 10;
      Vector3d bestVector = null;
      double bestDistance = Double.MAX_VALUE;

      for (int i = 0; i <= steps; i++) {
         double y = yMin + (yMax - yMin) * ((double)i / (double)steps);
         double xOffset = Math.clamp(mc.player.getX() - basePos.x, -wHalf, wHalf);
         double zOffset = Math.clamp(mc.player.getZ() - basePos.z, -wHalf, wHalf);
         Vector3d currentVector = new Vector3d(
            basePos.x - mc.player.getX() + xOffset, y - playerEyeY, basePos.z - mc.player.getZ() + zOffset
         );
         double distance = currentVector.length();
         if (distance < bestDistance) {
            bestDistance = distance;
            bestVector = currentVector;
         }
      }

      if (bestVector == null) {
         bestVector = new Vector3d(
            basePos.x - mc.player.getX(), (yMin + yMax) / 2.0 - playerEyeY, basePos.z - mc.player.getZ()
         );
      }

      return bestVector;
   }
}
