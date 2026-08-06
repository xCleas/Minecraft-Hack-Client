package dev.just.protect.runtime;

import java.util.concurrent.atomic.AtomicLong;

/**
 * CROSS-CHECK MESH - Birbirini Dogrulayan Kontroller
 *
 * A bozulursa → B sessizce bozulur
 * B bozulursa → C yanlis sonuc verir
 * C bozulursa → A tetiklenir
 *
 * 🕸️ Tek bir noktayi kirmak yetmez
 */
public final class CrossCheckMesh {

    // Mesh nodes - birbirine bagli
    private static final AtomicLong nodeA = new AtomicLong();
    private static final AtomicLong nodeB = new AtomicLong();
    private static final AtomicLong nodeC = new AtomicLong();
    private static final AtomicLong nodeD = new AtomicLong();

    // Mesh integrity checksum
    private static final AtomicLong meshChecksum = new AtomicLong();

    private static volatile boolean initialized = false;

    private CrossCheckMesh() {}

    /**
     * Mesh'i initialize et
     */
    public static void init() {
        if (initialized) return;

        long seed = System.nanoTime() ^ AntiReplay.getSessionId();

        // Node'lari birbirine bagimli olarak ayarla
        nodeA.set(seed);
        nodeB.set(computeB(seed));
        nodeC.set(computeC(nodeB.get()));
        nodeD.set(computeD(nodeC.get()));

        // Checksum
        meshChecksum.set(computeChecksum());

        initialized = true;
    }

    // Node hesaplama fonksiyonlari (birbirine bagimli)

    private static long computeB(long a) {
        return Long.rotateLeft(a, 17) ^ 0xCAFEBABEDEADBEEFL;
    }

    private static long computeC(long b) {
        return Long.rotateRight(b, 23) ^ 0x0123456789ABCDEFL;
    }

    private static long computeD(long c) {
        return (c * 31) ^ (c >> 16);
    }

    private static long computeA(long d) {
        // D'den A'yi geri hesapla (dongusel)
        return Long.rotateRight(d ^ 0xFEDCBA9876543210L, 11);
    }

    private static long computeChecksum() {
        return nodeA.get() ^ nodeB.get() ^ nodeC.get() ^ nodeD.get();
    }

    /**
     * Mesh integrity kontrolu
     */
    public static boolean verify() {
        if (!initialized) {
            init();
            return true;
        }

        // Her node dogru hesaplanmis mi
        long a = nodeA.get();
        long b = nodeB.get();
        long c = nodeC.get();
        long d = nodeD.get();

        boolean valid = true;

        // A -> B kontrolu
        if (computeB(a) != b) {
            valid = false;
            CanaryLogic.trigger(CanaryLogic.CANARY_MESH);
        }

        // B -> C kontrolu
        if (computeC(b) != c) {
            valid = false;
            CanaryLogic.trigger(CanaryLogic.CANARY_MESH);
        }

        // C -> D kontrolu
        if (computeD(c) != d) {
            valid = false;
            CanaryLogic.trigger(CanaryLogic.CANARY_MESH);
        }

        // Checksum kontrolu
        if (computeChecksum() != meshChecksum.get()) {
            valid = false;
            CanaryLogic.trigger(CanaryLogic.CANARY_MESH);
        }

        return valid;
    }

    /**
     * Node A'yi guncelle (cascade effect)
     */
    public static void updateA(long newValue) {
        nodeA.set(newValue);
        // Cascade: A degisince B, C, D de degismeli
        nodeB.set(computeB(newValue));
        nodeC.set(computeC(nodeB.get()));
        nodeD.set(computeD(nodeC.get()));
        meshChecksum.set(computeChecksum());
    }

    /**
     * Mesh-protected deger al
     * Mesh bozuksa yanlis deger doner
     */
    public static int getMeshProtectedValue(int original) {
        if (!verify()) {
            // Mesh bozuk - sessizce yanlis deger don
            return original ^ (int) (nodeA.get() & 0xFF);
        }
        return original;
    }

    /**
     * Mesh-protected hesaplama
     */
    public static long compute(long input) {
        if (!verify()) {
            // Mesh bozuk - yanlis hesaplama
            return input ^ meshChecksum.get();
        }

        // Normal hesaplama
        return input ^ nodeA.get() ^ nodeB.get();
    }

    /**
     * Cross-verify with other systems
     */
    public static void crossVerify() {
        // AntiDebug ile cross-check
        if (AntiDebug.isDebuggerDetected()) {
            // A'yi boz
            nodeA.updateAndGet(v -> v ^ System.nanoTime());
        }

        // VMCheck ile cross-check
        if (VMCheck.isTamperDetected()) {
            // B'yi boz
            nodeB.updateAndGet(v -> v ^ System.nanoTime());
        }

        // IntegrityCheck ile cross-check
        if (IntegrityCheck.isIntegrityViolated()) {
            // C'yi boz
            nodeC.updateAndGet(v -> v ^ System.nanoTime());
        }

        // Snapshot ile cross-check
        if (AntiReplay.detectSnapshot()) {
            // D'yi boz
            nodeD.updateAndGet(v -> v ^ System.nanoTime());
        }
    }

    /**
     * Mesh baglantili string decode
     */
    public static String meshDecode(String encoded) {
        if (!verify()) {
            // Mesh bozuk - corrupted string don
            return encoded.substring(0, Math.min(3, encoded.length())) + "...";
        }
        return StringGuard.g(encoded);
    }
}
