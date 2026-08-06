package dev.just.screens.unhook;

import dev.just.manager.ClientManager;
import dev.just.manager.IMinecraft;
import dev.just.manager.Manager;
import dev.just.modules.misc.UnHook;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public class UnHookScreen extends Screen implements IMinecraft {
   private final UnHook unHookFunction = Manager.FUNCTION_MANAGER.unHook;

   public UnHookScreen() {
      super(Text.literal("UnHook"));
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      this.handleTimers();
   }

   private void handleTimers() {
      ClientManager.legitMode = true;
      this.unHookFunction.onUnhook();
      mc.setScreen(null);
   }
}
