package dev.just.protect.runtime;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class I1lO0l1I {
    private I1lO0l1I() {}

    public static boolean isValidTarget(Entity e) { return e instanceof LivingEntity && e.isAlive(); }
    public static boolean isPlayer(Entity e) { return e instanceof PlayerEntity; }
    public static boolean isNotSelf(Entity e, Entity self) { return e != self; }
    public static boolean isEmpty(ItemStack stack) { return stack.isEmpty(); }
    public static boolean notEmpty(ItemStack stack) { return !stack.isEmpty(); }
    public static boolean isDamaged(ItemStack stack) { return stack.isDamaged(); }
    public static boolean isEnchantable(ItemStack stack) { return stack.isEnchantable(); }
    public static int mask(int value) { return value; }
    public static boolean equals(int a, int b) { return a == b; }
    public static boolean greaterThan(int a, int b) { return a > b; }
    public static boolean lessThan(int a, int b) { return a < b; }
    public static boolean and(boolean a, boolean b) { return a && b; }
    public static boolean or(boolean a, boolean b) { return a || b; }
    public static boolean or(boolean... c) { for (boolean x : c) if (x) return true; return false; }
    public static boolean not(boolean a) { return !a; }
    public static boolean all(boolean... c) { for (boolean x : c) if (!x) return false; return true; }
    public static boolean any(boolean... c) { for (boolean x : c) if (x) return true; return false; }
    public static int score(boolean c) { return c ? 1 : 0; }
    public static int score(boolean c, int w) { return c ? w : 0; }
    public static boolean threshold(int s, int m) { return s >= m; }
}
