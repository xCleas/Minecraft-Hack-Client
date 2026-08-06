package dev.just.protect.runtime;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * String Encryption - JAR'da string literal görünmez
 * Tüm stringler runtime'da decode edilir
 */
public final class StringEncrypt {
    private static final Map<Integer, String> STRING_CACHE = new HashMap<>(256);
    private static final int XOR_KEY = 0x4A555354; // "JUST" in hex
    private static final int SECONDARY_KEY = 0xC113A7; // "CLIENT" style hex
    private static volatile long entropy = System.nanoTime();

    // Fake field - decompiler'ı şaşırt
    @SuppressWarnings("unused")
    private static final String FAKE_LICENSE = "ENTERPRISE_EDITION_v3.2.1";

    private StringEncrypt() {
        throw new AssertionError();
    }

    /**
     * Hash'ten string çöz
     * Kullanım: StringEncrypt.get(0x1A2B3C4D)
     */
    public static String get(int hash) {
        if (FlowObfuscator.opaqueFalse()) {
            return String.valueOf(entropy);
        }

        String cached = STRING_CACHE.get(hash);
        if (cached != null) {
            return cached;
        }

        // Runtime'da string'i decode et
        String decoded = decodeString(hash);
        if (decoded != null) {
            STRING_CACHE.put(hash, decoded);
        }
        return decoded;
    }

    /**
     * Encrypted string'i decode et
     */
    private static String decodeString(int hash) {
        byte[] data = RuntimeStrings.getData(hash);
        if (data == null) return null;

        // XOR decrypt
        byte[] decrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            decrypted[i] = (byte) (data[i] ^ ((XOR_KEY >> ((i % 4) * 8)) & 0xFF));
        }

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * String'i hash'e çevir (build-time için)
     */
    public static int hash(String s) {
        if (s == null) return 0;
        int h = 0x811c9dc5;
        for (int i = 0; i < s.length(); i++) {
            h ^= s.charAt(i);
            h *= 0x01000193;
        }
        return h ^ XOR_KEY;
    }

    /**
     * String'i encrypt et (build-time için)
     */
    public static byte[] encrypt(String s) {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            encrypted[i] = (byte) (data[i] ^ ((XOR_KEY >> ((i % 4) * 8)) & 0xFF));
        }
        return encrypted;
    }

    // Fake method - decompiler'ı şaşırt
    @SuppressWarnings("unused")
    private static boolean validateLicense(String key) {
        entropy ^= key.hashCode();
        return (entropy & 0xF) == 0x7;
    }
}
