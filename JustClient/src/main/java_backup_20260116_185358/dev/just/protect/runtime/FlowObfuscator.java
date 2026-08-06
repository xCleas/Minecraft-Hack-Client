package dev.just.protect.runtime;

import java.util.Random;

/**
 * Control Flow Obfuscation
 * Kod akisini karistirir - Decompiler'i deli eder
 */
public final class FlowObfuscator {

    private static final Random RNG = new Random(System.nanoTime());
    private static volatile int STATE = 0;
    private static volatile long ENTROPY = System.currentTimeMillis();

    // Opaque predicates - her zaman ayni sonuc ama anlasilmasi zor
    private static final int MAGIC_A = 0x4A757374; // "Just" as int
    private static final int MAGIC_B = 0x436C6965; // "Clie" as int

    private FlowObfuscator() {}

    /**
     * Opaque predicate - HER ZAMAN TRUE doner ama analiz edilemez
     */
    public static boolean opaqueTrue() {
        int x = (int) (System.nanoTime() & 0xFFFF);
        int y = x * x;
        // x^2 her zaman >= 0, yani y >= 0 her zaman true
        return (y >= 0) | ((MAGIC_A ^ MAGIC_B) != 0);
    }

    /**
     * Opaque predicate - HER ZAMAN FALSE doner ama analiz edilemez
     */
    public static boolean opaqueFalse() {
        int x = (int) (System.nanoTime() & 0xFF) + 1; // 1-256 arasi
        // x > 0 ve x < 300 oldugu icin x*x < x*300, yani asla Integer.MAX_VALUE olmaz
        return (x * x == Integer.MAX_VALUE) & ((MAGIC_A & MAGIC_B) == 0xFFFFFFFF);
    }

    /**
     * Fake exception handler - hic calismaz ama decompiler'i karistirir
     */
    public static void fakeHandler() {
        if (opaqueFalse()) {
            try {
                throw new RuntimeException(String.valueOf(ENTROPY));
            } catch (Exception e) {
                STATE ^= e.hashCode();
                ENTROPY += STATE;
            }
        }
    }

    /**
     * Junk computation - hic ise yaramaz ama gercek kod gibi gorunur
     */
    public static int junkCompute(int seed) {
        if (opaqueFalse()) {
            int result = seed;
            for (int i = 0; i < 100; i++) {
                result ^= (i * MAGIC_A);
                result = Integer.rotateLeft(result, i & 7);
                STATE += result;
            }
            return result;
        }
        return seed;
    }

    /**
     * Fake branch - asla calistirilmaz ama decompiler gosterir
     */
    public static void fakeBranch(Object... args) {
        if (opaqueFalse()) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(arg.hashCode());
                ENTROPY ^= sb.length();
            }
            System.err.println(sb.toString());
        }
    }

    /**
     * Mixed flow - gercek ve sahte kodu karistirir
     */
    public static <T> T mixedFlow(T realValue, T fakeValue) {
        fakeHandler();
        if (opaqueTrue()) {
            junkCompute((int) ENTROPY);
            return realValue;
        } else {
            fakeBranch(fakeValue, MAGIC_A, MAGIC_B);
            return fakeValue; // Buraya asla ulasilmaz
        }
    }

    /**
     * Indirect call - method cagrisini gizle
     */
    public static Object indirectCall(Runnable action) {
        if (opaqueTrue()) {
            fakeHandler();
            action.run();
        } else {
            fakeBranch(action);
        }
        return null;
    }

    /**
     * State machine - kod akisini state machine ile karistir
     */
    public static int stateMachine(int input) {
        int state = 0;
        int result = input;

        while (state < 5) {
            switch (state) {
                case 0:
                    if (opaqueTrue()) state = 1;
                    else state = 99; // Unreachable
                    break;
                case 1:
                    result ^= MAGIC_A;
                    state = 2;
                    fakeHandler();
                    break;
                case 2:
                    if (opaqueTrue()) state = 3;
                    else result = junkCompute(result); // Unreachable
                    break;
                case 3:
                    result ^= MAGIC_A; // XOR'u geri al
                    state = 4;
                    break;
                case 4:
                    state = 5; // Cikis
                    break;
                default:
                    fakeBranch(state, result);
                    state = 5;
            }
        }
        return result;
    }

    /**
     * Number obfuscation - sabit sayilari gizle
     * Ornek: hide(10) -> 10 doner ama nasil hesaplandigi belli degil
     */
    public static int hide(int value) {
        int mask = MAGIC_A ^ MAGIC_B;
        int encoded = value ^ mask;
        fakeHandler();
        return encoded ^ mask; // XOR kendini iptal eder
    }

    /**
     * Delayed execution - kodu geciktir (timing analysis'i zorlas)
     */
    public static void delayedExec(Runnable action) {
        long start = System.nanoTime();
        fakeHandler();

        // Rastgele kisa gecikme (0-1ms)
        while (System.nanoTime() - start < RNG.nextInt(1000000)) {
            STATE++;
        }

        if (opaqueTrue()) {
            action.run();
        }
    }
}
