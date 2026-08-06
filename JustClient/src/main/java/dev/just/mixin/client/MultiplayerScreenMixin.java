package dev.just.mixin.client;

import dev.just.manager.Manager;
import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.fontManager.RenderFonts;
import dev.just.manager.proxyManager.GuiProxy;
import dev.just.manager.proxyManager.Proxy;
import dev.just.manager.proxyManager.ProxyManager;
import dev.just.mixin.iface.ScreenAccessor;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.awt.Color;

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
      if (pm != null) {
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

   @Inject(method = "render", at = @At("TAIL"))
   public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      MultiplayerScreen screen = (MultiplayerScreen)(Object)this;

      RenderFonts titleFont = FontUtils.sf_bold[20];

      int primaryColor = new Color(255, 140, 50).getRGB();
      int secondaryColor = new Color(255, 90, 30).getRGB();

      String title = "JUST CLIENT";

      float centerX = screen.width / 2.0f;
      float titleWidth = titleFont.getWidth(title);

      // Just Client - üstte ortada
      titleFont.renderGradientText(context.getMatrices(), title, centerX - titleWidth / 2, 4, primaryColor, secondaryColor);
   }
}
