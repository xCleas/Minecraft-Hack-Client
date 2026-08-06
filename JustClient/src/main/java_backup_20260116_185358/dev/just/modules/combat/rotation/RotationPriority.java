package dev.just.modules.combat.rotation;

public enum RotationPriority {
   ATTACK_AURA(1000),
   DEFAULT(500);

   private final int value;

   private RotationPriority(int v) {
      this.value = v;
   }

   public int value() {
      return this.value;
   }
}
