package dev.just.protect.runtime;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * String Obfuscation - Multi-layer koruma
 * Decompile edildiginde Base64 olarak gorunur
 */
public final class Strings {

    private static final int KEY = 0x4A; // XOR key
    private static final int KEY2 = 0x5F; // Secondary XOR key
    private static final byte[] SALT = {0x4A, 0x55, 0x53, 0x54}; // "JUST"

    private Strings() {}

    // ==========================================
    // BASE64 DECODE METHODS (Decompiler'da Base64 gorunur)
    // ==========================================

    /**
     * Base64 encoded string decode
     * Decompiler'da: Strings.b("SGVsbG8=") seklinde gorunur
     */
    public static String b(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Base64 + XOR decode (daha guclu)
     * Decompiler'da: Strings.bx("U0dWc2JHOD0=") seklinde gorunur
     */
    public static String bx(String encoded) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            byte[] result = new byte[decoded.length];
            for (int i = 0; i < decoded.length; i++) {
                result[i] = (byte) (decoded[i] ^ KEY2 ^ SALT[i % SALT.length]);
            }
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Double Base64 + XOR decode (en guclu)
     * Decompiler'da: Strings.bxx("VTBkV2MyeEhPRDA9") seklinde gorunur
     */
    public static String bxx(String encoded) {
        try {
            // First Base64 decode
            byte[] first = Base64.getDecoder().decode(encoded);
            // Second Base64 decode
            byte[] second = Base64.getDecoder().decode(first);
            // XOR with rotating key
            byte[] result = new byte[second.length];
            for (int i = 0; i < second.length; i++) {
                result[i] = (byte) (second[i] ^ KEY ^ KEY2 ^ (i * 7) ^ SALT[i % SALT.length]);
            }
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    // ==========================================
    // ENCODE HELPERS (Development icin)
    // ==========================================

    /**
     * String -> Base64 encode
     */
    public static String encodeB(String plain) {
        return Base64.getEncoder().encodeToString(plain.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * String -> Base64 + XOR encode
     */
    public static String encodeBx(String plain) {
        byte[] bytes = plain.getBytes(StandardCharsets.UTF_8);
        byte[] xored = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            xored[i] = (byte) (bytes[i] ^ KEY2 ^ SALT[i % SALT.length]);
        }
        return Base64.getEncoder().encodeToString(xored);
    }

    /**
     * String -> Double Base64 + XOR encode
     */
    public static String encodeBxx(String plain) {
        byte[] bytes = plain.getBytes(StandardCharsets.UTF_8);
        byte[] xored = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            xored[i] = (byte) (bytes[i] ^ KEY ^ KEY2 ^ (i * 7) ^ SALT[i % SALT.length]);
        }
        // Double Base64
        String first = Base64.getEncoder().encodeToString(xored);
        return Base64.getEncoder().encodeToString(first.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sifreli string coz (eski XOR method)
     * @param encoded XOR ile sifreli byte dizisi
     */
    public static String d(int... encoded) {
        char[] chars = new char[encoded.length];
        for (int i = 0; i < encoded.length; i++) {
            chars[i] = (char) (encoded[i] ^ KEY ^ (i * 3));
        }
        return new String(chars);
    }

    /**
     * String sifrele (development icin) - Konsola yazdirip koda yapistir
     */
    public static String encode(String plain) {
        StringBuilder sb = new StringBuilder();
        char[] chars = plain.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int encoded = chars[i] ^ KEY ^ (i * 3);
            sb.append("0x").append(Integer.toHexString(encoded).toUpperCase());
            if (i < chars.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    // ==========================================
    // CLIENT (Base64 encoded - decompiler'da gorunur)
    // ==========================================
    public static String CLIENT_NAME() { return b("SnVzdENsaWVudA=="); }
    public static String FILES_DIR() { return b("ZmlsZXM="); }
    public static String MODULES_DIR() { return b("ZmlsZXMvbW9kdWxlcw=="); }
    public static String OTOCONFIG() { return b("T1RPQ09ORklH"); }

    // ==========================================
    // CATEGORIES
    // ==========================================
    public static String CAT_COMBAT() { return b("Q29tYmF0"); }
    public static String CAT_MOVEMENT() { return b("TW92ZW1lbnQ="); }
    public static String CAT_RENDER() { return b("UmVuZGVy"); }
    public static String CAT_MISC() { return b("TWlzYw=="); }
    public static String CAT_PLAYER() { return b("UGxheWVy"); }

    // ==========================================
    // MODULES - COMBAT
    // ==========================================
    public static String MOD_KILLAURA() { return b("S2lsbEF1cmE="); }
    public static String MOD_ATTACKAURA() { return b("QXR0YWNrQXVyYQ=="); }
    public static String MOD_VELOCITY() { return b("VmVsb2NpdHk="); }
    public static String MOD_CRITICALS() { return b("Q3JpdGljYWxz"); }
    public static String MOD_AUTOTOTEM() { return b("QXV0b1RvdGVt"); }
    public static String MOD_HITBOX() { return b("SGl0Qm94"); }

    // ==========================================
    // MODULES - MOVEMENT
    // ==========================================
    public static String MOD_SPEED() { return b("U3BlZWQ="); }
    public static String MOD_FLIGHT() { return b("RmxpZ2h0"); }
    public static String MOD_NOSLOW() { return b("Tm9TbG93"); }
    public static String MOD_SPIDER() { return b("U3BpZGVy"); }
    public static String MOD_STRAFE() { return b("U3RyYWZl"); }

    // ==========================================
    // MODULES - RENDER
    // ==========================================
    public static String MOD_ESP() { return b("RVNQ"); }
    public static String MOD_HUD() { return b("SFVE"); }
    public static String MOD_NAMETAGS() { return b("TmFtZVRhZ3M="); }
    public static String MOD_FULLBRIGHT() { return b("RnVsbEJyaWdodA=="); }
    public static String MOD_TRACERS() { return b("VHJhY2Vycw=="); }
    public static String MOD_CLICKGUI() { return b("Q2xpY2tHVUk="); }

    // ==========================================
    // MODULES - MISC
    // ==========================================
    public static String MOD_XRAY() { return b("WHJheQ=="); }
    public static String MOD_TIMER() { return b("VGltZXI="); }
    public static String MOD_DISABLER() { return b("RGlzYWJsZXI="); }
    public static String MOD_ANTIVOID() { return b("QW50aVZvaWQ="); }

    // ==========================================
    // SETTINGS
    // ==========================================
    public static String SET_ENABLED() { return b("RW5hYmxlZA=="); }
    public static String SET_MODE() { return b("TW9kZQ=="); }
    public static String SET_RANGE() { return b("UmFuZ2U="); }
    public static String SET_SPEED() { return b("U3BlZWQ="); }
    public static String SET_DELAY() { return b("RGVsYXk="); }

    // ==========================================
    // MESSAGES
    // ==========================================
    public static String MSG_ENABLED() { return b("RW5hYmxlZA=="); }
    public static String MSG_DISABLED() { return b("RGlzYWJsZWQ="); }
    public static String MSG_ERROR() { return b("RXJyb3I="); }
    public static String MSG_SUCCESS() { return b("U3VjY2Vzcw=="); }

    // ==========================================
    // URLS / PATHS (kritik - XOR + Base64)
    // ==========================================
    public static String DISCORD_URL() { return b("aHR0cHM6Ly9kaXNjb3JkLmdn"); }
    public static String CONFIG_EXT() { return b("LmNmZw=="); }
    public static String JSON_EXT() { return b("Lmpzb24="); }
}
