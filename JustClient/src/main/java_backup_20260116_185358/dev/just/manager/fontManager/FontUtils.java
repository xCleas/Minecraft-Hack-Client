package dev.just.manager.fontManager;

import java.awt.Font;
import java.util.Objects;

public class FontUtils {
   public final String fontsDir = "/assets/justclient/font/";
   public volatile RenderFonts[] comfortaa = new RenderFonts[256];
   public static volatile RenderFonts[] durman = new RenderFonts[256];
   public static volatile RenderFonts[] glitched = new RenderFonts[256];
   public static volatile RenderFonts[] icons = new RenderFonts[256];
   public static volatile RenderFonts[] monsterrat = new RenderFonts[256];
   public static volatile RenderFonts[] profont = new RenderFonts[256];
   public static volatile RenderFonts[] sf_bold = new RenderFonts[256];
   public static volatile RenderFonts[] sf_medium = new RenderFonts[256];
   public static volatile RenderFonts[] iconsWex = new RenderFonts[256];
   public static volatile RenderFonts[] gilroy = new RenderFonts[256];
   public static volatile RenderFonts[] gilroy_bold = new RenderFonts[256];
   public static volatile RenderFonts[] hud = new RenderFonts[256];
   public static volatile RenderFonts[] icomoon = new RenderFonts[256];
   private boolean initialized = false;

   public synchronized void init() {
      if (!this.initialized) {
         this.initializationFont(this.comfortaa, "comfortaa.ttf");
         this.initializationFont(durman, "durman.ttf");
         this.initializationFont(glitched, "glitched.ttf");
         this.initializationFont(icons, "icons.ttf");
         this.initializationFont(monsterrat, "monsterrat.ttf");
         this.initializationFont(profont, "profont.ttf");
         this.initializationFont(sf_bold, "sf_bold.ttf");
         this.initializationFont(sf_medium, "sf_medium.ttf");
         this.initializationFont(iconsWex, "iconsWex.ttf");
         this.initializationFont(hud, "hud.ttf");
         this.initializationFont(gilroy, "gilroy.ttf");
         this.initializationFont(gilroy_bold, "gilroy-bold.ttf");
         this.initializationFont(icomoon, "icomoon.ttf");
         this.initialized = true;
      }
   }

   private void initializationFont(RenderFonts[] fontArray, String fontName) {
      if (fontArray != null) {
         try {
            Font font = Font.createFont(0, Objects.requireNonNull(FontUtils.class.getResourceAsStream("/assets/justclient/font/" + fontName)));

            for (int i = 1; i < fontArray.length; i++) {
               fontArray[i] = new RenderFonts(font, (float)i);
            }
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }
   }
}
