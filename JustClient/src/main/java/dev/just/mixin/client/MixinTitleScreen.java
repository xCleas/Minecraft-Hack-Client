package dev.just.mixin.client;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.screens.mainmenu.MainMenu;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public class MixinTitleScreen implements IMinecraft {
   @Inject(
      method = {"init"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onInit(CallbackInfo ci) {
      if (!ClientManager.legitMode) {
         mc.setScreen(new MainMenu());
         ci.cancel();
      }
   }
}
