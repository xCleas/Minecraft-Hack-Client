package dev.just.mixin.chat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.CommandManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPlayNetworkHandler.class})
public class MixinClientPlayNetworkHandler {
   @Inject(
      method = {"sendChatMessage"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendChatMessageHook(@NotNull String message, CallbackInfo ci) {
      if (!ClientManager.legitMode) {
         CommandManager commandManager = Manager.COMMAND_MANAGER;
         if (commandManager != null && message.startsWith(commandManager.getPrefix())) {
            try {
               commandManager.getDispatcher().execute(message.substring(commandManager.getPrefix().length()), commandManager.getSource());
            } catch (CommandSyntaxException var5) {
            }

            ci.cancel();
         }
      }
   }
}
