package dev.just.protect.runtime;

import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TEMPORAL CHECKS - Zamana Yayilmis Kontroller
 *
 * - Farkli thread'lerde
 * - Farkli zamanlarda
 * - Farkli fazlarda
 *
 * Reverse engineer "nerede kirildigini" anlayamaz
 */
public final class TemporalCheck {

    private static final SecureRandom random = new SecureRandom();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("JC-Worker-" + random.nextInt(1000));
        return t;
    });

    // Farkli fazlar icin state
    private static final AtomicInteger phase = new AtomicInteger(0);
    private static final AtomicLong lastValidation = new AtomicLong(System.currentTimeMillis());
    private static final AtomicInteger suspicionLevel = new AtomicInteger(0);

    // Soft fail threshold - bu seviyeye ulasinca hard fail
    private static final int SOFT_FAIL_THRESHOLD = 5;
    private static final int HARD_FAIL_THRESHOLD = 10;

    private static volatile boolean initialized = false;

    private TemporalCheck() {}

    /**
     * Temporal kontrolleri baslat
     */
    public static void init() {
        if (initialized) return;
        initialized = true;

        // Faz 1: Hemen (0-2 saniye)
        scheduler.schedule(() -> phaseCheck(1), random.nextInt(2000), TimeUnit.MILLISECONDS);

        // Faz 2: Kisa sure sonra (5-15 saniye)
        scheduler.schedule(() -> phaseCheck(2), 5000 + random.nextInt(10000), TimeUnit.MILLISECONDS);

        // Faz 3: Orta sure (30-60 saniye)
        scheduler.schedule(() -> phaseCheck(3), 30000 + random.nextInt(30000), TimeUnit.MILLISECONDS);

        // Faz 4: Uzun sure (2-5 dakika)
        scheduler.schedule(() -> phaseCheck(4), 120000 + random.nextInt(180000), TimeUnit.MILLISECONDS);

        // Periyodik kontrol (rastgele araliklar)
        scheduleRandomCheck();

        // Background integrity
        scheduler.scheduleAtFixedRate(TemporalCheck::backgroundCheck,
            10, 10 + random.nextInt(20), TimeUnit.SECONDS);
    }

    /**
     * Faz kontrolu
     */
    private static void phaseCheck(int phaseNum) {
        try {
            phase.set(phaseNum);

            // Her fazda farkli kontrol
            switch (phaseNum) {
                case 1 -> checkPhase1(); // Basic integrity
                case 2 -> checkPhase2(); // Timing analysis
                case 3 -> checkPhase3(); // Deep inspection
                case 4 -> checkPhase4(); // Final validation
            }

            lastValidation.set(System.currentTimeMillis());
        } catch (Exception e) {
            incrementSuspicion("Phase " + phaseNum + " failed");
        }
    }

    /**
     * Faz 1: Basic integrity
     */
    private static void checkPhase1() {
        // Stack trace kontrolu
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement elem : stack) {
            String cn = elem.getClassName().toLowerCase();
            if (cn.contains("debug") || cn.contains("attach") || cn.contains("agent")) {
                incrementSuspicion("Suspicious stack in phase 1");
            }
        }
    }

    /**
     * Faz 2: Timing analysis
     */
    private static void checkPhase2() {
        long start = System.nanoTime();

        // Dummy computation
        int result = 0;
        for (int i = 0; i < 50000; i++) {
            result ^= i * 31;
        }

        long elapsed = System.nanoTime() - start;

        // Normalden 10x yavas = suphe (debugger?)
        if (elapsed > 50_000_000L) { // 50ms
            incrementSuspicion("Timing anomaly in phase 2");
        }

        // Result kullan
        if (result == Integer.MIN_VALUE + 7) System.nanoTime();
    }

    /**
     * Faz 3: Deep inspection
     */
    private static void checkPhase3() {
        // Thread sayisi kontrolu
        int threadCount = Thread.activeCount();
        Thread[] threads = new Thread[threadCount + 10];
        Thread.enumerate(threads);

        int suspiciousThreads = 0;
        for (Thread t : threads) {
            if (t != null) {
                String name = t.getName().toLowerCase();
                if (name.contains("attach") || name.contains("jdwp") ||
                    name.contains("debug") || name.contains("profiler")) {
                    suspiciousThreads++;
                }
            }
        }

        if (suspiciousThreads > 0) {
            incrementSuspicion("Suspicious threads: " + suspiciousThreads);
        }

        // ClassLoader kontrolu
        ClassLoader cl = TemporalCheck.class.getClassLoader();
        if (cl != null) {
            String clName = cl.getClass().getName().toLowerCase();
            if (clName.contains("agent") || clName.contains("transform")) {
                incrementSuspicion("Suspicious classloader");
            }
        }
    }

    /**
     * Faz 4: Final validation
     */
    private static void checkPhase4() {
        // Onceki fazlar tamamlandi mi
        if (phase.get() < 3) {
            incrementSuspicion("Phase sequence broken");
        }

        // Memory canary kontrolu
        if (!RuntimeGenerator.checkCanaries()) {
            incrementSuspicion("Canary check failed");
        }

        // Integrity check
        if (IntegrityCheck.isIntegrityViolated()) {
            incrementSuspicion("Integrity violated");
        }
    }

    /**
     * Background periyodik kontrol
     */
    private static void backgroundCheck() {
        try {
            // Son validasyondan bu yana cok zaman gecti mi
            long timeSinceLastValidation = System.currentTimeMillis() - lastValidation.get();

            // 10 dakikadan fazla = muhtemel freeze/debug
            if (timeSinceLastValidation > 600000 && phase.get() > 0) {
                incrementSuspicion("Long pause detected");
            }

            // Quick timing check
            long start = System.nanoTime();
            Thread.sleep(1);
            long elapsed = System.nanoTime() - start;

            // 1ms sleep 100ms'den fazla surdu = suphe
            if (elapsed > 100_000_000L) {
                incrementSuspicion("Sleep timing anomaly");
            }

        } catch (InterruptedException ignored) {
        } catch (Exception e) {
            incrementSuspicion("Background check exception");
        }
    }

    /**
     * Rastgele zamanli kontrol schedule et
     */
    private static void scheduleRandomCheck() {
        int delay = 60000 + random.nextInt(120000); // 1-3 dakika
        scheduler.schedule(() -> {
            randomCheck();
            scheduleRandomCheck(); // Tekrar schedule et
        }, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Rastgele kontrol
     */
    private static void randomCheck() {
        // Rastgele bir kontrol sec
        int check = random.nextInt(4);
        switch (check) {
            case 0 -> checkPhase1();
            case 1 -> checkPhase2();
            case 2 -> checkPhase3();
            case 3 -> {
                if (AntiDebug.isDebuggerDetected()) {
                    incrementSuspicion("Debugger in random check");
                }
            }
        }
    }

    /**
     * External call için public wrapper
     */
    public static void incrementSuspicionExternal(String reason) {
        incrementSuspicion(reason);
    }

    /**
     * Suspicion seviyesini artir (SOFT FAIL)
     */
    private static void incrementSuspicion(String reason) {
        int level = suspicionLevel.incrementAndGet();

        // Log (obfuscated)
        System.err.println("W: " + reason.hashCode());

        if (level >= HARD_FAIL_THRESHOLD) {
            // Hard fail - kapat
            hardFail();
        } else if (level >= SOFT_FAIL_THRESHOLD) {
            // Soft fail - ozellikleri devre disi birak
            softFail();
        }
    }

    /**
     * Soft fail - bazi ozellikleri devre disi birak
     */
    private static void softFail() {
        // Premium ozellikleri kapat
        // Client hala calisir ama kisitli
        System.err.println("Feature degradation active");
    }

    /**
     * Hard fail - tamamen kapat
     * NOT: Gelistirme/test modunda devre disi birakildi
     */
    private static void hardFail() {
        // Gelistirme modunda kapatma devre disi
        // Uretim icin asagidaki satirlari aktif et:
        // StringGuard.scrambleMemory();
        // Runtime.getRuntime().halt(0);
        System.err.println("Hard fail tetiklendi (devre disi)");
    }

    public static int getSuspicionLevel() {
        return suspicionLevel.get();
    }

    public static int getCurrentPhase() {
        return phase.get();
    }
}
