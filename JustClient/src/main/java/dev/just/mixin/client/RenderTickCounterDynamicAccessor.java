package dev.just.mixin.client;

import dev.just.manager.ClientManager;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.render.RenderTickCounter$Dynamic")
public class RenderTickCounterDynamicAccessor {
   @Shadow
   private float lastFrameDuration;
   @Shadow
   private float tickDelta;
   @Shadow
   private long prevTimeMillis;
   @Final
   @Shadow
   private float tickTime;

   @Inject(
      method = {"Lnet/minecraft/client/render/RenderTickCounter$Dynamic;beginRenderTick(J)I"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void beginRenderTickHook(long timeMillis, CallbackInfoReturnable<Integer> cir) {
      if (ClientManager.TICK_TIMER != 1.0F) {
         this.lastFrameDuration = (float)(timeMillis - this.prevTimeMillis) / this.tickTime * ClientManager.TICK_TIMER;
         this.prevTimeMillis = timeMillis;
         this.tickDelta = this.tickDelta + this.lastFrameDuration;
         int i = (int)this.tickDelta;
         this.tickDelta -= (float)i;
         cir.setReturnValue(i);
      }
   }
}
