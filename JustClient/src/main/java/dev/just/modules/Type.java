package dev.just.modules;

import dev.just.protect.runtime.I0O1l0I1;

public enum Type {
   Combat("f", I0O1l0I1.b("U2F2YcWf")),
   Move("w", "Hareket"),
   Render("E", I0O1l0I1.b("R8O2cnNlbA==")),
   Player("r", "Oyuncu"),
   Misc("v", I0O1l0I1.b("RGnEn2Vy"));

   public final String icon;
   public final String displayName;

   private Type(String icon, String displayName) {
      this.icon = icon;
      this.displayName = displayName;
   }
}
