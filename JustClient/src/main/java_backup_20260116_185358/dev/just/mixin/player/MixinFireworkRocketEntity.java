package dev.just.mixin.player;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.combat.rotation.RotationController;
import dev.just.modules.movement.SuperFirework;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FireworkRocketEntity.class})
public abstract class MixinFireworkRocketEntity implements IMinecraft {
   @Redirect(
      method = {"tick"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
      )
   )
   private void tick(LivingEntity shooter, Vec3d originalVelocity) {
      if (shooter.isGliding() && shooter == mc.player) {
         SuperFirework superFirework = Manager.FUNCTION_MANAGER.superFirework;
         AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;
         RotationController rotationController = Manager.ROTATION;
         Vec3d rotationVector;
         if ((!attackAura.state || attackAura.getTarget() == null || !attackAura.isCorrectionEnabled()) && !rotationController.isControlling()) {
            rotationVector = shooter.getRotationVecClient();
         } else {
            rotationVector = Vec3d.fromPolar(Manager.ROTATION.getPitch(), Manager.ROTATION.getYaw());
         }

         double speedXZ = 1.5;
         double speedY = 1.5;
         if (superFirework.state) {
            float yaw = (!attackAura.state || attackAura.getTarget() == null) && !rotationController.isControlling()
               ? mc.player.getYaw() % 360.0F
               : Manager.ROTATION.getYaw() % 360.0F;
            boolean isDiagonal = false;
            boolean nearPlayer = false;
            if (yaw < 0.0F) {
               yaw += 360.0F;
            }

            if (superFirework.mode.is("ReallyWorld")) {
               float[] diagonals = new float[]{45.0F, 135.0F, 225.0F, 315.0F};
               float closestDiff = 180.0F;

               for (float d : diagonals) {
                  float diff = Math.abs(yaw - d);
                  diff = Math.min(diff, 360.0F - diff);
                  if (diff < closestDiff) {
                     closestDiff = diff;
                  }
               }

               if (closestDiff <= superFirework.diag1) {
                  speedXZ = (double)superFirework.speedD_1;
               } else if (closestDiff <= superFirework.diag2) {
                  speedXZ = (double)superFirework.speedD_2;
               } else if (closestDiff <= superFirework.diag3) {
                  speedXZ = (double)superFirework.speedD_3;
               } else if (closestDiff <= superFirework.diag4) {
                  speedXZ = (double)superFirework.speedD_4;
               } else if (closestDiff <= superFirework.diag5) {
                  speedXZ = (double)superFirework.speedD_5;
               } else if (closestDiff <= superFirework.diag6) {
                  speedXZ = (double)superFirework.speedD_6;
               } else if (closestDiff <= superFirework.diag7) {
                  speedXZ = (double)superFirework.speedD_7;
               } else if (closestDiff <= superFirework.diag8) {
                  speedXZ = (double)superFirework.speedD_8;
               } else if (closestDiff <= superFirework.diag9) {
                  speedXZ = (double)superFirework.speedD_9;
               } else {
                  speedXZ = (double)superFirework.speedXZ;
                  speedY = (double)superFirework.speedY;
               }
            }

            if (superFirework.mode.is("BravoHvH")) {
               for (float dx : new float[]{45.0F, 135.0F, 225.0F, 315.0F}) {
                  float diff = Math.abs(yaw - dx);
                  diff = Math.min(diff, 360.0F - diff);
                  if (diff <= 16.0F) {
                     isDiagonal = true;
                     break;
                  }
               }

               if (superFirework.nearBoost.get()) {
                  for (PlayerEntity p : Manager.SYNC_MANAGER.getPlayers()) {
                     if (p != mc.player && p.distanceTo(mc.player) <= 4.0F) {
                        nearPlayer = true;
                        break;
                     }
                  }
               }

               if (isDiagonal) {
                  speedXZ = 1.963;
               } else if (superFirework.nearBoost.get() && nearPlayer) {
                  speedXZ = 1.82;
                  speedY = 1.67;
               } else {
                  speedXZ = 1.675;
                  speedY = 1.66;
               }
            }

            if (superFirework.mode.is("PulseHVH")) {
               for (float dxx : new float[]{45.0F, 135.0F, 225.0F, 315.0F}) {
                  float diff = Math.abs(yaw - dxx);
                  diff = Math.min(diff, 360.0F - diff);
                  if (diff <= 16.0F) {
                     isDiagonal = true;
                     break;
                  }
               }

               if (superFirework.nearBoost.get()) {
                  for (PlayerEntity px : Manager.SYNC_MANAGER.getPlayers()) {
                     if (px != mc.player && px.distanceTo(mc.player) <= 5.0F) {
                        nearPlayer = true;
                        break;
                     }
                  }
               }

               if (isDiagonal) {
                  speedXZ = 1.963;
               } else if (superFirework.nearBoost.get() && nearPlayer) {
                  speedXZ = 1.82;
                  speedY = 1.67;
               } else {
                  speedXZ = 1.675;
                  speedY = 1.66;
               }
            }

            if (superFirework.mode.is("Custom")) {
               for (float dxxx : new float[]{45.0F, 135.0F, 225.0F, 315.0F}) {
                  float diff = Math.abs(yaw - dxxx);
                  diff = Math.min(diff, 360.0F - diff);
                  if (diff <= 16.0F) {
                     isDiagonal = true;
                     break;
                  }
               }

               if (superFirework.nearBoost.get()) {
                  for (PlayerEntity pxx : Manager.SYNC_MANAGER.getPlayers()) {
                     if (pxx != mc.player && pxx.distanceTo(mc.player) <= 5.0F) {
                        nearPlayer = true;
                        break;
                     }
                  }
               }

               if (isDiagonal) {
                  speedXZ = (double)superFirework.speed.get().floatValue();
               } else if (superFirework.nearBoost.get() && nearPlayer) {
                  speedXZ = (double)(superFirework.speed.get().floatValue() - 0.1F);
                  speedY = 1.67;
               } else {
                  speedXZ = 1.675;
                  speedY = 1.66;
               }
            }
         }

         Vec3d vec3d = shooter.getVelocity();
         shooter.setVelocity(
            vec3d.add(
               rotationVector.x * 0.1 + (rotationVector.x * speedXZ - vec3d.x) * 0.5,
               rotationVector.y * 0.1 + (rotationVector.y * speedY - vec3d.y) * 0.5,
               rotationVector.z * 0.1 + (rotationVector.z * speedXZ - vec3d.z) * 0.5
            )
         );
      } else {
         shooter.setVelocity(originalVelocity);
      }
   }
}
