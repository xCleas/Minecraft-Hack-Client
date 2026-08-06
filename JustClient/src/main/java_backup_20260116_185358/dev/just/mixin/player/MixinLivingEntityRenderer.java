package dev.just.mixin.player;

import dev.just.events.Event;
import dev.just.events.impl.render.EventPlayerRender;
import dev.just.manager.IMinecraft;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntityRenderer.class})
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState> implements IMinecraft {
   @Unique
   private float originalPrevHeadYaw;
   @Unique
   private float originalHeadYaw;
   @Unique
   private float originalPrevHeadPitch;
   @Unique
   private float originalHeadPitch;
   @Unique
   private float originalBodyYaw;
   @Unique
   private float originalPrevBodyYaw;
   @Unique
   private boolean replaced;

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"},
      at = {@At("HEAD")}
   )
   private void onUpdateRenderStatePre(T livingEntity, S state, float tickDelta, CallbackInfo ci) {
      if (mc != null && mc.player != null && livingEntity == mc.player) {
         if (!(mc.currentScreen instanceof InventoryScreen)) {
            EventPlayerRender playerRender = new EventPlayerRender(livingEntity);
            Event.call(playerRender);
            this.originalPrevHeadYaw = livingEntity.prevHeadYaw;
            this.originalHeadYaw = livingEntity.headYaw;
            this.originalPrevHeadPitch = livingEntity.prevPitch;
            this.originalHeadPitch = livingEntity.getPitch();
            this.originalBodyYaw = livingEntity.bodyYaw;
            this.originalPrevBodyYaw = livingEntity.prevBodyYaw;
            livingEntity.prevHeadYaw = playerRender.getPrevYaw();
            livingEntity.headYaw = playerRender.getYaw();
            livingEntity.prevPitch = playerRender.getPrevPitch();
            livingEntity.setPitch(playerRender.getPitch());
            livingEntity.prevBodyYaw = playerRender.getPrevBodyYaw();
            livingEntity.bodyYaw = playerRender.getBodyYaw();
            this.replaced = true;
         }
      }
   }

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"},
      at = {@At("TAIL")}
   )
   private void onUpdateRenderStatePost(T livingEntity, S state, float tickDelta, CallbackInfo ci) {
      if (this.replaced && mc != null && mc.player != null && livingEntity == mc.player) {
         livingEntity.prevHeadYaw = this.originalPrevHeadYaw;
         livingEntity.headYaw = this.originalHeadYaw;
         livingEntity.prevPitch = this.originalPrevHeadPitch;
         livingEntity.setPitch(this.originalHeadPitch);
         livingEntity.prevBodyYaw = this.originalPrevBodyYaw;
         livingEntity.bodyYaw = this.originalBodyYaw;
         this.replaced = false;
      }
   }
}
