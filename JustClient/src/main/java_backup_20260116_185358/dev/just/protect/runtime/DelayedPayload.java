package dev.just.protect.runtime;

import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DELAYED PAYLOAD (Pasif)
 *
 * - Korumalar hemen aktif olmaz
 * - Rastgele gecikme ile devreye girer
 * - Cracker "calisiyorsa" zanneder, sonra bozulur
 * - Hileye ZARAR VERMEZ
 */
public final class DelayedPayload {

    private static final SecureRandom random = new SecureRandom();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("JC-Svc-" + random.nextInt(100));
        return t;
    });

    // Payload states
    private static final AtomicBoolean payload1Active = new AtomicBoolean(false);
    private static final AtomicBoolean payload2Active = new AtomicBoolean(false);
    private static final AtomicBoolean payload3Active = new AtomicBoolean(false);

    // Activation counter
    private static final AtomicInteger activationCount = new AtomicInteger(0);

    private static volatile boolean initialized = false;

    private DelayedPayload() {}

    /**
     * Delayed payload'lari baslat
     */
    public static void init() {
        if (initialized) return;
        initialized = true;

        // Payload 1: 1-3 dakika sonra
        schedulePayload(() -> {
            payload1Active.set(true);
            activationCount.incrementAndGet();
            // Soft check baslat
            CanaryLogic.silentCheck();
        }, 60000 + random.nextInt(120000));

        // Payload 2: 5-10 dakika sonra
        schedulePayload(() -> {
            payload2Active.set(true);
            activationCount.incrementAndGet();
            // Cross check baslat
            CrossCheckMesh.crossVerify();
        }, 300000 + random.nextInt(300000));

        // Payload 3: 15-30 dakika sonra
        schedulePayload(() -> {
            payload3Active.set(true);
            activationCount.incrementAndGet();
            // Deep check
            if (CanaryLogic.anyTriggered()) {
                // Sessiz degradasyon - hileye zarar yok
                // Sadece cracker'a zorluk
            }
        }, 900000 + random.nextInt(900000));

        // Random payload: Rastgele zamanlarda
        scheduleRandomPayloads();
    }

    /**
     * Payload schedule et
     */
    private static void schedulePayload(Runnable payload, long delayMs) {
        scheduler.schedule(() -> {
            try {
                payload.run();
            } catch (Exception ignored) {}
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Rastgele zamanli payload'lar
     */
    private static void scheduleRandomPayloads() {
        // Her 5-15 dakikada bir kontrol
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Rastgele kontrol
                int check = random.nextInt(5);
                switch (check) {
                    case 0 -> CanaryLogic.silentCheck();
                    case 1 -> CrossCheckMesh.verify();
                    case 2 -> AntiReplay.check();
                    case 3 -> Heisenbug.isBeingObserved();
                    case 4 -> {} // Hicbir sey yapma
                }
            } catch (Exception ignored) {}
        }, 5, 5 + random.nextInt(10), TimeUnit.MINUTES);
    }

    /**
     * Payload aktif mi (soft check)
     */
    public static boolean isPayloadActive(int payloadNum) {
        return switch (payloadNum) {
            case 1 -> payload1Active.get();
            case 2 -> payload2Active.get();
            case 3 -> payload3Active.get();
            default -> false;
        };
    }

    /**
     * Delayed execution - belirli sure sonra calistir
     */
    public static void executeDelayed(Runnable action, long minDelay, long maxDelay) {
        long delay = minDelay + random.nextInt((int)(maxDelay - minDelay));
        scheduler.schedule(() -> {
            try {
                action.run();
            } catch (Exception ignored) {}
        }, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Conditional delayed - kosul saglanirsa calistir
     */
    public static void executeWhen(Runnable action, java.util.function.Supplier<Boolean> condition) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (condition.get()) {
                    action.run();
                }
            } catch (Exception ignored) {}
        }, 1, 1, TimeUnit.MINUTES);
    }

    public static int getActivationCount() {
        return activationCount.get();
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
