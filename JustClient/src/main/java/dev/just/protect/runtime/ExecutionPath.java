package dev.just.protect.runtime;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * EXECUTION PATH RANDOMIZATION
 *
 * Her calistirmada farkli kod yolu:
 * - Ayni sonuc
 * - Farkli execution order
 * - Farkli decrypt sequence
 *
 * 🎯 Etki:
 * - Tek bir crack genellenemez
 * - Tutorial / write-up ise yaramaz
 */
public final class ExecutionPath {

    private static final SecureRandom random = new SecureRandom();

    // Execution path seed (her session'da farkli)
    private static final long PATH_SEED = generatePathSeed();

    private ExecutionPath() {}

    /**
     * Benzersiz path seed olustur
     */
    private static long generatePathSeed() {
        return System.nanoTime() ^ Runtime.getRuntime().freeMemory() ^
               Thread.currentThread().getId() ^ random.nextLong();
    }

    /**
     * Rastgele sirada calistir (ayni sonuc)
     */
    @SafeVarargs
    public static <T> T randomOrder(Supplier<T>... operations) {
        if (operations.length == 0) return null;

        // Shuffle order
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < operations.length; i++) {
            order.add(i);
        }

        // PATH_SEED'e gore shuffle (deterministic per session)
        Collections.shuffle(order, new java.util.Random(PATH_SEED ^ operations.length));

        // Son operation'in sonucunu don
        T result = null;
        for (int idx : order) {
            result = operations[idx].get();
        }
        return result;
    }

    /**
     * Rastgele sirada void operasyonlar calistir
     */
    public static void randomOrderVoid(Runnable... operations) {
        if (operations.length == 0) return;

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < operations.length; i++) {
            order.add(i);
        }

        Collections.shuffle(order, new java.util.Random(PATH_SEED ^ operations.length));

        for (int idx : order) {
            operations[idx].run();
        }
    }

    /**
     * Farkli decrypt sequence
     */
    public static String decryptRandom(String[] parts) {
        if (parts.length == 0) return "";

        // Parcalari rastgele sirada coz
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            order.add(i);
        }

        Collections.shuffle(order, new java.util.Random(PATH_SEED ^ parts.length));

        // Decode
        String[] decoded = new String[parts.length];
        for (int idx : order) {
            decoded[idx] = StringGuard.g(parts[idx]);
        }

        // Birlestir
        StringBuilder sb = new StringBuilder();
        for (String s : decoded) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Conditional execution (path'e gore)
     */
    public static <T> T pathConditional(Supplier<T> path1, Supplier<T> path2) {
        // PATH_SEED'e gore path sec
        if ((PATH_SEED & 1) == 0) {
            return path1.get();
        } else {
            return path2.get();
        }
    }

    /**
     * Multi-path hesaplama (hepsi ayni sonuc, farkli yol)
     */
    public static int multiPath(int value) {
        int path = (int) (PATH_SEED % 4);

        switch (path) {
            case 0:
                // Path A: XOR chain
                return ((value ^ 0x5A) ^ 0x5A);

            case 1:
                // Path B: Add-subtract
                return ((value + 100) - 100);

            case 2:
                // Path C: Multiply-divide
                int temp = value * 7;
                return temp / 7;

            case 3:
                // Path D: Bit operations
                return Integer.rotateLeft(Integer.rotateRight(value, 13), 13);

            default:
                return value;
        }
    }

    /**
     * Delay injection (timing analysis zorlaştırma)
     */
    public static void randomDelay() {
        int delayPath = (int) ((PATH_SEED ^ System.nanoTime()) % 5);

        try {
            switch (delayPath) {
                case 0 -> {} // No delay
                case 1 -> Thread.sleep(1);
                case 2 -> Thread.yield();
                case 3 -> { for (int i = 0; i < 1000; i++) Math.random(); }
                case 4 -> System.nanoTime();
            }
        } catch (InterruptedException ignored) {}
    }

    /**
     * Opaque predicate (her zaman true, farkli yoldan)
     */
    public static boolean opaqueTrue() {
        int path = (int) (PATH_SEED % 6);

        return switch (path) {
            case 0 -> (PATH_SEED | 1) != 0;
            case 1 -> (PATH_SEED & Long.MAX_VALUE) >= 0;
            case 2 -> Math.abs(PATH_SEED) >= 0 || PATH_SEED == Long.MIN_VALUE;
            case 3 -> String.valueOf(PATH_SEED).length() > 0;
            case 4 -> (PATH_SEED ^ PATH_SEED) == 0;
            case 5 -> (PATH_SEED + 1) != PATH_SEED;
            default -> true;
        };
    }

    /**
     * Opaque predicate (her zaman false, farkli yoldan)
     */
    public static boolean opaqueFalse() {
        int path = (int) (PATH_SEED % 4);

        return switch (path) {
            case 0 -> (PATH_SEED & 0) != 0;
            case 1 -> PATH_SEED != PATH_SEED;
            case 2 -> (PATH_SEED ^ PATH_SEED) != 0;
            case 3 -> String.valueOf(PATH_SEED).isEmpty();
            default -> false;
        };
    }

    public static long getPathSeed() {
        return PATH_SEED;
    }
}
