package dev.just.mixin.player;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.combat.AutoExplosion;
import dev.just.modules.combat.CrystalAura;
import dev.just.modules.combat.rotation.RotationController;
import dev.just.modules.render.SwingAnimations;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class MixinLivingEntity implements IMinecraft {
   @Inject(
      method = {"getHandSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
      SwingAnimations swingAnimations = Manager.FUNCTION_MANAGER.swingAnimations;
      if (swingAnimations.state && swingAnimations.slowAnimation.get()) {
         info.setReturnValue(swingAnimations.slowAnimationSpeed.get().intValue());
      }
   }

   @Shadow
   public abstract float getJumpVelocity();

   @Shadow
   protected abstract double getGravity();

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onJump(CallbackInfo ci) {
      if ((Object)this == mc.player) {
         AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;
         AutoExplosion autoExplosion = Manager.FUNCTION_MANAGER.autoExplosion;
         RotationController rotationController = Manager.ROTATION;
         CrystalAura crystalAura = Manager.FUNCTION_MANAGER.crystalAura;
         Float yaw = null;
         if ((!attackAura.state || !attackAura.isCorrectionEnabled()) && !rotationController.isControlling()) {
            if (autoExplosion.check()) {
               yaw = autoExplosion.serverRot.x;
            } else if (crystalAura.check()) {
               yaw = crystalAura.rotate.x;
            }
         } else {
            yaw = rotationController.getYaw();
         }

         if (yaw != null) {
            float jumpVelocity = this.getJumpVelocity();
            if (jumpVelocity <= 1.0E-5F) {
               ci.cancel();
            } else {
               Vec3d currentVelocity = mc.player.getVelocity();
               mc.player.setVelocity(currentVelocity.x, Math.max((double)jumpVelocity, currentVelocity.y), currentVelocity.z);
               if (mc.player.isSprinting()) {
                  float yawRad = yaw * (float) (Math.PI / 180.0);
                  double x = (double)(-MathHelper.sin(yawRad)) * 0.2;
                  double z = (double)MathHelper.cos(yawRad) * 0.2;
                  mc.player.addVelocityInternal(new Vec3d(x, 0.0, z));
               }

               mc.player.velocityDirty = true;
               ci.cancel();
            }
         }
      }
   }

   @Inject(
      method = {"calcGlidingVelocity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onCalcGlidingVelocity(Vec3d oldVelocity, CallbackInfoReturnable<Vec3d> cir) {
      if ((Object)this == mc.player) {
         AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;
         RotationController rotationController = Manager.ROTATION;
         if (attackAura.state && attackAura.isCorrectionEnabled() || rotationController.isControlling()) {
            float customYaw = rotationController.getYaw();
            float customPitch = rotationController.getPitch();
            Vec3d rotationVec = Vec3d.fromPolar(customPitch, customYaw);
            float pitchRad = customPitch * (float) (Math.PI / 180.0);
            double horizontalLength = Math.sqrt(rotationVec.x * rotationVec.x + rotationVec.z * rotationVec.z);
            double oldHorizontalLength = oldVelocity.horizontalLength();
            double gravity = this.getGravity();
            double cosSquared = MathHelper.square(Math.cos((double)pitchRad));
            Vec3d newVelocity = oldVelocity.add(0.0, gravity * (-1.0 + cosSquared * 0.75), 0.0);
            if (newVelocity.y < 0.0 && horizontalLength > 0.0) {
               double lift = newVelocity.y * -0.1 * cosSquared;
               newVelocity = newVelocity.add(rotationVec.x * lift / horizontalLength, lift, rotationVec.z * lift / horizontalLength);
            }

            if (pitchRad < 0.0F && horizontalLength > 0.0) {
               double acceleration = oldHorizontalLength * (double)(-MathHelper.sin(pitchRad)) * 0.04;
               newVelocity = newVelocity.add(
                  -rotationVec.x * acceleration / horizontalLength, acceleration * 3.2, -rotationVec.z * acceleration / horizontalLength
               );
            }

            if (horizontalLength > 0.0) {
               newVelocity = newVelocity.add(
                  (rotationVec.x / horizontalLength * oldHorizontalLength - newVelocity.x) * 0.1,
                  0.0,
                  (rotationVec.z / horizontalLength * oldHorizontalLength - newVelocity.z) * 0.1
               );
            }

            newVelocity = newVelocity.multiply(0.99, 0.98, 0.99);
            cir.setReturnValue(newVelocity);
            cir.cancel();
         }
      }
   }
}
