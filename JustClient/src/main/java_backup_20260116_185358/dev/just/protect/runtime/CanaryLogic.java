package dev.just.protect.runtime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CANARY LOGIC - Sessiz Alarm
 *
 * - Kirilma tespiti ama tepki YOK
 * - Flag set edilir
 * - Bazi ozellikler yavaslar
 * - Bazi sonuclar degrade olur
 *
 * ⚠️ Exit yok → patch noktasi bulunamaz
 */
public final class CanaryLogic {

    // Canary flags - tetiklendiginde true
    private static final ConcurrentHashMap<String, AtomicBoolean> canaries = new ConcurrentHashMap<>();

    // Degradation level (0 = normal, higher = more degraded)
    private static final AtomicInteger degradationLevel = new AtomicInteger(0);

    // Canary types
    public static final String CANARY_DEBUG = "dbg";
    public static final String CANARY_TAMPER = "tpr";
    public static final String CANARY_TIMING = "tmg";
    public static final String CANARY_INTEGRITY = "itg";
    public static final String CANARY_SNAPSHOT = "snp";
    public static final String CANARY_MESH = "msh";

    private CanaryLogic() {}

    static {
        // Canary'leri initialize et
        canaries.put(CANARY_DEBUG, new AtomicBoolean(false));
        canaries.put(CANARY_TAMPER, new AtomicBoolean(false));
        canaries.put(CANARY_TIMING, new AtomicBoolean(false));
        canaries.put(CANARY_INTEGRITY, new AtomicBoolean(false));
        canaries.put(CANARY_SNAPSHOT, new AtomicBoolean(false));
        canaries.put(CANARY_MESH, new AtomicBoolean(false));
    }

    /**
     * Canary tetikle (SESSIZ - exit yok!)
     */
    public static void trigger(String canaryId) {
        AtomicBoolean canary = canaries.get(canaryId);
        if (canary != null && !canary.get()) {
            canary.set(true);
            degradationLevel.incrementAndGet();

            // Sessiz log (sadece hash)
            System.err.println("C:" + canaryId.hashCode());
        }
    }

    /**
     * Canary tetiklenmis mi
     */
    public static boolean isTriggered(String canaryId) {
        AtomicBoolean canary = canaries.get(canaryId);
        return canary != null && canary.get();
    }

    /**
     * Herhangi bir canary tetiklenmis mi
     */
    public static boolean anyTriggered() {
        return canaries.values().stream().anyMatch(AtomicBoolean::get);
    }

    /**
     * Kac canary tetiklenmis
     */
    public static int triggeredCount() {
        return (int) canaries.values().stream().filter(AtomicBoolean::get).count();
    }

    /**
     * Degradation seviyesi
     */
    public static int getDegradationLevel() {
        return degradationLevel.get();
    }

    /**
     * Degrade edilmis hesaplama (canary tetiklenmisse yavas)
     */
    public static int degradedCompute(int value) {
        int level = degradationLevel.get();

        if (level > 0) {
            // Her level icin biraz gecikme ekle
            try {
                Thread.sleep(level * 10L);
            } catch (InterruptedException ignored) {}

            // Sonucu hafifce boz (fark edilmez ama tutarsiz)
            if (level > 3) {
                value ^= (level & 0x3);
            }
        }

        return value;
    }

    /**
     * Degrade edilmis float hesaplama
     */
    public static float degradedCompute(float value) {
        int level = degradationLevel.get();

        if (level > 2) {
            // Cok kucuk sapma ekle
            value += (level * 0.0001f);
        }

        return value;
    }

    /**
     * Degrade edilmis string (canary tetiklenmisse bazen farkli)
     */
    public static String degradedString(String original) {
        int level = degradationLevel.get();

        if (level > 5 && original.length() > 10) {
            // Bazen son karakteri degistir
            if (System.currentTimeMillis() % 100 < level) {
                return original.substring(0, original.length() - 1) + "?";
            }
        }

        return original;
    }

    /**
     * Feature availability (degrade olunca bazi ozellikler kapanir)
     */
    public static boolean isFeatureAvailable(String featureId) {
        int level = degradationLevel.get();

        // Level 3'ten sonra bazi ozellikler devre disi
        if (level >= 3) {
            int featureHash = Math.abs(featureId.hashCode());
            return (featureHash % 10) >= level;
        }

        return true;
    }

    /**
     * Performance multiplier (degrade olunca yavaslar)
     */
    public static double getPerformanceMultiplier() {
        int level = degradationLevel.get();
        return 1.0 + (level * 0.1); // Her level %10 yavaslatma
    }

    /**
     * Sessiz kontrol - tetiklenirse canary aktive et
     */
    public static void silentCheck() {
        if (AntiDebug.isDebuggerDetected()) {
            trigger(CANARY_DEBUG);
        }

        if (VMCheck.isTamperDetected()) {
            trigger(CANARY_TAMPER);
        }

        if (IntegrityCheck.isIntegrityViolated()) {
            trigger(CANARY_INTEGRITY);
        }

        if (AntiReplay.detectSnapshot()) {
            trigger(CANARY_SNAPSHOT);
        }
    }
}
