package dev.just.screens.mainmenu;

import dev.just.manager.fontManager.FontUtils;
import dev.just.manager.fontManager.RenderFonts;
import dev.just.screens.altmanager.AltManager;
import dev.just.util.color.ColorUtil;
import dev.just.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.MinecraftClient;
import org.joml.Vector4f;

public class MainMenu extends Screen {
   private float[] buttonHover = new float[6];

   public MainMenu() {
      super(Text.literal("Just Client"));
   }

   protected void init() {
      for (int i = 0; i < buttonHover.length; i++) {
         buttonHover[i] = 0;
      }
   }

   public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
      // Koyu arka plan
      RenderUtil.drawRoundedRect(ctx.getMatrices(), 0, 0, this.width, this.height, 0, new Color(18, 18, 24).getRGB());

      RenderFonts titleFont = FontUtils.sf_bold[36];
      RenderFonts subtitleFont = FontUtils.sf_medium[14];
      RenderFonts buttonFont = FontUtils.sf_medium[16];

      // Renkler
      int primaryColor = new Color(255, 140, 50).getRGB();
      int secondaryColor = new Color(255, 90, 30).getRGB();

      // Baslik
      String title = "JUST CLIENT";
      float titleWidth = titleFont.getWidth(title);
      float centerX = this.width / 2.0f;
      float titleY = this.height * 0.2f;

      // Gradient title
      titleFont.renderGradientText(ctx.getMatrices(), title, centerX - titleWidth / 2, titleY, primaryColor, secondaryColor);

      // Alt yazi
      String subtitle = "Minecraft 1.21.4";
      float subtitleWidth = subtitleFont.getWidth(subtitle);
      subtitleFont.drawLeftAligned(ctx.getMatrices(), subtitle, centerX - subtitleWidth / 2, titleY + 40, new Color(100, 100, 110).getRGB());

      // Butonlar
      int buttonWidth = 200;
      int buttonHeight = 36;
      int buttonSpacing = 10;
      float buttonsY = titleY + 80;
      int buttonX = (this.width - buttonWidth) / 2;

      String[] labels = {"Tek Oyunculu", "Cok Oyunculu", "Hesap Yoneticisi", "Ayarlar", "Cikis Yap", "Oyunu Kapat"};

      for (int i = 0; i < 6; i++) {
         int by = (int)(buttonsY + i * (buttonHeight + buttonSpacing));
         boolean hovered = mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= by && mouseY <= by + buttonHeight;

         // Hover animasyonu
         float targetHover = hovered ? 1.0f : 0.0f;
         buttonHover[i] += (targetHover - buttonHover[i]) * 0.12f;

         // Arka plan rengi
         int bgBase = new Color(28, 28, 36).getRGB();
         int bgHover = new Color(40, 40, 52).getRGB();
         int bgColor = ColorUtil.blendColorsInt(bgBase, bgHover, buttonHover[i]);

         // Buton arka plani
         RenderUtil.drawRoundedRect(ctx.getMatrices(), buttonX, by, buttonWidth, buttonHeight, 6.0f, bgColor);

         // Sol renkli cizgi
         int lineColor;
         if (i >= 4) {
            lineColor = new Color(220, 80, 80).getRGB(); // Kirmizi - cikis butonlari
         } else {
            lineColor = ColorUtil.interpolateColor(primaryColor, secondaryColor, (float)i / 3.0f);
         }
         RenderUtil.drawRoundedRect(ctx.getMatrices(), buttonX, by, 3, buttonHeight, new Vector4f(6, 0, 0, 6), lineColor);

         // Buton metni
         int textColor = hovered ? Color.WHITE.getRGB() : new Color(200, 200, 210).getRGB();
         float textY = by + (buttonHeight - buttonFont.getHeight()) / 2.0f + 1;
         buttonFont.drawLeftAligned(ctx.getMatrices(), labels[i], buttonX + 14, textY, textColor);

         // Ok isareti (hover)
         if (buttonHover[i] > 0.05f) {
            int alpha = (int)(buttonHover[i] * 200);
            buttonFont.drawLeftAligned(ctx.getMatrices(), ">", buttonX + buttonWidth - 20, textY, new Color(255, 255, 255, alpha).getRGB());
         }
      }

      // Alt bilgi
      RenderFonts smallFont = FontUtils.durman[11];
      smallFont.drawLeftAligned(ctx.getMatrices(), "Just Client", 6, this.height - 14, new Color(100, 100, 110).getRGB());
      smallFont.drawLeftAligned(ctx.getMatrices(), " by Lxrich", 6 + smallFont.getWidth("Just Client"), this.height - 14, new Color(70, 70, 80).getRGB());
      String ver = "v1.21.4";
      smallFont.drawLeftAligned(ctx.getMatrices(), ver, this.width - smallFont.getWidth(ver) - 6, this.height - 14, new Color(70, 70, 80).getRGB());
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int buttonWidth = 200;
      int buttonHeight = 36;
      int buttonSpacing = 10;
      float buttonsY = this.height * 0.2f + 80;
      int buttonX = (this.width - buttonWidth) / 2;

      for (int i = 0; i < 6; i++) {
         int by = (int)(buttonsY + i * (buttonHeight + buttonSpacing));
         if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= by && mouseY <= by + buttonHeight) {
            switch (i) {
               case 0:
                  this.client.setScreen(new SelectWorldScreen(this));
                  break;
               case 1:
                  this.client.setScreen(new MultiplayerScreen(this));
                  break;
               case 2:
                  this.client.setScreen(new AltManager(this));
                  break;
               case 3:
                  this.client.setScreen(new OptionsScreen(this, this.client.options));
                  break;
               case 4:
                  // Cikis Yap - Hesaptan cikis
                  this.client.disconnect();
                  this.client.setScreen(new TitleScreen());
                  break;
               case 5:
                  // Oyunu Kapat
                  this.client.scheduleStop();
                  break;
            }
            return true;
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }
}
