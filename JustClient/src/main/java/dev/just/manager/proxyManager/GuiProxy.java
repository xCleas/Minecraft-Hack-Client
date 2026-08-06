package dev.just.manager.proxyManager;

import dev.just.manager.Manager;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.apache.commons.lang3.StringUtils;
import dev.just.protect.runtime.I0O1l0I1;

public class GuiProxy extends Screen {
   private boolean isSocks4;
   private TextFieldWidget ipPort;
   private TextFieldWidget username;
   private TextFieldWidget password;
   private CheckboxWidget enabledCheck;
   private final Screen parentScreen;
   private String msg = "";
   private int[] positionY;
   private int positionX;

   public GuiProxy(Screen parentScreen) {
      super(Text.literal(I0O1l0I1.b("UHJveHkgQXlhcmxhcsSx")));
      this.parentScreen = parentScreen;
   }

   private static boolean isValidIpPort(String ipP) {
      String[] split = ipP.split(":");
      if (split.length == 2 && StringUtils.isNumeric(split[1])) {
         int port = Integer.parseInt(split[1]);
         return port >= 0 && port <= 65535;
      } else {
         return false;
      }
   }

   private boolean checkProxy() {
      if (!isValidIpPort(this.ipPort.getText())) {
         this.msg = Formatting.RED + I0O1l0I1.b("R2XDp2Vyc2l6IFBvcnQh");
         this.ipPort.setFocused(true);
         return false;
      } else {
         return true;
      }
   }

   private void centerButtons(int amount, int buttonLength, int gap) {
      this.positionX = (this.width - buttonLength) / 2;
      this.positionY = new int[amount];
      int startY = (this.height - (amount - 1) * gap) / 2;

      for (int i = 0; i < amount; i++) {
         this.positionY[i] = startY + gap * i;
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      this.msg = "";
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
      super.render(context, mouseX, mouseY, partialTicks);
      if (this.enabledCheck.isChecked() && !isValidIpPort(this.ipPort.getText())) {
         this.enabledCheck.onPress();
      }

      context.drawTextWithShadow(this.textRenderer, I0O1l0I1.b("UHJveHkgVGlwaQ=="), this.positionX - 65, this.positionY[1] + 5, 10518656);
      context.drawCenteredTextWithShadow(this.textRenderer, I0O1l0I1.b("S2ltbGlrIERvxJ9ydWxhbWE="), this.width / 2, this.positionY[3] + 8, Formatting.WHITE.getColorValue());
      context.drawTextWithShadow(this.textRenderer, "IP:PORT", this.positionX - 55, this.positionY[2] + 5, 10518656);
      if (!this.msg.isEmpty()) {
         context.drawCenteredTextWithShadow(this.textRenderer, this.msg, this.width / 2, this.positionY[0], 16777215);
      }

      this.ipPort.render(context, mouseX, mouseY, partialTicks);
      this.username.render(context, mouseX, mouseY, partialTicks);
      if (!this.isSocks4) {
         this.password.render(context, mouseX, mouseY, partialTicks);
      }
   }

   public void init() {
      ProxyManager pm = Manager.PROXY_MANAGER;
      this.centerButtons(10, 160, 26);
      this.isSocks4 = ProxyManager.proxy.type == Proxy.ProxyType.SOCKS4;
      this.addDrawableChild(ButtonWidget.builder(Text.literal(this.isSocks4 ? "Socks 4" : "Socks 5"), b -> {
         this.isSocks4 = !this.isSocks4;
         b.setMessage(Text.literal(this.isSocks4 ? "Socks 4" : "Socks 5"));
      }).dimensions(this.positionX, this.positionY[1], 160, 20).build());
      this.ipPort = new TextFieldWidget(this.textRenderer, this.positionX, this.positionY[2], 160, 20, Text.literal(""));
      this.ipPort.setPlaceholder(Text.literal("127.0.0.1:1080"));
      this.ipPort.setText(ProxyManager.proxy.ipPort);
      this.ipPort.setMaxLength(1024);
      this.ipPort.setFocused(true);
      this.addSelectableChild(this.ipPort);
      this.username = new TextFieldWidget(this.textRenderer, this.positionX, this.positionY[4], 160, 20, Text.literal(""));
      this.username.setPlaceholder(Text.literal(I0O1l0I1.b("S3VsbGFuxLFjxLEgQWTEsQ==")));
      this.username.setText(ProxyManager.proxy.username);
      this.username.setMaxLength(255);
      this.addSelectableChild(this.username);
      this.password = new TextFieldWidget(this.textRenderer, this.positionX, this.positionY[5], 160, 20, Text.literal(""));
      this.password.setPlaceholder(Text.literal(I0O1l0I1.b("xZ5pZnJl")));
      this.password.setText(ProxyManager.proxy.password);
      this.password.setMaxLength(255);
      this.addSelectableChild(this.password);
      int posXButtons = (this.width - 160) / 2;
      this.addDrawableChild(ButtonWidget.builder(Text.literal("Tamam"), b -> {
         if (this.checkProxy()) {
            ProxyManager.proxy = new Proxy(this.isSocks4, this.ipPort.getText(), this.username.getText(), this.password.getText());
            ProxyManager.proxyEnabled = this.enabledCheck.isChecked();
            pm.setDefaultProxy(ProxyManager.proxy);
            pm.saveConfig();
            this.client.setScreen(new MultiplayerScreen(new TitleScreen()));
         }
      }).dimensions(posXButtons, this.positionY[8], 77, 20).build());
      this.enabledCheck = CheckboxWidget.builder(Text.literal("Aktif"), this.textRenderer)
         .pos((this.width - 15 - this.textRenderer.getWidth(Text.literal("Aktif"))) / 2, this.positionY[7])
         .checked(ProxyManager.proxyEnabled)
         .build();
      this.addDrawableChild(this.enabledCheck);
      this.addDrawableChild(
         ButtonWidget.builder(Text.literal("Geri"), b -> this.client.setScreen(this.parentScreen))
            .dimensions(posXButtons + 80 + 3, this.positionY[8], 77, 20)
            .build()
      );
   }

   public void close() {
      super.close();
      this.msg = "";
   }
}
