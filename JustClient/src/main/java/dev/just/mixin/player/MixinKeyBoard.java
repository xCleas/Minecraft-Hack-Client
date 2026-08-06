package dev.just.mixin.player;

import dev.just.JustClient;
import dev.just.manager.IMinecraft;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Keyboard.class})
public class MixinKeyBoard implements IMinecraft {
   @Inject(
      method = {"onKey"},
      at = {@At("HEAD")}
   )
   private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      if (!(mc.currentScreen instanceof Screen)) {
         JustClient main = JustClient.getInstance();
         if (main == null) main = new JustClient();

         if (action == 1) {
            // Key pressed
            main.keyPress(key);
         } else if (action == 0) {
            // Key released
            main.keyRelease(key);
         }
      }
   }
}
