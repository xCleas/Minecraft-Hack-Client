package dev.just.mixin.display;

import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.util.player.TimerUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HandledScreen.class})
public abstract class MixinHandledScreen<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T>, IMinecraft {
   @Unique
   private final TimerUtil timerUtil = new TimerUtil();
   @Shadow
   @Nullable
   protected Slot focusedSlot;

   protected MixinHandledScreen(Text title) {
      super(title);
   }

   @Shadow
   protected abstract void onMouseClick(Slot var1, int var2, int var3, SlotActionType var4);

   @Inject(
      method = {"drawMouseoverTooltip"},
      at = {@At("HEAD")}
   )
   private void onDrawMouseoverTooltip(DrawContext context, int x, int y, CallbackInfo ci) {
      if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
         long windowHandle = mc.getWindow().getHandle();
         boolean leftMousePressed = GLFW.glfwGetMouseButton(windowHandle, 0) == 1;
         boolean shiftPressed = InputUtil.isKeyPressed(windowHandle, 340) || InputUtil.isKeyPressed(windowHandle, 344);
         if (Manager.FUNCTION_MANAGER.itemScroller != null
            && Manager.FUNCTION_MANAGER.itemScroller.state
            && leftMousePressed
            && shiftPressed
            && this.client.currentScreen != null
            && this.timerUtil.hasTimeElapsed(Manager.FUNCTION_MANAGER.itemScroller.scroll.get().longValue())
            && this.focusedSlot.hasStack()) {
            this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.QUICK_MOVE);
            this.timerUtil.reset();
         }
      }
   }
}
