package dev.just.protect.runtime;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Ultra String Encryption
 * Multi-layer encryption + anti-dump protection
 */
public final class StringGuard {

    private static final Map<Integer, String> cache = new HashMap<>();
    private static byte[] masterKey;
    private static boolean initialized = false;

    // Anti-dump: key parcalari artik statik array degil, runtime'da hesaplanacak
    private static final long SEED = 0x4A757374436C6965L; 

    private StringGuard() {}

    /**
     * Baslatma - runtime'da key olustur
     */
    public static void init() {
        if (initialized) return;

        // Key'i runtime'da matematiksel olarak olustur (Statik array acigini kapatir)
        masterKey = new byte[16];
        long tempSeed = SEED;
        for (int i = 0; i < 16; i++) {
            masterKey[i] = (byte) ((tempSeed ^ (i * 0x5A)) & 0xFF);
            tempSeed = Long.rotateLeft(tempSeed, 5);
        }

        // Decompiler'in "if(false)" olarak silemeyecegi ama sabit sonuc veren entropy
        int entropy1 = "JustProtect".length() * 31;
        int entropy2 = StringGuard.class.getName().hashCode();

        // Key'i mutate et (Statik analizi zorlastirir ama her zaman ayni key'i uretir)
        for (int i = 0; i < masterKey.length; i++) {
            masterKey[i] ^= (byte) ((entropy1 >> (i % 4)) & 0xFF);
            masterKey[i] ^= (byte) ((entropy2 >> (i % 2)) & 0xFF);
        }

        initialized = true;
    }

    /**
     * Layer 1: XOR sifrele
     */
    private static byte[] xorEncrypt(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
            result[i] = (byte) (result[i] ^ (i * 7));
        }
        return result;
    }

    /**
     * Layer 2: Byte shuffle
     */
    private static byte[] shuffle(byte[] data) {
        byte[] result = new byte[data.length];
        int half = data.length / 2;

        for (int i = 0; i < data.length; i++) {
            if (i % 2 == 0) {
                result[i / 2] = data[i];
            } else {
                result[half + i / 2] = data[i];
            }
        }
        return result;
    }

    /**
     * Layer 2: Byte unshuffle
     */
    private static byte[] unshuffle(byte[] data) {
        byte[] result = new byte[data.length];
        int half = data.length / 2;

        for (int i = 0; i < data.length; i++) {
            if (i < half) {
                result[i * 2] = data[i];
            } else {
                result[(i - half) * 2 + 1] = data[i];
            }
        }
        return result;
    }

    /**
     * Layer 3: AES sifrele
     */
    private static byte[] aesEncrypt(byte[] data, byte[] key) {
        try {
            // IV olustur
            byte[] iv = new byte[16];
            System.arraycopy(key, 0, iv, 0, Math.min(key.length, 16));
            for (int i = 0; i < iv.length; i++) {
                iv[i] ^= 0x5A;
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * Layer 3: AES coz
     */
    private static byte[] aesDecrypt(byte[] data, byte[] key) {
        try {
            byte[] iv = new byte[16];
            System.arraycopy(key, 0, iv, 0, Math.min(key.length, 16));
            for (int i = 0; i < iv.length; i++) {
                iv[i] ^= 0x5A;
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return data;
        }
    }

    /**
     * String sifrele (compile-time icin)
     */
    public static String encrypt(String plain) {
        init();
        try {
            byte[] data = plain.getBytes(StandardCharsets.UTF_8);

            // Layer 1: XOR
            data = xorEncrypt(data, masterKey);

            // Layer 2: Shuffle
            data = shuffle(data);

            // Layer 3: AES
            data = aesEncrypt(data, masterKey);

            return Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            return plain;
        }
    }

    /**
     * String coz (runtime)
     * @param id - string id'si
     * @param enc - encrypted string
     */
    public static String g(int id, String enc) {
        // Anti-debug check
        if (AntiDebug.isDebuggerDetected() || VMCheck.isTamperDetected()) {
            return "";
        }

        // Cache kontrolu
        String cached = cache.get(id);
        if (cached != null) {
            return cached;
        }

        init();

        try {
            byte[] data = Base64.getDecoder().decode(enc);

            // Layer 3: AES coz
            data = aesDecrypt(data, masterKey);

            // Layer 2: Unshuffle
            data = unshuffle(data);

            // Layer 1: XOR coz
            data = xorEncrypt(data, masterKey);

            String result = new String(data, StandardCharsets.UTF_8);

            // Cache'e ekle
            cache.put(id, result);

            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Tek parametre versiyonu
     */
    public static String g(String enc) {
        return g(enc.hashCode(), enc);
    }

    /**
     * Numeric string decode (sayi olarak sakla)
     */
    public static String n(long... values) {
        if (AntiDebug.isDebuggerDetected()) return "";

        StringBuilder sb = new StringBuilder();
        for (long val : values) {
            // Her 2 byte bir karakter
            while (val > 0) {
                sb.append((char) (val & 0xFFFF));
                val >>= 16;
            }
        }
        return sb.reverse().toString();
    }

    /**
     * Cache temizle (memory dump'a karsi)
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Memory'deki stringleri kirlet
     */
    public static void scrambleMemory() {
        try {
            // Cache'deki degerleri kirlet
            for (Map.Entry<Integer, String> entry : cache.entrySet()) {
                char[] chars = entry.getValue().toCharArray();
                Arrays.fill(chars, '\0');
            }
            cache.clear();

            // GC zorla
            System.gc();
        } catch (Exception ignored) {}
    }
}