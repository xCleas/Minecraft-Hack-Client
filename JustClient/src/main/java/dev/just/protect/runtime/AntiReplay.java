package dev.just.protect.runtime;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ANTI-REPLAY / ANTI-SNAPSHOT Protection
 *
 * - Memory snapshot ise yaramaz
 * - Seed'ler tek kullanimlik
 * - Snapshot restore → bozulma
 */
public final class AntiReplay {

    private static final SecureRandom random = new SecureRandom();

    // Time-dependent state
    private static final AtomicLong epoch = new AtomicLong(System.nanoTime());
    private static final AtomicLong sequence = new AtomicLong(0);

    // One-time tokens (kullanildiktan sonra invalid)
    private static final ConcurrentHashMap<Long, Long> usedTokens = new ConcurrentHashMap<>();

    // Session state (her restart'ta farkli)
    private static final long SESSION_ID = generateSessionId();
    private static final byte[] SESSION_KEY = generateSessionKey();

    private AntiReplay() {}

    /**
     * Benzersiz session ID olustur
     */
    private static long generateSessionId() {
        return System.nanoTime() ^ Runtime.getRuntime().freeMemory() ^
               Thread.currentThread().getId() ^ random.nextLong();
    }

    /**
     * Session key olustur
     */
    private static byte[] generateSessionKey() {
        byte[] key = new byte[32];
        random.nextBytes(key);

        // Time-based entropy
        long time = System.nanoTime();
        for (int i = 0; i < 8; i++) {
            key[i] ^= (byte) (time >> (i * 8));
        }

        // Memory-based entropy
        long mem = Runtime.getRuntime().freeMemory();
        for (int i = 8; i < 16; i++) {
            key[i] ^= (byte) (mem >> ((i - 8) * 8));
        }

        return key;
    }

    /**
     * One-time token olustur
     */
    public static long generateToken() {
        long token = SESSION_ID ^ System.nanoTime() ^ sequence.incrementAndGet();
        token ^= random.nextLong();
        return token;
    }

    /**
     * Token dogrula ve tuket (tek kullanimlik)
     */
    public static boolean consumeToken(long token) {
        // Zaten kullanilmis mi
        if (usedTokens.containsKey(token)) {
            return false;
        }

        // Token'i kullanildi olarak isaretle
        usedTokens.put(token, System.currentTimeMillis());

        // Eski token'lari temizle (memory leak onleme)
        if (usedTokens.size() > 10000) {
            long cutoff = System.currentTimeMillis() - 300000; // 5 dakika
            usedTokens.entrySet().removeIf(e -> e.getValue() < cutoff);
        }

        return true;
    }

    /**
     * Time-dependent hash - snapshot'ta farkli sonuc verir
     */
    public static long getTimeHash() {
        long current = System.nanoTime();
        long delta = current - epoch.get();

        // Delta + sequence + session
        return delta ^ sequence.get() ^ SESSION_ID;
    }

    /**
     * Session-bound deger olustur
     */
    public static int sessionBound(int value) {
        // Deger session'a bagimli
        return value ^ (int) (SESSION_ID & 0xFFFFFFFF) ^ (int) sequence.get();
    }

    /**
     * Session-bound deger coz
     */
    public static int sessionUnbound(int encoded) {
        return encoded ^ (int) (SESSION_ID & 0xFFFFFFFF) ^ (int) sequence.get();
    }

    /**
     * Snapshot tespiti
     * Eger snapshot restore edilmisse, epoch ve sequence uyumsuz olur
     */
    public static boolean detectSnapshot() {
        long currentTime = System.nanoTime();
        long storedEpoch = epoch.get();

        // Zaman geriye gitmis (snapshot restore)
        if (currentTime < storedEpoch) {
            return true;
        }

        // Cok buyuk zaman atlama (freeze sonrasi restore)
        long delta = currentTime - storedEpoch;
        long expectedDelta = sequence.get() * 1_000_000; // ~1ms per sequence

        // %1000 sapma = suphe
        if (expectedDelta > 0 && delta > expectedDelta * 10) {
            return true;
        }

        return false;
    }

    /**
     * Anti-replay check
     */
    public static void check() {
        if (detectSnapshot()) {
            // Soft fail - sessiz degradasyon
            CanaryLogic.trigger("snapshot_detected");
        }

        // Epoch guncelle
        epoch.set(System.nanoTime());
        sequence.incrementAndGet();
    }

    public static long getSessionId() {
        return SESSION_ID;
    }

    public static byte[] getSessionKey() {
        return SESSION_KEY.clone();
    }
}
