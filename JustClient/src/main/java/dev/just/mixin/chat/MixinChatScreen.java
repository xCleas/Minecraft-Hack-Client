package dev.just.mixin.chat;

import dev.just.manager.IMinecraft;
import dev.just.manager.dragManager.DragManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChatScreen.class})
public abstract class MixinChatScreen implements IMinecraft {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      DragManager.draggables.values().forEach(dragging -> {
         if (dragging.getModule() != null && dragging.getModule().state) {
            dragging.onDraw((double)mouseX, (double)mouseY, mc.getWindow());
            dragging.renderGuides(mc.getWindow());
         }
      });
   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor;mouseClicked(DDI)Z",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void afterSuggestionsClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      if (!cir.getReturnValueZ()) {
         DragManager.draggables.values().forEach(dragging -> {
            if (dragging.getModule() != null && dragging.getModule().state) {
               dragging.onClick(mouseX, mouseY, button);
            }
         });
      }
   }
}
