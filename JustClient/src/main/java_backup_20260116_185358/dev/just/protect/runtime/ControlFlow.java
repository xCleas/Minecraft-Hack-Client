package dev.just.protect.runtime;


public final class ControlFlow {

    private static volatile int SEED = (int) System.nanoTime();

    private ControlFlow() {}

    public static int next(int input, int mod) {
        SEED ^= input;
        SEED += 0x9E3779B9;
        SEED ^= (SEED >>> 16);
        int r = SEED % mod;
        return r < 0 ? -r : r;
    }
}
