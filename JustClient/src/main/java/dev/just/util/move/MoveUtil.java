package dev.just.util.move;

import dev.just.events.impl.input.EventKeyBoard;
import dev.just.manager.IMinecraft;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public class MoveUtil implements IMinecraft {
   public static boolean isMoving() {
      return mc.player != null
         && mc.world != null
         && mc.player.input != null
         && ((double)mc.player.input.movementForward != 0.0 || (double)mc.player.input.movementSideways != 0.0);
   }

   public static boolean isInWeb() {
      Box box = mc.player.getBoundingBox();

      for (int x = MathHelper.floor(box.minX); x <= MathHelper.floor(box.maxX); x++) {
         for (int y = MathHelper.floor(box.minY); y <= MathHelper.floor(box.maxY); y++) {
            for (int z = MathHelper.floor(box.minZ); z <= MathHelper.floor(box.maxZ); z++) {
               BlockPos blockPos = new BlockPos(x, y, z);
               if (mc.world.getBlockState(blockPos).isOf(Blocks.COBWEB)) {
                  Box blockBox = new Box(blockPos);
                  if (blockBox.intersects(box)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static void setSpeed(float motion) {
      float forward = mc.player.input.movementForward;
      float strafe = mc.player.input.movementSideways;
      float yaw = mc.player.getYaw();
      if (forward == 0.0F && strafe == 0.0F) {
         mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
      } else {
         if (forward != 0.0F) {
            if (strafe > 0.0F) {
               yaw += forward > 0.0F ? -45.0F : 45.0F;
            } else if (strafe < 0.0F) {
               yaw += forward > 0.0F ? 45.0F : -45.0F;
            }

            strafe = 0.0F;
            forward = forward > 0.0F ? 1.0F : -1.0F;
         }

         double rad = Math.toRadians((double)(yaw + 90.0F));
         double x = (double)(forward * motion) * Math.cos(rad) + (double)(strafe * motion) * Math.sin(rad);
         double z = (double)(forward * motion) * Math.sin(rad) - (double)(strafe * motion) * Math.cos(rad);
         mc.player.setVelocity(x, mc.player.getVelocity().y, z);
      }
   }

   public static void fixMovement(EventKeyBoard event, float yaw) {
      float forward = event.getMovementForward();
      float strafe = event.getMovementStrafe();
      double angle = MathHelper.wrapDegrees(
         Math.toDegrees(direction(mc.player.isGliding() ? yaw : mc.player.getYaw(), (double)forward, (double)strafe))
      );
      if (forward != 0.0F || strafe != 0.0F) {
         float closestForward = 0.0F;
         float closestStrafe = 0.0F;
         float closestDifference = Float.MAX_VALUE;

         for (float predictedForward = -1.0F; predictedForward <= 1.0F; predictedForward++) {
            for (float predictedStrafe = -1.0F; predictedStrafe <= 1.0F; predictedStrafe++) {
               if (predictedStrafe != 0.0F || predictedForward != 0.0F) {
                  double predictedAngle = MathHelper.wrapDegrees(Math.toDegrees(direction(yaw, (double)predictedForward, (double)predictedStrafe)));
                  double difference = Math.abs(angle - predictedAngle);
                  if (difference < (double)closestDifference) {
                     closestDifference = (float)difference;
                     closestForward = predictedForward;
                     closestStrafe = predictedStrafe;
                  }
               }
            }
         }

         event.setMovementForward(closestForward);
         event.setMovementStrafe(closestStrafe);
      }
   }

   public static double direction(float rotationYaw, double moveForward, double moveStrafing) {
      if (moveForward < 0.0) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (moveForward < 0.0) {
         forward = -0.5F;
      } else if (moveForward > 0.0) {
         forward = 0.5F;
      }

      if (moveStrafing > 0.0) {
         rotationYaw -= 90.0F * forward;
      }

      if (moveStrafing < 0.0) {
         rotationYaw += 90.0F * forward;
      }

      return Math.toRadians((double)rotationYaw);
   }

   public static double getSpeed() {
      return Math.hypot(mc.player.getVelocity().x, mc.player.getVelocity().z);
   }

   public static double[] forward(double d) {
      float f = mc.player.input.movementForward;
      float f2 = mc.player.input.movementSideways;
      float f3 = mc.player.getYaw();
      if (f != 0.0F) {
         if (f2 > 0.0F) {
            f3 += (float)(f > 0.0F ? -45 : 45);
         } else if (f2 < 0.0F) {
            f3 += (float)(f > 0.0F ? 45 : -45);
         }

         f2 = 0.0F;
         if (f > 0.0F) {
            f = 1.0F;
         } else if (f < 0.0F) {
            f = -1.0F;
         }
      }

      double d2 = Math.sin(Math.toRadians((double)(f3 + 90.0F)));
      double d3 = Math.cos(Math.toRadians((double)(f3 + 90.0F)));
      double d4 = (double)f * d * d3 + (double)f2 * d * d2;
      double d5 = (double)f * d * d2 - (double)f2 * d * d3;
      return new double[]{d4, d5};
   }

   public static void setMotion(double speed) {
      double forward = (double)mc.player.input.movementForward;
      double strafe = (double)mc.player.input.movementSideways;
      float yaw = mc.player.getYaw();
      if (forward == 0.0 && strafe == 0.0) {
         mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
      } else {
         if (forward != 0.0) {
            if (strafe > 0.0) {
               yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
               yaw += (float)(forward > 0.0 ? 45 : -45);
            }

            strafe = 0.0;
            if (forward > 0.0) {
               forward = 1.0;
            } else if (forward < 0.0) {
               forward = -1.0;
            }
         }

         double sin = (double)MathHelper.sin((float)Math.toRadians((double)(yaw + 90.0F)));
         double cos = (double)MathHelper.cos((float)Math.toRadians((double)(yaw + 90.0F)));
         mc.player
            .setVelocity(forward * speed * cos + strafe * speed * sin, mc.player.getVelocity().y, forward * speed * sin - strafe * speed * cos);
      }
   }
}
