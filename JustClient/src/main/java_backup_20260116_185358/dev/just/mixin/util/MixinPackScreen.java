package dev.just.mixin.util;

import dev.just.manager.ClientManager;
import dev.just.manager.commandManager.impl.UnHookCommand;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.gui.screen.pack.PackScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.screen.pack.PackScreen.class)
public abstract class MixinPackScreen {
   @Shadow
   @Final
   @Mutable
   private Path file;

   @Inject(
      method = {"init"},
      at = {@At("HEAD")}
   )
   private void onInit(CallbackInfo ci) {
      if (ClientManager.legitMode) {
         try {
            File customFile = UnHookCommand.CUSTOM_PATH_FILE;
            if (customFile.exists()) {
               String content = Files.readString(customFile.toPath()).trim();
               if (!content.isEmpty()) {
                  Path customPath = Path.of(content);
                  if (Files.exists(customPath)) {
                     this.file = customPath;
                     System.out.println("Legit mod: Kaynak paketi yolu degistirildi: " + customPath);
                  } else {
                     System.err.println("Dosyadaki yol mevcut degil: " + customPath);
                  }
               }
            }
         } catch (Exception var5) {
            System.err.println("Kaynak paketi yolu degistirilirken hata: " + var5.getMessage());
         }
      }
   }
}
