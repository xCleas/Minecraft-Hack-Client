package dev.just.protect.runtime;

import java.util.Base64;

/**
 * Runtime string decryption for CatleanASM v4.0
 * Multi-layer XOR decryption with bit rotation
 * DO NOT MODIFY - Referenced by obfuscated bytecode
 */
public final class StringDecrypt {

    private StringDecrypt() {}

    /**
     * Legacy single-key decryption (v3.0 compatibility)
     */
    public static String d(String encoded, int key) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            char[] chars = new String(decoded).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] ^ key ^ (i % 256));
            }
            return new String(chars);
        } catch (Exception e) {
            return encoded;
        }
    }

    /**
     * CatleanASM v5.0 - Multi-layer decryption (short name for bytecode)
     */
    public static String d(String encoded, int key1, int key2) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            char[] chars = new String(decoded, "ISO-8859-1").toCharArray();

            // Reverse Layer 2: bit rotation + XOR with key2
            for (int i = 0; i < chars.length; i++) {
                int c = chars[i] & 0xFF;
                c = ((c >>> 3) | (c << 5)) & 0xFF;
                c = c ^ key2;
                chars[i] = (char) c;
            }

            // Reverse Layer 1: XOR with key1 + position
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) ((chars[i] ^ key1 ^ i) & 0xFF);
            }

            return new String(chars);
        } catch (Exception e) {
            return encoded;
        }
    }

    /**
     * Multi-layer decryption (v4.0)
     * Layer 1: Reverse bit rotation + XOR with key2
     * Layer 2: XOR with key1 + position
     */
    public static String decrypt(String encoded, int key1, int key2) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            char[] chars = new String(decoded).toCharArray();

            // Reverse Layer 2: bit rotation + XOR with key2
            for (int i = 0; i < chars.length; i++) {
                int c = chars[i];
                c = ((c >> 3) | (c << 5)) & 0xFF; // Reverse bit rotation
                c = c ^ key2;
                chars[i] = (char) c;
            }

            // Reverse Layer 1: XOR with key1 + position
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] ^ key1 ^ (i % 256));
            }

            return new String(chars);
        } catch (Exception e) {
            return encoded;
        }
    }
}
