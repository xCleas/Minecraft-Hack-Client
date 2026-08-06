package dev.just.mixin.world;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.util.render.providers.ResourceProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({AbstractClientPlayerEntity.class})
public class MixinAbstractClientPlayerEntity implements IMinecraft {
   private String cachedPlayerName;

   @Inject(
      method = {"getSkinTextures"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void injectGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
      if (!ClientManager.legitMode) {
         AbstractClientPlayerEntity player = (AbstractClientPlayerEntity)(Object)this;
         String playerName = player.getName().getString();
         if (this.cachedPlayerName == null) {
            this.cachedPlayerName = mc.player.getName().getString();
         }

         if (Manager.FRIEND_MANAGER.isFriend(playerName) || playerName.equalsIgnoreCase(this.cachedPlayerName)) {
            SkinTextures original = (SkinTextures)cir.getReturnValue();
            SkinTextures newTextures = new SkinTextures(
               original.texture(),
               original.textureUrl(),
               ResourceProvider.CUSTOM_CAPE,
               ResourceProvider.CUSTOM_ELYTRA,
               original.model(),
               original.secure()
            );
            cir.setReturnValue(newTextures);
         }
      }
   }
}
