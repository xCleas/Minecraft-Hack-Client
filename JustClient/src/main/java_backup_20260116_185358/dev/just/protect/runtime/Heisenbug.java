package dev.just.protect.runtime;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HEISENBUG KORUMASI
 *
 * Analiz edildikce davranis degistirir:
 * - Debugger varken farkli sonuc
 * - Stacktrace cagrilinca farkli logic
 * - Profiling acikken performans dususu
 *
 * 👁️ Reverse engineer gozlem yaptikca hata uretir
 *
 * ASIMETRIK DAVRANIS
 * - Crack'li surum calisiyor gibi gorunur ama ayni davranmaz
 * - Deterministik olmayan kucuk farklar
 * - Hesaplamada mikroskobik sapmalar
 * - Uzun vadede veri bozulmasi
 */
public final class Heisenbug {

    private static final SecureRandom random = new SecureRandom();
    private static final AtomicInteger observationCount = new AtomicInteger(0);

    // Observation state
    private static volatile boolean beingObserved = false;
    private static volatile long lastObservation = 0;

    private Heisenbug() {}

    /**
     * Gozlem tespiti - stacktrace/debug varsa true
     */
    public static boolean isBeingObserved() {
        // Onceki gozlem state'ini kontrol et
        if (beingObserved && System.currentTimeMillis() - lastObservation < 5000) {
            return true;
        }

        boolean observed = false;

        // Debugger kontrolu
        if (AntiDebug.isDebuggerDetected()) {
            observed = true;
        }

        // Stack trace derinlik kontrolu
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        if (stack.length > 50) {
            // Cok derin stack = muhtemel profiler/debugger
            observed = true;
        }

        // Suspicious stack frames
        for (StackTraceElement elem : stack) {
            String cn = elem.getClassName().toLowerCase();
            if (cn.contains("debug") || cn.contains("trace") ||
                cn.contains("profile") || cn.contains("instrument") ||
                cn.contains("reflect") || cn.contains("proxy")) {
                observed = true;
                break;
            }
        }

        // Timing anomaly
        long start = System.nanoTime();
        // Dummy op
        int x = 0;
        for (int i = 0; i < 1000; i++) x += i;
        long elapsed = System.nanoTime() - start;

        if (elapsed > 10_000_000) { // 10ms cok fazla
            observed = true;
        }

        if (observed) {
            beingObserved = true;
            lastObservation = System.currentTimeMillis();
            observationCount.incrementAndGet();
        }

        // x kullan
        if (x == -999) System.nanoTime();

        return observed;
    }

    /**
     * Heisenbug-protected hesaplama
     * Gozlemleniyorsa farkli sonuc
     */
    public static int compute(int value) {
        if (isBeingObserved()) {
            // Gozlemleniyorsa farkli hesaplama
            int noise = observationCount.get() % 7;
            return value + noise;
        }
        return value;
    }

    /**
     * Heisenbug-protected float hesaplama
     */
    public static float compute(float value) {
        if (isBeingObserved()) {
            // Mikroskobik sapma
            float noise = observationCount.get() * 0.00001f;
            return value + noise;
        }
        return value;
    }

    /**
     * Asimetrik davranis - crack'li versiyonda farkli
     */
    public static int asymmetric(int value) {
        // Canary tetiklenmisse (muhtemel crack)
        if (CanaryLogic.anyTriggered()) {
            // Kucuk ama birikimli sapma
            int drift = CanaryLogic.triggeredCount();
            return value + (drift % 3) - 1; // -1, 0, veya +1 sapma
        }

        // Mesh bozuksa (muhtemel patch)
        if (!CrossCheckMesh.verify()) {
            // Farkli sapma
            return value ^ (observationCount.get() & 0x3);
        }

        return value;
    }

    /**
     * Asimetrik float - uzun vadede veri bozulmasi
     */
    public static float asymmetricFloat(float value) {
        if (CanaryLogic.anyTriggered()) {
            // Cok kucuk ama birikimli hata
            int count = CanaryLogic.triggeredCount();
            return value * (1.0f + count * 0.00001f);
        }
        return value;
    }

    /**
     * Deterministik olmayan davranis
     * Ayni input, bazen farkli output (crack tespiti icin)
     */
    public static int nondeterministic(int value) {
        if (CanaryLogic.getDegradationLevel() > 0) {
            // Degradation varsa bazen farkli sonuc
            if (System.currentTimeMillis() % 100 < CanaryLogic.getDegradationLevel() * 5) {
                return value ^ 1;
            }
        }
        return value;
    }

    /**
     * Timing-based davranis
     * Profiler acikken yavas
     */
    public static void timingProtected() {
        if (isBeingObserved()) {
            // Gozlemleniyorsa yavasla
            try {
                Thread.sleep(observationCount.get() * 10L);
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * Stack-sensitive hesaplama
     * Farkli call site'dan farkli sonuc
     */
    public static int stackSensitive(int value) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        // Caller'a gore farkli davranis
        if (stack.length > 3) {
            String caller = stack[3].getClassName();
            int callerHash = caller.hashCode();

            // Beklenmeyen caller = farkli sonuc
            if (!caller.startsWith("dev.just")) {
                return value ^ (callerHash & 0xF);
            }
        }

        return value;
    }

    /**
     * Progressive degradation
     * Her gozlemde biraz daha bozul
     */
    public static int progressiveDegradation(int value) {
        int observations = observationCount.get();

        if (observations > 0) {
            // Her 10 gozlemde 1 bit flip
            int flips = observations / 10;
            if (flips > 0) {
                value ^= (1 << (flips % 16));
            }
        }

        return value;
    }

    public static int getObservationCount() {
        return observationCount.get();
    }
}
