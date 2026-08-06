package dev.just.protect.runtime;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Runtime Integrity Check
 * Method hash'lerini kontrol eder
 * Patch atılırsa algılar
 */
public final class IntegrityGuard {
    private static final Map<String, byte[]> METHOD_HASHES = new ConcurrentHashMap<>();
    private static final AtomicBoolean integrityViolated = new AtomicBoolean(false);
    private static volatile String violationSource = null;
    private static volatile long lastCheck = 0;
    private static final long CHECK_INTERVAL = 60000; // 1 dakika

    // Fake fields
    @SuppressWarnings("unused")
    private static volatile boolean serverVerified = true;
    @SuppressWarnings("unused")
    private static volatile String clientSignature = null;

    private IntegrityGuard() {
        throw new AssertionError();
    }

    static {
        // Kritik sınıfların hash'lerini kaydet
        registerCriticalClasses();
    }

    private static void registerCriticalClasses() {
        try {
            // Kritik sınıflar - bunlar değiştirilirse algılanır
            registerClass("dev.just.modules.Function");
            registerClass("dev.just.manager.Manager");
            registerClass("dev.just.protect.runtime.l1O0I1lO");
            registerClass("dev.just.protect.runtime.O1lI0O1l");
            registerClass("dev.just.protect.runtime.lO1I0l1O");
        } catch (Exception e) {
            // Sessizce devam et
        }
    }

    private static void registerClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            byte[] hash = computeClassHash(clazz);
            METHOD_HASHES.put(className, hash);
        } catch (Exception e) {
            // Sınıf bulunamazsa atla
        }
    }

    private static byte[] computeClassHash(Class<?> clazz) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Sınıf adı
            md.update(clazz.getName().getBytes());

            // Method'lar
            for (Method m : clazz.getDeclaredMethods()) {
                md.update(m.getName().getBytes());
                md.update(m.getReturnType().getName().getBytes());
                for (Class<?> param : m.getParameterTypes()) {
                    md.update(param.getName().getBytes());
                }
            }

            return md.digest();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Periyodik integrity check
     */
    public static void check() {
        long now = System.currentTimeMillis();
        if (now - lastCheck < CHECK_INTERVAL) {
            return;
        }
        lastCheck = now;

        l1O0I1lO.fakeHandler();

        for (Map.Entry<String, byte[]> entry : METHOD_HASHES.entrySet()) {
            String className = entry.getKey();
            byte[] expectedHash = entry.getValue();

            try {
                Class<?> clazz = Class.forName(className);
                byte[] currentHash = computeClassHash(clazz);

                if (!MessageDigest.isEqual(expectedHash, currentHash)) {
                    integrityViolated.set(true);
                    violationSource = className;
                    onIntegrityViolation(className);
                    return;
                }
            } catch (Exception e) {
                // Sınıf yüklenemezse - muhtemel tampering
                integrityViolated.set(true);
                violationSource = className;
            }
        }
    }

    /**
     * Integrity ihlali algılandığında
     */
    private static void onIntegrityViolation(String source) {
        // Log (obfuscated)
        System.err.println("IG: " + (source.hashCode() ^ 0xBADC0DE));

        // Feature degrade
        TemporalCheck.incrementSuspicionExternal("Integrity violation: " + source);
    }

    /**
     * Integrity durumu
     */
    public static boolean isIntegrityViolated() {
        return integrityViolated.get();
    }

    /**
     * İhlal kaynağı
     */
    public static String getViolationSource() {
        return violationSource;
    }

    /**
     * Manuel check tetikle
     */
    public static boolean verifyClass(Class<?> clazz) {
        l1O0I1lO.fakeHandler();

        if (l1O0I1lO.opaqueFalse()) {
            return false;
        }

        byte[] expected = METHOD_HASHES.get(clazz.getName());
        if (expected == null) {
            return true; // Kayıtlı değil, skip
        }

        byte[] current = computeClassHash(clazz);
        return MessageDigest.isEqual(expected, current);
    }

    /**
     * Yeni sınıf kaydet (runtime)
     */
    public static void register(Class<?> clazz) {
        byte[] hash = computeClassHash(clazz);
        METHOD_HASHES.put(clazz.getName(), hash);
    }
}
