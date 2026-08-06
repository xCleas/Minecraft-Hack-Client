package dev.just.protect.runtime;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * String Obfuscation - AES-256 + Multi-layer koruma
 * Decompile edildiginde sifrelenmis veri gorunur
 */
public final class I0O1l0I1 {

    private static final int KEY = 0x4A; // XOR key
    private static final int KEY2 = 0x5F; // Secondary XOR key
    private static final byte[] SALT = {0x4A, 0x55, 0x53, 0x54}; // "JUST"

    // AES-128 Key (16 bytes) - obfuscated
    private static final byte[] AES_KEY = {
        (byte)(0x45 ^ 0x12), (byte)(0x78 ^ 0x12), (byte)(0x6F ^ 0x12), (byte)(0x6E ^ 0x12),
        (byte)(0x43 ^ 0x12), (byte)(0x6C ^ 0x12), (byte)(0x69 ^ 0x12), (byte)(0x65 ^ 0x12),
        (byte)(0x6E ^ 0x12), (byte)(0x74 ^ 0x12), (byte)(0x4B ^ 0x12), (byte)(0x65 ^ 0x12),
        (byte)(0x79 ^ 0x12), (byte)(0x31 ^ 0x12), (byte)(0x32 ^ 0x12), (byte)(0x33 ^ 0x12)
    };

    // IV (16 bytes) - obfuscated
    private static final byte[] AES_IV = {
        (byte)(0x52 ^ 0x23), (byte)(0x61 ^ 0x23), (byte)(0x6E ^ 0x23), (byte)(0x64 ^ 0x23),
        (byte)(0x6F ^ 0x23), (byte)(0x6D ^ 0x23), (byte)(0x49 ^ 0x23), (byte)(0x56 ^ 0x23),
        (byte)(0x56 ^ 0x23), (byte)(0x65 ^ 0x23), (byte)(0x63 ^ 0x23), (byte)(0x74 ^ 0x23),
        (byte)(0x6F ^ 0x23), (byte)(0x72 ^ 0x23), (byte)(0x31 ^ 0x23), (byte)(0x36 ^ 0x23)
    };

    // String cache - performance için
    private static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    // Lazy initialized cipher components
    private static volatile SecretKeySpec keySpec;
    private static volatile IvParameterSpec ivSpec;
    private static volatile boolean initialized = false;

    private I0O1l0I1() {}

    // ==========================================
    // AES DECRYPTION (En güçlü koruma)
    // ==========================================

    /**
     * AES key'i deobfuscate et
     */
    private static byte[] getKey() {
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++) {
            key[i] = (byte)(AES_KEY[i] ^ 0x12);
        }
        return key;
    }

    /**
     * AES IV'i deobfuscate et
     */
    private static byte[] getIV() {
        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++) {
            iv[i] = (byte)(AES_IV[i] ^ 0x23);
        }
        return iv;
    }

    /**
     * Lazy initialization
     */
    private static void ensureInitialized() {
        if (!initialized) {
            synchronized (I0O1l0I1.class) {
                if (!initialized) {
                    keySpec = new SecretKeySpec(getKey(), "AES");
                    ivSpec = new IvParameterSpec(getIV());
                    initialized = true;
                }
            }
        }
    }

    /**
     * AES-128-CBC decrypt - EN GÜÇLÜ
     * Decompiler'da: I0O1l0I1.a("Base64EncodedAESCiphertext") şeklinde görünür
     */
    public static String a(String encrypted) {
        // Cache kontrolü
        String cached = cache.get(encrypted);
        if (cached != null) return cached;

        try {
            ensureInitialized();

            // Base64 decode
            byte[] cipherText = Base64.getDecoder().decode(encrypted);

            // AES decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(cipherText);

            String result = new String(decrypted, StandardCharsets.UTF_8);
            cache.put(encrypted, result);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * AES + XOR hybrid decrypt - ULTRA GÜÇLÜ
     * Önce XOR sonra AES
     */
    public static String ax(String encrypted) {
        String cached = cache.get("ax:" + encrypted);
        if (cached != null) return cached;

        try {
            ensureInitialized();

            // Base64 decode
            byte[] data = Base64.getDecoder().decode(encrypted);

            // XOR layer (before AES)
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte)(data[i] ^ KEY ^ (i % 7));
            }

            // AES decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(data);

            String result = new String(decrypted, StandardCharsets.UTF_8);
            cache.put("ax:" + encrypted, result);
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * AES encryption helper (build time için)
     */
    public static String encryptAES(String plain) {
        try {
            ensureInitialized();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * AES + XOR hybrid encryption helper
     */
    public static String encryptAESX(String plain) {
        try {
            ensureInitialized();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            // XOR layer
            for (int i = 0; i < encrypted.length; i++) {
                encrypted[i] = (byte)(encrypted[i] ^ KEY ^ (i % 7));
            }

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return "";
        }
    }

    // ==========================================
    // BASE64 DECODE METHODS (Decompiler'da Base64 gorunur)
    // ==========================================

    /**
     * Base64 encoded string decode
     * Decompiler'da: I0O1l0I1.b("SGVsbG8=") seklinde gorunur
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
     * Decompiler'da: I0O1l0I1.bx("U0dWc2JHOD0=") seklinde gorunur
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
     * Decompiler'da: I0O1l0I1.bxx("VTBkV2MyeEhPRDA9") seklinde gorunur
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
