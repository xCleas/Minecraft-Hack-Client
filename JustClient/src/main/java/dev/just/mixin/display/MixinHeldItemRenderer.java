package dev.just.mixin.display;

import dev.just.manager.Manager;
import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HeldItemRenderer.class})
public abstract class MixinHeldItemRenderer {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderItemHook(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (!item.isEmpty() && !(item.getItem() instanceof FilledMapItem) && Manager.FUNCTION_MANAGER != null
          && Manager.FUNCTION_MANAGER.swingAnimations != null) {
         ci.cancel();
         Manager.FUNCTION_MANAGER
            .swingAnimations
            .renderFirstPersonItem(player, tickDelta, pitch, hand, swingProgress, item, equipProgress, matrices, vertexConsumers, light);
      }
   }
}
