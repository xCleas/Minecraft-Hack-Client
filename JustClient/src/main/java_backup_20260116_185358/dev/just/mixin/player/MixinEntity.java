package dev.just.mixin.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.combat.AttackAura;
import dev.just.modules.combat.AutoExplosion;
import dev.just.modules.combat.CrystalAura;
import dev.just.modules.combat.HitBox;
import dev.just.modules.combat.TargetStrafe;
import dev.just.modules.combat.rotation.RotationController;
import dev.just.modules.movement.freelook.CameraOverriddenEntity;
import dev.just.modules.movement.freelook.FreeLookState;
import dev.just.modules.player.NoPush;
import dev.just.modules.render.Trails;
import dev.just.util.IEntity;
import dev.just.util.player.AuraUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Entity.class})
public abstract class MixinEntity implements IEntity, CameraOverriddenEntity, IMinecraft {
   @Unique
   private float cameraYaw;
   @Unique
   private float cameraPitch;
   @Unique
   private List<Trails.Trail> trails = new ArrayList<>();
   @Unique
   private Vec3d lastTrailPos;
   @Shadow
   private Box boundingBox;

   @Shadow
   protected static Vec3d movementInputToVelocity(Vec3d movementInput, float speed, float yaw) {
      double d = movementInput.lengthSquared();
      if (d < 1.0E-7) {
         return Vec3d.ZERO;
      } else {
         Vec3d vec3d = (d > 1.0 ? movementInput.normalize() : movementInput).multiply((double)speed);
         float sin = MathHelper.sin(yaw * (float) (Math.PI / 180.0));
         float cos = MathHelper.cos(yaw * (float) (Math.PI / 180.0));
         return new Vec3d(
            vec3d.x * (double)cos - vec3d.z * (double)sin, vec3d.y, vec3d.z * (double)cos + vec3d.x * (double)sin
         );
      }
   }

   @Inject(
      method = {"changeLookDirection"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onChangeLookDirection(double deltaX, double deltaY, CallbackInfo ci) {
      Entity self = (Entity)(Object)this;
      if (FreeLookState.active && self instanceof ClientPlayerEntity) {
         this.cameraYaw += (float)deltaX * 0.15F;
         this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float)deltaY * 0.15F, -90.0F, 90.0F);
         ci.cancel();
      }
   }

   @Override
   public float getCameraPitch() {
      return this.cameraPitch;
   }

   @Override
   public float getCameraYaw() {
      return this.cameraYaw;
   }

   @Override
   public void setCameraPitch(float pitch) {
      this.cameraPitch = pitch;
   }

   @Override
   public void setCameraYaw(float yaw) {
      this.cameraYaw = yaw;
   }

   @ModifyArgs(
      method = {"pushAwayFrom"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"
      )
   )
   private void pushAwayFromHook(Args args) {
      Entity self = (Entity)(Object)this;
      NoPush noPush = Manager.FUNCTION_MANAGER.noPush;
      if (self == mc.player && noPush.state && noPush.mods.get("Oyuncular")) {
         args.set(0, 0.0);
         args.set(1, 0.0);
         args.set(2, 0.0);
      }
   }

   @Override
   public List<Trails.Trail> justClientFabric1_21_4$getTrails() {
      return this.trails;
   }

   @Override
   public Vec3d justClientFabric1_21_4$getLastTrailPos() {
      return this.lastTrailPos;
   }

   @Override
   public void justClientFabric1_21_4$setLastTrailPos(Vec3d pos) {
      this.lastTrailPos = pos;
   }

   @ModifyExpressionValue(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z"
      )}
   )
   private boolean fixFallDistanceCalculation(boolean original) {
      Entity self = (Entity)(Object)this;
      return self != mc.player && original;
   }

   @Inject(
      method = {"getBoundingBox"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void getBoundingBox(CallbackInfoReturnable<Box> cir) {
      Entity self = (Entity)(Object)this;
      HitBox hitBox = Manager.FUNCTION_MANAGER.xbox;
      if (hitBox.state && mc != null && mc.player != null && self.getId() != mc.player.getId()) {
         float halfSize = hitBox.size.get().floatValue() / 2.0F;
         Box expanded = new Box(
            this.boundingBox.minX - (double)halfSize,
            this.boundingBox.minY - (double)halfSize,
            this.boundingBox.minZ - (double)halfSize,
            this.boundingBox.maxX + (double)halfSize,
            this.boundingBox.maxY + (double)halfSize,
            this.boundingBox.maxZ + (double)halfSize
         );
         cir.setReturnValue(expanded);
      }
   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
      Entity self = (Entity)(Object)this;
      if (self == mc.player) {
         Vec3d customVelocity = null;
         AttackAura attackAura = Manager.FUNCTION_MANAGER.attackAura;
         AutoExplosion autoExplosion = Manager.FUNCTION_MANAGER.autoExplosion;
         CrystalAura crystalAura = Manager.FUNCTION_MANAGER.crystalAura;
         TargetStrafe targetStrafe = Manager.FUNCTION_MANAGER.targetStrafe;
         RotationController rotationController = Manager.ROTATION;
         boolean correcting = attackAura.state && attackAura.isCorrectionEnabled() || rotationController.isControlling();
         if (correcting) {
            float yaw = rotationController.getYaw();
            if (targetStrafe.state && attackAura.getTarget() != null) {
               Entity target = attackAura.getTarget();
               boolean nearTarget = mc.player.getBoundingBox().expand((double)targetStrafe.blocks.get().floatValue()).intersects(target.getBoundingBox());
               boolean insideHitbox = mc.player.getBoundingBox().expand((double)targetStrafe.hitbox.get().floatValue()).intersects(target.getBoundingBox());
               if (nearTarget) {
                  if (insideHitbox) {
                     customVelocity = AuraUtil.getVelocityTowards(
                        target, (double)targetStrafe.speedSlider.get().floatValue(), false, targetStrafe.predictView.get()
                     );
                  } else if (targetStrafe.ptytag.is("Motion / Velocity")) {
                     mc.options.forwardKey.setPressed(true);
                     customVelocity = movementInputToVelocity(movementInput, speed, yaw);
                  } else {
                     customVelocity = AuraUtil.getVelocityTowards(target, 0.08F, false, targetStrafe.predictView.get());
                  }
               } else {
                  customVelocity = movementInputToVelocity(movementInput, speed, yaw);
               }
            } else {
               customVelocity = movementInputToVelocity(movementInput, speed, yaw);
            }
         } else if (autoExplosion.check()) {
            customVelocity = movementInputToVelocity(movementInput, speed, autoExplosion.serverRot.x);
         } else if (crystalAura.check()) {
            customVelocity = movementInputToVelocity(movementInput, speed, crystalAura.rotate.x);
         }

         if (customVelocity != null) {
            self.setVelocity(self.getVelocity().add(customVelocity));
            ci.cancel();
         }
      }
   }
}
