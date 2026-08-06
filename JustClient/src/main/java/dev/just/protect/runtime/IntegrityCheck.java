package dev.just.protect.runtime;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * JAR Integrity Check
 * Dosya degisikliklerini algilar
 */
public final class IntegrityCheck {

    private static final Map<String, byte[]> classHashes = new HashMap<>();
    private static volatile boolean integrityViolated = false;
    private static String jarPath;

    private IntegrityCheck() {}

    /**
     * Integrity kontrollerini baslat
     */
    public static void init() {
        try {
            // JAR dosya yolunu bul
            jarPath = IntegrityCheck.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath();

            // Ilk hash'leri al
            computeInitialHashes();

            // Periyodik kontrol
            Timer timer = new Timer("IntegrityCheck", true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    verifyIntegrity();
                }
            }, 5000, 5000);

        } catch (Exception e) {
            // Sessizce devam et
        }
    }

    /**
     * Baslangic hash'lerini hesapla
     */
    private static void computeInitialHashes() {
        String[] criticalClasses = {
            "dev/just/JustClient.class",
            "dev/just/protect/runtime/AntiDebug.class",
            "dev/just/protect/runtime/VMCheck.class",
            "dev/just/protect/runtime/IntegrityCheck.class",
            "dev/just/protect/runtime/StringGuard.class"
        };

        for (String className : criticalClasses) {
            try {
                byte[] hash = computeClassHash(className);
                if (hash != null) {
                    classHashes.put(className, hash);
                }
            } catch (Exception ignored) {}
        }
    }

    /**
     * Sinif hash'ini hesapla
     */
    private static byte[] computeClassHash(String className) {
        try {
            InputStream is = IntegrityCheck.class.getClassLoader()
                .getResourceAsStream(className);

            if (is == null) return null;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;

            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            is.close();

            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Integrity kontrolu
     */
    private static void verifyIntegrity() {
        if (integrityViolated) return;

        for (Map.Entry<String, byte[]> entry : classHashes.entrySet()) {
            try {
                byte[] currentHash = computeClassHash(entry.getKey());
                byte[] originalHash = entry.getValue();

                if (currentHash == null || !java.util.Arrays.equals(currentHash, originalHash)) {
                    onIntegrityViolation("Class modified: " + entry.getKey());
                    return;
                }
            } catch (Exception ignored) {}
        }

        // Stack trace kontrolu - beklenmedik caller
        checkStackTrace();
    }

    /**
     * Stack trace kontrolu
     */
    private static void checkStackTrace() {
        try {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            for (StackTraceElement element : stack) {
                String className = element.getClassName().toLowerCase();

                // Suspicious patterns
                if (className.contains("agent") ||
                    className.contains("instrument") ||
                    className.contains("transform") ||
                    className.contains("hook") ||
                    className.contains("inject") ||
                    className.contains("patch")) {

                    onIntegrityViolation("Suspicious stack trace: " + element.getClassName());
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    /**
     * Integrity ihlali
     */
    private static void onIntegrityViolation(String reason) {
        integrityViolated = true;

        // Sahte log
        System.err.println("java.lang.VerifyError: " + reason.hashCode());

        // String cache'i temizle
        StringGuard.scrambleMemory();

        // Uygulamayi kapat
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {}

        Runtime.getRuntime().halt(0);
    }

    public static boolean isIntegrityViolated() {
        return integrityViolated;
    }

    /**
     * Method integrity check (runtime)
     */
    public static void checkMethod() {
        if (integrityViolated || AntiDebug.isDebuggerDetected() || VMCheck.isTamperDetected()) {
            Runtime.getRuntime().halt(0);
        }
    }
}
