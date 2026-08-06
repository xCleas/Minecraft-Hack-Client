package dev.just.mixin.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.just.events.Event;
import dev.just.events.impl.EventUpdate;
import dev.just.events.impl.move.EventMotion;
import dev.just.events.impl.move.EventNoSlow;
import dev.just.events.impl.player.EventSprint;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayerEntity.class})
public abstract class MixinClientPlayerEntity implements IMinecraft {
   @Unique
   private float preYaw;
   @Unique
   private float prePitch;
   @Unique
   private float packetYaw;
   @Unique
   private float packetPitch;

   @Shadow
   public abstract void move(MovementType var1, Vec3d var2);

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void onTickHead(CallbackInfo ci) {
      if (mc.player == null || mc.world == null) return;
      Event.call(new EventUpdate());
      this.preYaw = mc.player.getYaw();
      this.prePitch = mc.player.getPitch();
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendMovementPacketsHead(CallbackInfo ci) {
      EventMotion event = new EventMotion(
         mc.player.getX(),
         mc.player.getY(),
         mc.player.getZ(),
         mc.player.getYaw(),
         mc.player.getPitch(),
         mc.player.isOnGround()
      );
      Event.call(event);
      if (event.isCancel()) {
         ci.cancel();
      } else {
         mc.player.setYaw(event.getYaw());
         mc.player.setPitch(event.getPitch());
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V",
         shift = Shift.AFTER
      )}
   )
   private void afterSendMovementPackets(CallbackInfo ci) {
      this.packetYaw = mc.player.getYaw();
      this.packetPitch = mc.player.getPitch();
      mc.player.setYaw(this.preYaw);
      mc.player.setPitch(this.prePitch);
   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocksHook(double x, double d, CallbackInfo ci) {
      if (Manager.FUNCTION_MANAGER != null && Manager.FUNCTION_MANAGER.noPush != null
          && Manager.FUNCTION_MANAGER.noPush.state && Manager.FUNCTION_MANAGER.noPush.mods.get("Bloklar")) {
         ci.cancel();
      }
   }

   private boolean checkNoSlowCancel() {
      EventNoSlow eventNoSlow = new EventNoSlow();
      Event.call(eventNoSlow);
      return eventNoSlow.isCancel();
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/input/Input;movementSideways:F",
         opcode = 181,
         ordinal = 0
      )
   )
   private void redirectMovementSideways(Input input, float value) {
      if (!this.checkNoSlowCancel()) {
         input.movementSideways = value;
      }
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/input/Input;movementForward:F",
         opcode = 181,
         ordinal = 0
      )
   )
   private void redirectMovementForward(Input input, float value) {
      if (!this.checkNoSlowCancel()) {
         input.movementForward = value;
      }
   }

   @Redirect(
      method = {"tickMovement"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V",
         ordinal = 0
      )
   )
   private void redirectSetSprinting(ClientPlayerEntity player, boolean sprinting) {
      if (!this.checkNoSlowCancel()) {
         player.setSprinting(sprinting);
      }
   }

   @ModifyExpressionValue(
      method = {"tickMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"
      )}
   )
   private boolean hookSprintStart(boolean original) {
      EventSprint event = new EventSprint(original);
      return event.isSprinting();
   }

   @ModifyExpressionValue(
      method = {"tickMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;canSprint()Z"
      )}
   )
   private boolean hookSprintStop(boolean original) {
      EventSprint event = new EventSprint(original);
      Event.call(event);
      return event.isSprinting();
   }
}
