package dev.just.mixin.chat;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.just.manager.ClientManager;
import dev.just.manager.Manager;
import dev.just.manager.commandManager.CommandManager;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandSource;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ChatInputSuggestor.class})
public abstract class MixinChatInputSuggestor {
   @Final
   @Shadow
   TextFieldWidget textField;
   @Shadow
   boolean completingSuggestions;
   @Shadow
   private ParseResults<CommandSource> parse;
   @Shadow
   private CompletableFuture<Suggestions> pendingSuggestions;
   @Shadow
   private ChatInputSuggestor.SuggestionWindow window;

   @Shadow
   protected abstract void showCommandSuggestions();

   @Inject(
      method = {"refresh"},
      at = {@At(
         value = "INVOKE",
         target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
         remap = false
      )},
      cancellable = true,
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   public void refreshHook(CallbackInfo ci, String string, StringReader reader) {
      CommandManager commandManager = Manager.COMMAND_MANAGER;
      if (commandManager == null) return;

      String prefix = commandManager.getPrefix();
      String text = this.textField.getText();

      // "." ile başlıyorsa client komutlarını göster
      if (!ClientManager.legitMode && text.startsWith(prefix)) {
         try {
            // Her seferinde yeniden parse et
            String commandText = text.substring(prefix.length());
            StringReader cmdReader = new StringReader(commandText);
            this.parse = commandManager.getDispatcher().parse(cmdReader, commandManager.getSource());

            int cursor = this.textField.getCursor();
            if (cursor >= 1 && (this.window == null || !this.completingSuggestions)) {
               this.pendingSuggestions = commandManager.getDispatcher().getCompletionSuggestions(this.parse, cursor - prefix.length());
               this.pendingSuggestions.thenRun(() -> {
                  if (this.pendingSuggestions.isDone()) {
                     this.showCommandSuggestions();
                  }
               });
            }

            ci.cancel();
         } catch (Exception e) {
            // Hata olursa sessizce devam et
         }
      }
   }
}
