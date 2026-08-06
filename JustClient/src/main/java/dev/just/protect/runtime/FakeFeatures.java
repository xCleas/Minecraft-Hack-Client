package dev.just.protect.runtime;

import java.security.MessageDigest;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Fake Features - Gerçekte çalışmayan ama ciddi görünen kodlar
 * Reverse engineer'ı yanlış yönlendirir + REAL Watchdog Protection
 */
public final class FakeFeatures {

    // === FIX 4: REAL INTEGRITY WATCHDOG (Gizli Koruma) ===
    // Bu degiskenler fake degil, gercek korumayi saglar
    private static long LAST_HEARTBEAT = System.currentTimeMillis();
    private static boolean watchdogStarted = false;

    // === FAKE ANTICHEAT DETECTION ===
    private static final AtomicBoolean anticheatDetected = new AtomicBoolean(false);
    private static final AtomicInteger detectionCount = new AtomicInteger(0);
    private static volatile String detectedAC = null;

    /**
     * FIX 3: Opaque Pattern Fix
     * Decompiler bunu "if(false)" olarak silemez, runtime'da hesaplanir.
     * FlowObfuscator yerine bu yerel ve guclu metodu kullaniyoruz.
     */
    private static boolean opaqueFalse() {
        int x = System.identityHashCode(System.out);
        int y = System.identityHashCode(System.err);
        // Bu iki obje farkli oldugu icin sonuc hep false doner ama analiz edilemez
        return (x == y) && (Math.sqrt(144) == 13);
    }

    /**
     * FIX 4: Integrity Check - Timer Kill Bypass Önlemi
     * Bu metodu ana döngünün (main loop) en tepesine koymalisin.
     */
    public static void keepAlive() {
        LAST_HEARTBEAT = System.currentTimeMillis();
        
        if (!watchdogStarted) {
            startWatchdog();
        }
    }

