package dev.just.mixin.display;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.movement.freelook.CameraOverriddenEntity;
import dev.just.modules.movement.freelook.FreeLookState;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class MixinCamera implements IMinecraft {
   @Unique
   private boolean initialized = false;
   @Shadow
   private boolean thirdPerson;

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(
      method = {"update"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
         shift = Shift.AFTER
      )}
   )
   private void onUpdate(CallbackInfo ci) {
      if (FreeLookState.active && mc.player instanceof CameraOverriddenEntity entity) {
         if (!this.initialized) {
            entity.setCameraPitch(mc.player.getPitch());
            entity.setCameraYaw(mc.player.getYaw());
            this.initialized = true;
         }

         this.setRotation(entity.getCameraYaw(), entity.getCameraPitch());
      }
   }

   @Inject(
      method = {"update"},
      at = {@At("TAIL")}
   )
   private void updateHook(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.freeCamera.state) {
         this.thirdPerson = true;
      }
   }

   @ModifyArgs(
      method = {"update"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
      )
   )
   private void setRotationHook(Args args) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.freeCamera.state) {
         args.setAll(new Object[]{Manager.FUNCTION_MANAGER.freeCamera.getFakeYaw(), Manager.FUNCTION_MANAGER.freeCamera.getFakePitch()});
      }
   }

   @ModifyArgs(
      method = {"update"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"
      )
   )
   private void setPosHook(Args args) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.freeCamera.state) {
         args.setAll(
            new Object[]{
               Manager.FUNCTION_MANAGER.freeCamera.getFakeX(), Manager.FUNCTION_MANAGER.freeCamera.getFakeY(), Manager.FUNCTION_MANAGER.freeCamera.getFakeZ()
            }
         );
      }
   }
}
