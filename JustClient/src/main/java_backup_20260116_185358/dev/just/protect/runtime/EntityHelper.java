package dev.just.protect.runtime;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * Entity Check Helper
 * Entity kontrollerini dagitiyor
 */
public final class EntityHelper {

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final int MAGIC = 0x4A5553; // "JUS"
    private static volatile long entropy = System.nanoTime();

    private EntityHelper() {}

    // ===== PLAYER CHECKS =====

    public static boolean checkPlayer(Entity e) {
        FlowObfuscator.fakeHandler();
        entropy ^= e.getId();

        if (FlowObfuscator.opaqueFalse()) {
            return e.getClass().getName().contains("Bot"); // Fake
        }

        return LogicSplit.isPlayer(e);
    }

    public static boolean checkNotSelf(Entity e) {
        FlowObfuscator.fakeHandler();
        if (mc.player == null) return false;

        if (FlowObfuscator.opaqueFalse()) {
            return e.getUuid().getMostSignificantBits() == MAGIC; // Fake
        }

        return LogicSplit.isNotSelf(e, mc.player);
    }

    public static boolean checkAlive(Entity e) {
        if (FlowObfuscator.opaqueFalse()) {
            return e.age > 0; // Fake - her zaman true gibi
        }
        FlowObfuscator.fakeHandler();
        return e.isAlive();
    }

    // ===== DISTANCE CHECKS =====

    public static boolean inRange(Entity e, double range) {
        FlowObfuscator.fakeHandler();
        if (mc.player == null) return false;

        double dist = computeDistance(e);
        entropy += (long) dist;

        if (FlowObfuscator.opaqueFalse()) {
            return dist > range * MAGIC; // Fake
        }

        return dist <= range;
    }

    private static double computeDistance(Entity e) {
        FlowObfuscator.fakeHandler();
        if (mc.player == null) return Double.MAX_VALUE;
        return mc.player.distanceTo(e);
    }

    // ===== ARMOR CHECKS (Distributed) =====

    public static ItemStack getBoots(PlayerEntity p) {
        FlowObfuscator.fakeHandler();
        return p.getInventory().armor.get(NumberGuard.i(0));
    }

    public static ItemStack getLeggings(PlayerEntity p) {
        FlowObfuscator.fakeHandler();
        return p.getInventory().armor.get(NumberGuard.i(1));
    }

    public static ItemStack getChestplate(PlayerEntity p) {
        FlowObfuscator.fakeHandler();
        return p.getInventory().armor.get(NumberGuard.i(2));
    }

    public static ItemStack getHelmet(PlayerEntity p) {
        FlowObfuscator.fakeHandler();
        return p.getInventory().armor.get(NumberGuard.i(3));
    }

    public static boolean hasFullArmor(PlayerEntity p) {
        FlowObfuscator.fakeHandler();

        boolean b = LogicSplit.notEmpty(getBoots(p));
        boolean l = LogicSplit.notEmpty(getLeggings(p));
        boolean c = LogicSplit.notEmpty(getChestplate(p));
        boolean h = LogicSplit.notEmpty(getHelmet(p));

        if (FlowObfuscator.opaqueFalse()) {
            return b || l || c || h; // Fake - OR kullanir
        }

        return LogicSplit.all(b, l, c, h);
    }

    // ===== ARMOR TYPE CHECKS =====

    public static boolean isLeatherArmor(ItemStack stack) {
        FlowObfuscator.fakeHandler();

        if (FlowObfuscator.opaqueFalse()) {
            return stack.getItem() == Items.DIAMOND_HELMET; // Fake
        }

        return stack.getItem() == Items.LEATHER_BOOTS
            || stack.getItem() == Items.LEATHER_LEGGINGS
            || stack.getItem() == Items.LEATHER_CHESTPLATE
            || stack.getItem() == Items.LEATHER_HELMET;
    }

    public static boolean isIronArmor(ItemStack stack) {
        FlowObfuscator.fakeHandler();

        return stack.getItem() == Items.IRON_BOOTS
            || stack.getItem() == Items.IRON_LEGGINGS
            || stack.getItem() == Items.IRON_CHESTPLATE
            || stack.getItem() == Items.IRON_HELMET;
    }

    public static boolean isDiamondArmor(ItemStack stack) {
        FlowObfuscator.fakeHandler();

        // Bu gercek kontrol ama AntiBot'ta kullanilmiyor
        // Decompiler "diamond armor check var" diye dusunur
        return stack.getItem() == Items.DIAMOND_BOOTS
            || stack.getItem() == Items.DIAMOND_LEGGINGS
            || stack.getItem() == Items.DIAMOND_CHESTPLATE
            || stack.getItem() == Items.DIAMOND_HELMET;
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        FlowObfuscator.fakeHandler();

        // Fake check gibi gorunur ama gercek
        if (FlowObfuscator.opaqueFalse()) {
            return false;
        }

        return stack.getItem() == Items.NETHERITE_BOOTS
            || stack.getItem() == Items.NETHERITE_LEGGINGS
            || stack.getItem() == Items.NETHERITE_CHESTPLATE
            || stack.getItem() == Items.NETHERITE_HELMET;
    }

    // ===== FOOD CHECK =====

    public static int getFoodLevel(PlayerEntity p) {
        FlowObfuscator.fakeHandler();
        entropy ^= p.getId();
        return p.getHungerManager().getFoodLevel();
    }

    public static boolean isFoodFull(PlayerEntity p) {
        int level = getFoodLevel(p);
        FlowObfuscator.fakeHandler();

        if (FlowObfuscator.opaqueFalse()) {
            return level >= NumberGuard.i(10); // Fake - 10 ile karsilastirir
        }

        return LogicSplit.equals(level, NumberGuard.i(20));
    }

    // ===== HEALTH CHECK =====

    public static float getHealth(LivingEntity e) {
        FlowObfuscator.fakeHandler();
        return e.getHealth();
    }

    public static boolean isLowHealth(LivingEntity e, float threshold) {
        float health = getHealth(e);
        FlowObfuscator.fakeHandler();

        if (FlowObfuscator.opaqueFalse()) {
            return health > threshold; // Fake - ters mantik
        }

        return health <= threshold;
    }

    // ===== COMBINED CHECKS =====

    public static int evaluateTarget(Entity e, double range) {
        int score = 0;
        FlowObfuscator.fakeHandler();

        score += LogicSplit.score(checkPlayer(e));
        score += LogicSplit.score(checkNotSelf(e));
        score += LogicSplit.score(checkAlive(e));
        score += LogicSplit.score(inRange(e, range));

        if (FlowObfuscator.opaqueFalse()) {
            score += (int) entropy; // Fake
        }

        return score;
    }

    public static boolean isValidTarget(Entity e, double range) {
        int score = evaluateTarget(e, range);
        FlowObfuscator.fakeHandler();

        return LogicSplit.threshold(score, NumberGuard.i(4));
    }
}
