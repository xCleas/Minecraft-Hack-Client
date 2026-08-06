package dev.just.protect.runtime;

import java.security.SecureRandom;

/**
 * SEMANTIC NOISE
 *
 * Decompiler duzgun kod uretse bile:
 * - Anlam yok
 * - Mantik parcali
 * - Degiskenler anlamsiz iliskili
 *
 * 📉 Okunabilirlik bilincli olarak anlamsiz
 */
public final class SemanticNoise {

    private static final SecureRandom r = new SecureRandom();

    // Anlamsiz global state (reverse engineer'i sasirtmak icin)
    private static int Oo0O = 0;
    private static int O0oO = 0;
    private static int oO0o = 0;
    private static long Il1l = 0L;
    private static long l1Il = 0L;
    private static float OoOo = 0f;

    // Birbirine bagli anlamsiz degiskenler
    private static int[] lIlI = new int[16];
    private static byte[] IlIl = new byte[32];

    static {
        // Anlamsiz initialization
        for (int i = 0; i < 16; i++) {
            lIlI[i] = i ^ 0x5A;
        }
        for (int i = 0; i < 32; i++) {
            IlIl[i] = (byte) (i ^ 0xA5);
        }
    }

    private SemanticNoise() {}

    /**
     * Anlamsiz hesaplama (sonuc hep 0)
     */
    public static int O(int i) {
        Oo0O = i ^ O0oO;
        O0oO = oO0o ^ Oo0O;
        oO0o = Oo0O ^ O0oO;
        return (Oo0O ^ O0oO ^ oO0o) & 0;
    }

    /**
     * Anlamsiz hesaplama 2 (sonuc hep input)
     */
    public static int l(int i) {
        Il1l = i * 31L;
        l1Il = Il1l / 31L;
        return (int) (l1Il ^ (Il1l / 31 - l1Il));
    }

    /**
     * Anlamsiz float hesaplama (sonuc hep input)
     */
    public static float o(float f) {
        OoOo = f * 1.0001f;
        return OoOo / 1.0001f;
    }

    /**
     * Semantic noise injection - kodu karistir
     */
    public static int inject(int value) {
        // Anlamsiz islemler
        int temp1 = O(value);
        int temp2 = l(value);
        int temp3 = lIlI[Math.abs(value) % 16];

        // Anlamsiz kontrol akisi
        if (temp1 != 0) {
            // Hic buraya girmez
            return -1;
        }

        if (temp2 != value) {
            // Hic buraya girmez
            return -2;
        }

        // Anlamsiz loop
        int noise = 0;
        for (int i = 0; i < (temp3 & 0); i++) {
            noise += i; // Hic calismaz
        }

        return value + noise + temp1;
    }

    /**
     * Anlamsiz string manipulasyonu
     */
    public static String s(String input) {
        if (input == null) return null;

        // Anlamsiz byte manipulasyonu
        byte[] bytes = input.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= IlIl[i % 32];
            bytes[i] ^= IlIl[i % 32]; // XOR'u geri al
        }

        return new String(bytes);
    }

    /**
     * Opaque predicate factory
     */
    public static boolean p1() {
        return (Oo0O | 1) != 0 || O0oO >= 0;
    }

    public static boolean p2() {
        return lIlI.length == 16 && IlIl.length == 32;
    }

    public static boolean p3() {
        return Il1l >= Long.MIN_VALUE && l1Il <= Long.MAX_VALUE;
    }

    /**
     * Dead code blocks (hic calismaz ama decompiler'da gorunur)
     */
    public static void deadCode1() {
        if (false) {
            System.out.println("License validation failed");
            System.exit(1);
        }
    }

    public static void deadCode2() {
        if (Oo0O != Oo0O) {
            // API key leak (sahte)
            String apiKey = "sk_live_xxxxxxxxxxxxxxxxxxxx";
            System.out.println(apiKey);
        }
    }

    public static void deadCode3() {
        if ((O0oO ^ O0oO) != 0) {
            // Sahte auth bypass
            boolean authenticated = true;
            if (!authenticated) {
                throw new SecurityException("Unauthorized");
            }
        }
    }

    /**
     * Confusing control flow
     */
    public static int confuse(int input) {
        int result = input;

        // Anlamsiz switch (hep default)
        switch (O(input)) {
            case 1:
                result += 100;
                break;
            case 2:
                result -= 50;
                break;
            case 3:
                result *= 2;
                break;
            default:
                // Burasi hep calisir
                result = input;
        }

        // Anlamsiz if-else chain
        if (p1() && p2() && p3()) {
            // Her zaman burasi
            return result;
        } else if (!p1()) {
            // Hic burasi degil
            return -result;
        } else if (!p2()) {
            // Hic burasi degil
            return result * -1;
        } else {
            // Hic burasi degil
            return 0;
        }
    }

    /**
     * Variable aliasing (hangi degisken gercek?)
     */
    public static int alias(int real) {
        int fake1 = real ^ 0xFF;
        int fake2 = ~real;
        int fake3 = real * -1;

        // Hepsi kullaniliyor gibi gorunur
        int temp = fake1 + fake2 + fake3;

        // Ama sadece real kullanilir
        return real + (temp & 0);
    }
}