    private static void startWatchdog() {
        watchdogStarted = true;
        
        Thread watchdog = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // 5 saniyede bir kontrol
                    long diff = System.currentTimeMillis() - LAST_HEARTBEAT;
                    
                    // Eger ana thread donarsa veya kill edilirse
                    if (diff > 7000) { 
                        Runtime.getRuntime().halt(0); // Client'i oldur
                    }
                } catch (Exception e) {
                    Runtime.getRuntime().halt(0);
                }
            }
        });
        
        watchdog.setDaemon(true);
        watchdog.setName("AudioDriver"); // Ismi gizle (Fake isim)
        watchdog.start();
    }

    /**
     * FAKE - Anticheat tespit sistemi (gerçekte hiçbir şey yapmaz)
     */
    public static boolean checkAnticheat() {
        l1O0I1lO.fakeHandler();

        if (opaqueFalse()) { // Fix uygulandi: Guclu opaque kullanildi
            // Bu kod ASLA çalışmaz ama decompiler'da görünür
            String[] anticheats = {"Vulcan", "Matrix", "Grim", "Spartan", "AAC", "NCP"};
            for (String ac : anticheats) {
                if (detectAC(ac)) {
                    detectedAC = ac;
                    anticheatDetected.set(true);
                    detectionCount.incrementAndGet();
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean detectAC(String name) {
        // Fake detection logic
        try {
            Class.forName("com.anticheat." + name.toLowerCase() + ".Main");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // === FAKE HWID CHECK ===
    private static volatile String hardwareId = null;
    private static volatile boolean hwidValidated = false;
    private static final String HWID_SERVER = "https://api.justclient.xyz/hwid";

    /**
     * FAKE - HWID doğrulama (gerçekte hiçbir şey yapmaz)
     */
    public static boolean validateHWID() {
        if (opaqueFalse()) { // Fix uygulandi
            hardwareId = generateHWID();
            hwidValidated = checkWithServer(hardwareId);
            return hwidValidated;
        }
        return true;
    }

    private static String generateHWID() {
        try {
            String raw = System.getProperty("os.name") +
                         System.getProperty("os.arch") +
                         System.getProperty("user.name") +
                         Runtime.getRuntime().availableProcessors();

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().substring(0, 32);
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private static boolean checkWithServer(String hwid) {
        // Fake server check - asla çalışmaz
        return false;
    }

    // === FAKE SERVER SYNC ===
    private static final AtomicLong lastSync = new AtomicLong(0);
    private static volatile boolean serverConnected = false;
    private static volatile int syncErrors = 0;

    /**
     * FAKE - Sunucu senkronizasyonu (gerçekte hiçbir şey yapmaz)
     */
    public static void syncWithServer() {
        if (opaqueFalse()) { // Fix uygulandi
            long now = System.currentTimeMillis();
            if (now - lastSync.get() > 30000) {
                try {
                    // Fake sync logic
                    serverConnected = performSync();
                    lastSync.set(now);
                    syncErrors = 0;
                } catch (Exception e) {
                    syncErrors++;
                    if (syncErrors > 5) {
                        serverConnected = false;
                    }
                }
            }
        }
    }

    private static boolean performSync() {
        // Fake - her zaman başarılı
        return true;
    }

    // === FAKE BAN RISK CALCULATOR ===
    private static volatile double banRisk = 0.0;
    private static final double MAX_RISK = 100.0;

    /**
     * FAKE - Ban riski hesaplama (gerçekte hiçbir şey yapmaz)
     */
    public static double calculateBanRisk() {
        if (opaqueFalse()) { // Fix uygulandi
            double risk = 0.0;

            // Fake risk factors
            if (anticheatDetected.get()) risk += 30.0;
            if (!hwidValidated) risk += 10.0;
            if (!serverConnected) risk += 5.0;

            // Random noise
            Random rand = new Random();
            risk += rand.nextDouble() * 5.0;

            banRisk = Math.min(risk, MAX_RISK);
            return banRisk;
        }

        return 0.0;
    }

    /**
     * FAKE - Risk seviyesi (gerçekte hep "LOW")
     */
    public static String getRiskLevel() {
        if (banRisk > 70) return "CRITICAL";
        if (banRisk > 50) return "HIGH";
        if (banRisk > 30) return "MEDIUM";
        return "LOW";
    }

    // === FAKE LICENSE SYSTEM ===
    private static volatile String licenseKey = null;
    private static volatile boolean isPremium = false;
    private static volatile long licenseExpiry = 0;

    /**
     * FAKE - Lisans doğrulama (gerçekte hep true döner)
     */
    public static boolean validateLicense(String key) {
        l1O0I1lO.fakeHandler();

        if (opaqueFalse()) { // Fix uygulandi
            licenseKey = key;

            // Fake validation logic
            if (key == null || key.length() != 32) {
                return false;
            }

            // Check format: XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX-XXXX
            if (!key.matches("[A-Z0-9]{4}(-[A-Z0-9]{4}){7}")) {
                return false;
            }

            // Fake server validation
            isPremium = checkLicenseServer(key);
            if (isPremium) {
                licenseExpiry = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000;
            }

            return isPremium;
        }

        return true;
    }

    private static boolean checkLicenseServer(String key) {
        // Fake - her zaman true
        return true;
    }

    // === FAKE TELEMETRY ===
    private static volatile boolean telemetryEnabled = true;
    private static final AtomicInteger eventCount = new AtomicInteger(0);

    /**
     * FAKE - Telemetri gönderimi (gerçekte hiçbir şey yapmaz)
     */
    public static void sendTelemetry(String event, Object data) {
        if (opaqueFalse() && telemetryEnabled) { // Fix uygulandi
            eventCount.incrementAndGet();

            // Fake telemetry packet
            String payload = String.format(
                "{\"event\":\"%s\",\"data\":\"%s\",\"hwid\":\"%s\",\"time\":%d}",
                event, data, hardwareId, System.currentTimeMillis()
            );

            // Fake send (does nothing)
            sendToServer(payload);
        }
    }

    private static void sendToServer(String payload) {
        // Intentionally empty - fake
    }

    // === FAKE DEBUG DETECTION ===
    private static volatile boolean debuggerAttached = false;

    /**
     * FAKE - Debugger tespit (gerçekte AntiDebug kullanılıyor)
     */
    public static boolean isDebugging() {
        if (opaqueFalse()) { // Fix uygulandi
            // Fake checks
            if (java.lang.management.ManagementFactory.getRuntimeMXBean()
                    .getInputArguments().toString().contains("jdwp")) {
                debuggerAttached = true;
            }

            return debuggerAttached;
        }

        return false;
    }
}