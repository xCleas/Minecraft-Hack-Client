package dev.just.protect;

/**
 * String encryption utility - Runtime decryption
 * Stringleri XOR + Base64 ile sifreler
 */
public class StringProtect {

    private static final int[] K = {0x4A, 0x55, 0x53, 0x54, 0x43, 0x4C, 0x49, 0x45, 0x4E, 0x54};

    /**
     * Sifreli stringi coz
     * @param e encrypted string (hex format)
     * @return decrypted string
     */
    public static String d(String e) {
        if (e == null || e.isEmpty()) return e;
        try {
            byte[] b = new byte[e.length() / 2];
            for (int i = 0; i < b.length; i++) {
                b[i] = (byte) Integer.parseInt(e.substring(i * 2, i * 2 + 2), 16);
            }
            byte[] r = new byte[b.length];
            for (int i = 0; i < b.length; i++) {
                r[i] = (byte) (b[i] ^ K[i % K.length]);
            }
            return new String(r, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return e;
        }
    }

    /**
     * Stringi sifrele (build time icin)
     * Bu metodu kullanarak stringleri sifreleyebilirsin
     */
    public static String e(String s) {
        if (s == null || s.isEmpty()) return s;
        byte[] b = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            int v = (b[i] ^ K[i % K.length]) & 0xFF;
            sb.append(String.format("%02x", v));
        }
        return sb.toString();
    }

    // Test
    public static void main(String[] args) {
        String[] tests = {"Kill Aura", "Speed", "Fly", "NoFall", "Jesus", "Velocity"};
        System.out.println("// Encrypted strings:");
        for (String t : tests) {
            System.out.println("// \"" + t + "\" -> \"" + e(t) + "\"");
        }
    }
}
