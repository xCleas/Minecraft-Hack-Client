package dev.just.protect.runtime;

/**
 * ULTRA PROTECTION SYSTEM v2.0
 *
 * Tum korumalari baslatir ve yonetir
 * SOFT FAIL yaklasimi - legit kullanicilara zarar vermez
 */
public final class UltraProtect {

    private static volatile boolean initialized = false;
    private static volatile boolean protectionActive = true;

    private UltraProtect() {}

    /**
     * Tum korumalari baslat
     * Bu metod JustClient.onInitialize() icinde cagrilmali
     */
    public static void init() {
        if (initialized) return;

        try {
            // ===== TEMEL KORUMALAR =====

            // 1. Anti-Debug - debugger algila
            AntiDebug.init();

            // 2. VM Check - sanal makine/sandbox algila
            VMCheck.init();

            // 3. String Guard - string sifreleme
            StringGuard.init();

            // 4. Runtime Generator - canary'leri baslat
            RuntimeGenerator.initCanaries();

            // 5. Integrity Check - dosya butunlugu
            IntegrityCheck.init();

            // ===== GELISMIS KORUMALAR =====

            // 6. Cross-Check Mesh - birbirini dogrulayan kontroller
            CrossCheckMesh.init();

            // 7. Temporal Checks - zamana yayilmis kontroller
            TemporalCheck.init();

            // 8. Anti-Replay - snapshot korumasi
            AntiReplay.check();

            // ===== CRAFTRISE LEVEL KORUMALAR =====

            // 9. Flow Obfuscation - kod akisi karistirma
            FlowObfuscator.fakeHandler();

            // 10. Class Guard - class koruma
            ClassGuard.checkClassIntegrity(UltraProtect.class);

            // ===== SOFT FAIL KONTROL =====

            // Ilk kontrol (soft fail)
            if (AntiDebug.isDebuggerDetected()) {
                CanaryLogic.trigger(CanaryLogic.CANARY_DEBUG);
            }

            if (VMCheck.isTamperDetected()) {
                CanaryLogic.trigger(CanaryLogic.CANARY_TAMPER);
            }

            if (IntegrityCheck.isIntegrityViolated()) {
                CanaryLogic.trigger(CanaryLogic.CANARY_INTEGRITY);
            }

            // Kritik seviye kontrolu
            if (CanaryLogic.triggeredCount() >= 3) {
                // 3+ canary = muhtemel crack, sessiz degradasyon
                // Hard fail YOK - patch noktasi bulunamaz
            }

            initialized = true;

        } catch (Exception e) {
            // Hata durumunda soft fail
            CanaryLogic.trigger(CanaryLogic.CANARY_TAMPER);
        }
    }

    /**
     * Koruma durumunu kontrol et
     * Kritik metodlarin basinda cagrilmali
     */
    public static void check() {
        if (!protectionActive) return;

        // Soft check - canary tetikle, exit yapma
        CanaryLogic.silentCheck();
        CrossCheckMesh.crossVerify();
        AntiReplay.check();
    }

    /**
     * Hizli kontrol - sadece debugger (soft fail)
     */
    public static void quickCheck() {
        if (!protectionActive) return;

        if (Heisenbug.isBeingObserved()) {
            CanaryLogic.trigger(CanaryLogic.CANARY_DEBUG);
        }
    }

    /**
     * String decode wrapper (korunmali)
     */
    public static String s(String enc) {
        quickCheck();

        // Mesh kontrolu
        if (!CrossCheckMesh.verify()) {
            return CrossCheckMesh.meshDecode(enc);
        }

        // Heisenbug - gozlemleniyorsa farkli davran
        if (Heisenbug.isBeingObserved()) {
            return CanaryLogic.degradedString(StringGuard.g(enc));
        }

        return StringGuard.g(enc);
    }

    /**
     * String decode with ID
     */
    public static String s(int id, String enc) {
        quickCheck();
        return StringGuard.g(id, enc);
    }

    /**
     * Korunmali hesaplama
     */
    public static int compute(int value) {
        // Semantic noise ekle
        value = SemanticNoise.inject(value);

        // Execution path randomization
        value = ExecutionPath.multiPath(value);

        // Heisenbug
        value = Heisenbug.compute(value);

        // Asimetrik davranis
        value = Heisenbug.asymmetric(value);

        // Mesh korumasi
        value = CrossCheckMesh.getMeshProtectedValue(value);

        // Canary degradation
        value = CanaryLogic.degradedCompute(value);

        return value;
    }

    /**
     * Korunmali float hesaplama
     */
    public static float compute(float value) {
        value = Heisenbug.compute(value);
        value = Heisenbug.asymmetricFloat(value);
        value = CanaryLogic.degradedCompute(value);
        return value;
    }

    /**
     * Feature availability check
     */
    public static boolean isFeatureAvailable(String featureId) {
        return CanaryLogic.isFeatureAvailable(featureId);
    }

    /**
     * Performance multiplier
     */
    public static double getPerformanceMultiplier() {
        return CanaryLogic.getPerformanceMultiplier();
    }

    /**
     * Degradation level
     */
    public static int getDegradationLevel() {
        return CanaryLogic.getDegradationLevel();
    }

    /**
     * Koruma aktif mi
     */
    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean isProtectionActive() {
        return protectionActive;
    }

    /**
     * Debug info (obfuscated)
     */
    public static String getStatus() {
        return String.format("P:%d,C:%d,O:%d,D:%d",
            TemporalCheck.getCurrentPhase(),
            CanaryLogic.triggeredCount(),
            Heisenbug.getObservationCount(),
            CanaryLogic.getDegradationLevel());
    }
}
