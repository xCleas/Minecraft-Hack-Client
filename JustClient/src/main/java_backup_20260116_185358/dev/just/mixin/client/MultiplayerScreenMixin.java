package dev.just.mixin.client;

import dev.just.manager.Manager;
import dev.just.manager.proxyManager.GuiProxy;
import dev.just.manager.proxyManager.Proxy;
import dev.just.manager.proxyManager.ProxyManager;
import dev.just.mixin.iface.ScreenAccessor;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MultiplayerScreen.class})
public class MultiplayerScreenMixin {
   @Inject(
      method = {"init()V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screen/multiplayer/MultiplayerScreen;updateButtonActivationStates()V"
      )}
   )
   public void multiplayerGuiOpen(CallbackInfo ci) {
      ProxyManager pm = Manager.PROXY_MANAGER;
      String playerName = ProxyManager.mc.getSession().getUsername();
      if (!playerName.equals(pm.lastPlayerName)) {
         pm.lastPlayerName = playerName;
         ProxyManager.proxy = pm.accounts.getOrDefault(playerName, pm.accounts.getOrDefault("", new Proxy()));
      }

      MultiplayerScreen screen = (MultiplayerScreen)(Object)this;
      ProxyManager.proxyMenuButton = ButtonWidget.builder(
            Text.literal("Proxy: " + ProxyManager.getLastUsedProxyIp()), b -> ProxyManager.mc.setScreen(new GuiProxy(screen))
         )
         .dimensions(screen.width - 320, 479, 100, 20)
         .build();
      ScreenAccessor sa = (ScreenAccessor)screen;
      sa.getDrawables().add(ProxyManager.proxyMenuButton);
      sa.getSelectables().add(ProxyManager.proxyMenuButton);
      sa.getChildren().add(ProxyManager.proxyMenuButton);
   }
}
