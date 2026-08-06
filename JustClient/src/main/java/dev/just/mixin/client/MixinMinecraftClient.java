package dev.just.mixin.client;

import dev.just.JustClient;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin({MinecraftClient.class})
public abstract class MixinMinecraftClient {
   @Inject(
      method = {"isMultiplayerEnabled"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isMultiplayerEnabled(CallbackInfoReturnable<Boolean> cir) {
      cir.setReturnValue(true);
   }

   @Inject(
      method = {"getWindowTitle"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getWindowTitle(CallbackInfoReturnable<String> cir) {
      if (!ClientManager.legitMode) {
         String name = Manager.USER_PROFILE != null ? Manager.USER_PROFILE.getName() : "JustPlayer";
         cir.setReturnValue("Just Client 1.21.4 Fabric | " + name);
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"stop"}
   )
   private void stop(CallbackInfo ci) {
      JustClient.getInstance().shutDown();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void init(CallbackInfo callbackInfo) {
      JustClient.getInstance().init();
   }
}
