package dev.just.manager.themeManager;

import dev.just.manager.IMinecraft;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.math.MathHelper;
import dev.just.protect.runtime.I0O1l0I1;

public class StyleManager implements IMinecraft {
   private final List<Style> styles = new CopyOnWriteArrayList<>();
   private Style currentStyle;
   private final Path file = Paths.get(Objects.requireNonNull(mc.runDirectory).getAbsolutePath(), "files", "themes.ew");

   public void init() {
      this.addStyle(I0O1l0I1.b("VmFyc2F5xLFsYW4="), "#FF6B00", "#FF9500");
      this.addStyle("Turuncu", "#FF7D00", "#FFD700");
      this.addStyle(I0O1l0I1.b("QXRlxZ8="), "#FF4500", "#FF8C00");
      this.addStyle(I0O1l0I1.b("R8O8bmXFnw=="), "#FFA500", "#FFD700");
      this.addStyle("Amber", "#FF8000", "#FFBF00");
      this.loadCustomThemes();
      if (!this.styles.isEmpty()) {
         this.currentStyle = this.styles.get(0);
      }
   }

   private void addStyle(String name, String... hexColors) {
      int[] colors = new int[hexColors.length];

      for (int i = 0; i < hexColors.length; i++) {
         colors[i] = StyleManager.HexColor.toColor(hexColors[i]);
      }

      this.styles.add(new Style(name, colors));
   }

   public void addCustomTheme(String name, int color1, int color2) {
      Style style = new Style(name, color1, color2);
      this.styles.add(style);
      this.saveCustomThemes();
   }

   public void removeStyle(Style style) {
      if (style != null) {
         if (this.styles.contains(style)) {
            if (style.name.toLowerCase().startsWith("custom")) {
               this.styles.remove(style);
               this.saveCustomThemes();
               if (this.currentStyle == style) {
                  this.currentStyle = !this.styles.isEmpty() ? this.styles.get(0) : null;
               }
            }
         }
      }
   }

   public void setTheme(Style style) {
      if (this.styles.contains(style)) {
         this.currentStyle = style;
      }
   }

   public Style getTheme() {
      return this.currentStyle;
   }

   public List<Style> getStyles() {
      return this.styles;
   }

   public int getFirstColor() {
      return this.currentStyle != null && this.currentStyle.colors.length > 0 ? this.currentStyle.colors[0] : -1;
   }

   public int getSecondColor() {
      return this.currentStyle != null && this.currentStyle.colors.length > 1 ? this.currentStyle.colors[1] : this.getFirstColor();
   }

   private void saveCustomThemes() {
      try {
         Files.createDirectories(this.file.getParent());
         List<String> lines = new ArrayList<>();

         for (Style style : this.styles) {
            if (style.name.toLowerCase().startsWith("custom")) {
               StringBuilder sb = new StringBuilder(style.name);

               for (int color : style.colors) {
                  sb.append(":").append(colorToHex(color));
               }

               lines.add(sb.toString());
            }
         }

         Files.write(this.file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (IOException var9) {
         System.err.println(I0O1l0I1.b("VGVtYWxhciBrYXlkZWRpbGVtZWRpOiA=") + var9.getMessage());
      }
   }

   private void loadCustomThemes() {
      try {
         Files.createDirectories(this.file.getParent());
         if (Files.notExists(this.file)) {
            Files.createFile(this.file);
            return;
         }

         Files.lines(this.file, StandardCharsets.UTF_8).map(String::trim).filter(s -> !s.isEmpty()).forEach(line -> {
            try {
               String[] parts = line.split(":");
               if (parts.length >= 3) {
                  String name = parts[0];
                  String hex1 = parts[1];
                  String hex2 = parts[2];
                  this.addStyle(name, hex1, hex2);
               }
            } catch (Exception var6) {
               System.err.println(I0O1l0I1.b("VGVtYSBzYXTEsXLEsSDDp8O2esO8bWxlbmVtZWRpOiA=") + line);
            }
         });
      } catch (IOException var2) {
         System.err.println(I0O1l0I1.b("VGVtYWxhciB5w7xrbGVuZW1lZGk6IA==") + var2.getMessage());
      }
   }

   private static String colorToHex(int color) {
      int rgb = color & 16777215;
      return "#" + String.format("%06X", rgb);
   }

   public static class HexColor {
      public static int toColor(String hexColor) {
         int rgb = Integer.parseInt(hexColor.substring(1), 16);
         return reAlphaInt(rgb, 255);
      }

      public static int reAlphaInt(int color, int alpha) {
         return MathHelper.clamp(alpha, 0, 255) << 24 | color & 16777215;
      }
   }
}
