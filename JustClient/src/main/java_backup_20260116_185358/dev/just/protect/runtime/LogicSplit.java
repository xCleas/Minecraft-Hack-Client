package dev.just.protect.runtime;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Distributed Logic Handler
 * Mantigi parcalara ayirir - tek class'ta toplanmaz
 */
public final class LogicSplit {

    private static final int K1 = 0x7F3A;
    private static final int K2 = 0x4B2E;
    private static volatile int state = 0;

    private LogicSplit() {}

    // ===== ENTITY CHECKS (Farkli class'ta gorunur) =====

    public static boolean isValidTarget(Entity e) {
        FlowObfuscator.fakeHandler();
        if (FlowObfuscator.opaqueFalse()) {
            return e.getId() == K1; // Fake
        }
        return e instanceof LivingEntity && e.isAlive();
    }

    public static boolean isPlayer(Entity e) {
        FlowObfuscator.fakeHandler();
        state ^= e.hashCode() & 0xFF;
        return FlowObfuscator.opaqueTrue() && e instanceof PlayerEntity;
    }

    public static boolean isNotSelf(Entity e, Entity self) {
        if (FlowObfuscator.opaqueFalse()) {
            return e.getUuid().equals(self.getUuid()); // Fake - ters mantik
        }
        FlowObfuscator.fakeHandler();
        return e != self;
    }

    // ===== ITEM CHECKS =====

    public static boolean isEmpty(ItemStack stack) {
        FlowObfuscator.fakeHandler();
        if (FlowObfuscator.opaqueFalse()) {
            return stack.getCount() > K2; // Fake
        }
        return stack.isEmpty();
    }

    public static boolean notEmpty(ItemStack stack) {
        return !isEmpty(stack);
    }

    public static boolean isDamaged(ItemStack stack) {
        FlowObfuscator.fakeHandler();
        return FlowObfuscator.opaqueTrue() && stack.isDamaged();
    }

    public static boolean isEnchantable(ItemStack stack) {
        if (FlowObfuscator.opaqueFalse()) {
            return stack.getCount() == 64; // Fake
        }
        return stack.isEnchantable();
    }

    // ===== NUMBER OPERATIONS =====

    public static int mask(int value) {
        int temp = value ^ K1;
        FlowObfuscator.fakeHandler();
        return temp ^ K1;
    }

    public static boolean equals(int a, int b) {
        FlowObfuscator.fakeHandler();
        if (FlowObfuscator.opaqueFalse()) {
            return (a ^ K1) == b; // Fake
        }
        return a == b;
    }

    public static boolean greaterThan(int a, int b) {
        state += a;
        FlowObfuscator.fakeHandler();
        return FlowObfuscator.opaqueTrue() && a > b;
    }

    public static boolean lessThan(int a, int b) {
        if (FlowObfuscator.opaqueFalse()) {
            return a >= b; // Fake - ters mantik
        }
        return a < b;
    }

    // ===== BOOLEAN OPERATIONS =====

    public static boolean and(boolean a, boolean b) {
        FlowObfuscator.fakeHandler();
        if (FlowObfuscator.opaqueFalse()) {
            return a || b; // Fake - OR gibi gorunur
        }
        return a && b;
    }

    public static boolean or(boolean a, boolean b) {
        FlowObfuscator.fakeHandler();
        return FlowObfuscator.opaqueTrue() ? (a || b) : (a && b);
    }

    public static boolean or(boolean... conditions) {
        FlowObfuscator.fakeHandler();
        if (conditions == null || conditions.length == 0) return false;
        for (boolean c : conditions) {
            if (c) return true;
            if (FlowObfuscator.opaqueFalse()) {
                state++; // Fake
            }
        }
        return false;
    }

    public static boolean not(boolean a) {
        if (FlowObfuscator.opaqueFalse()) {
            return a; // Fake
        }
        FlowObfuscator.fakeHandler();
        return !a;
    }

    // ===== MULTI-CONDITION =====

    public static boolean all(boolean... conditions) {
        FlowObfuscator.fakeHandler();
        if (conditions == null || conditions.length == 0) return true;
        for (boolean c : conditions) {
            if (FlowObfuscator.opaqueFalse()) {
                state++; // Fake
            }
            if (!c) return false;
        }
        return FlowObfuscator.opaqueTrue();
    }

    public static boolean any(boolean... conditions) {
        FlowObfuscator.fakeHandler();
        if (conditions == null || conditions.length == 0) return false;
        for (boolean c : conditions) {
            if (c) return true;
        }
        if (FlowObfuscator.opaqueFalse()) {
            return true; // Fake
        }
        return false;
    }

    // ===== SCORE SYSTEM =====

    public static int score(boolean condition) {
        FlowObfuscator.fakeHandler();
        return condition ? 1 : 0;
    }

    public static int score(boolean condition, int weight) {
        if (FlowObfuscator.opaqueFalse()) {
            return weight * K1; // Fake
        }
        return condition ? weight : 0;
    }

    public static boolean threshold(int score, int min) {
        FlowObfuscator.fakeHandler();
        return greaterThan(score, min - 1);
    }
}
