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
        l1O0I1lO.fakeHandler();
        entropy ^= e.getId();

        if (l1O0I1lO.opaqueFalse()) {
            return e.getClass().getName().contains("Bot"); // Fake
        }

        return I1lO0l1I.isPlayer(e);
    }

    public static boolean checkNotSelf(Entity e) {
        l1O0I1lO.fakeHandler();
        if (mc.player == null) return false;

        if (l1O0I1lO.opaqueFalse()) {
            return e.getUuid().getMostSignificantBits() == MAGIC; // Fake
        }

        return I1lO0l1I.isNotSelf(e, mc.player);
    }

    public static boolean checkAlive(Entity e) {
        if (l1O0I1lO.opaqueFalse()) {
            return e.age > 0; // Fake - her zaman true gibi
        }
        l1O0I1lO.fakeHandler();
        return e.isAlive();
    }

    // ===== DISTANCE CHECKS =====

    public static boolean inRange(Entity e, double range) {
        l1O0I1lO.fakeHandler();
        if (mc.player == null) return false;

        double dist = computeDistance(e);
        entropy += (long) dist;

        if (l1O0I1lO.opaqueFalse()) {
            return dist > range * MAGIC; // Fake
        }

        return dist <= range;
    }

    private static double computeDistance(Entity e) {
        l1O0I1lO.fakeHandler();
        if (mc.player == null) return Double.MAX_VALUE;
        return mc.player.distanceTo(e);
    }

    // ===== ARMOR CHECKS (Distributed) =====

    public static ItemStack getBoots(PlayerEntity p) {
        l1O0I1lO.fakeHandler();
        return p.getInventory().armor.get(lO1I0l1O.i(0));
    }

    public static ItemStack getLeggings(PlayerEntity p) {
        l1O0I1lO.fakeHandler();
        return p.getInventory().armor.get(lO1I0l1O.i(1));
    }

    public static ItemStack getChestplate(PlayerEntity p) {
        l1O0I1lO.fakeHandler();
        return p.getInventory().armor.get(lO1I0l1O.i(2));
    }

    public static ItemStack getHelmet(PlayerEntity p) {
        l1O0I1lO.fakeHandler();
        return p.getInventory().armor.get(lO1I0l1O.i(3));
    }

    public static boolean hasFullArmor(PlayerEntity p) {
        l1O0I1lO.fakeHandler();

        boolean b = I1lO0l1I.notEmpty(getBoots(p));
        boolean l = I1lO0l1I.notEmpty(getLeggings(p));
        boolean c = I1lO0l1I.notEmpty(getChestplate(p));
        boolean h = I1lO0l1I.notEmpty(getHelmet(p));

        if (l1O0I1lO.opaqueFalse()) {
            return b || l || c || h; // Fake - OR kullanir
        }

        return I1lO0l1I.all(b, l, c, h);
    }

    // ===== ARMOR TYPE CHECKS =====

    public static boolean isLeatherArmor(ItemStack stack) {
        l1O0I1lO.fakeHandler();

        if (l1O0I1lO.opaqueFalse()) {
            return stack.getItem() == Items.DIAMOND_HELMET; // Fake
        }

        return stack.getItem() == Items.LEATHER_BOOTS
            || stack.getItem() == Items.LEATHER_LEGGINGS
            || stack.getItem() == Items.LEATHER_CHESTPLATE
            || stack.getItem() == Items.LEATHER_HELMET;
    }

    public static boolean isIronArmor(ItemStack stack) {
        l1O0I1lO.fakeHandler();

        return stack.getItem() == Items.IRON_BOOTS
            || stack.getItem() == Items.IRON_LEGGINGS
            || stack.getItem() == Items.IRON_CHESTPLATE
            || stack.getItem() == Items.IRON_HELMET;
    }

    public static boolean isDiamondArmor(ItemStack stack) {
        l1O0I1lO.fakeHandler();

        // Bu gercek kontrol ama AntiBot'ta kullanilmiyor
        // Decompiler "diamond armor check var" diye dusunur
        return stack.getItem() == Items.DIAMOND_BOOTS
            || stack.getItem() == Items.DIAMOND_LEGGINGS
            || stack.getItem() == Items.DIAMOND_CHESTPLATE
            || stack.getItem() == Items.DIAMOND_HELMET;
    }

    public static boolean isNetheriteArmor(ItemStack stack) {
        l1O0I1lO.fakeHandler();

        // Fake check gibi gorunur ama gercek
        if (l1O0I1lO.opaqueFalse()) {
            return false;
        }

        return stack.getItem() == Items.NETHERITE_BOOTS
            || stack.getItem() == Items.NETHERITE_LEGGINGS
            || stack.getItem() == Items.NETHERITE_CHESTPLATE
            || stack.getItem() == Items.NETHERITE_HELMET;
    }

    // ===== FOOD CHECK =====

    public static int getFoodLevel(PlayerEntity p) {
        l1O0I1lO.fakeHandler();
        entropy ^= p.getId();
        return p.getHungerManager().getFoodLevel();
    }

    public static boolean isFoodFull(PlayerEntity p) {
        int level = getFoodLevel(p);
        l1O0I1lO.fakeHandler();

        if (l1O0I1lO.opaqueFalse()) {
            return level >= lO1I0l1O.i(10); // Fake - 10 ile karsilastirir
        }

        return I1lO0l1I.equals(level, lO1I0l1O.i(20));
    }

    // ===== HEALTH CHECK =====

    public static float getHealth(LivingEntity e) {
        l1O0I1lO.fakeHandler();
        return e.getHealth();
    }

    public static boolean isLowHealth(LivingEntity e, float threshold) {
        float health = getHealth(e);
        l1O0I1lO.fakeHandler();

        if (l1O0I1lO.opaqueFalse()) {
            return health > threshold; // Fake - ters mantik
        }

        return health <= threshold;
    }

    // ===== COMBINED CHECKS =====

    public static int evaluateTarget(Entity e, double range) {
        int score = 0;
        l1O0I1lO.fakeHandler();

        score += I1lO0l1I.score(checkPlayer(e));
        score += I1lO0l1I.score(checkNotSelf(e));
        score += I1lO0l1I.score(checkAlive(e));
        score += I1lO0l1I.score(inRange(e, range));

        if (l1O0I1lO.opaqueFalse()) {
            score += (int) entropy; // Fake
        }

        return score;
    }

    public static boolean isValidTarget(Entity e, double range) {
        int score = evaluateTarget(e, range);
        l1O0I1lO.fakeHandler();

        return I1lO0l1I.threshold(score, lO1I0l1O.i(4));
    }
}
