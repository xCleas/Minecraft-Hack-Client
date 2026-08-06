package dev.just.modules;

import dev.just.protect.runtime.Strings;

public enum Type {
   Combat("f", Strings.b("U2F2YcWf")),
   Move("w", "Hareket"),
   Render("E", Strings.b("R8O2cnNlbA==")),
   Player("r", "Oyuncu"),
   Misc("v", Strings.b("RGnEn2Vy"));

   public final String icon;
   public final String displayName;

   private Type(String icon, String displayName) {
      this.icon = icon;
      this.displayName = displayName;
   }
}
