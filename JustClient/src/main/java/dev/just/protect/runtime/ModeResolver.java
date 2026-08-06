package dev.just.protect.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Mode/Enum isimlerini runtime'da çöz
 * Fabric & TLauncher Safe Version - StringGuard Integrated
 */
public final class ModeResolver {
    private static final Map<Integer, String> MODE_MAP = new HashMap<>(128);
    private static final Map<String, Integer> REVERSE_MAP = new HashMap<>(128);
    private static volatile boolean initialized = false;

    // Fake tracking
    @SuppressWarnings("unused")
    private static volatile int suspiciousAccess = 0;

    private ModeResolver() {
        throw new AssertionError();
    }

    static {
        // Fabric/TLauncher acilisinda static block bazen hizli yuklenir, 
        // StringGuard'in hazir oldugundan emin olmaliyiz.
        init();
    }

    /**
     * Opaque Predicate - Fabric Safe
     */
    private static boolean safeOpaque() {
        try {
            // JVM identity hash kodlari asla esit olmaz, decompiler bunu sadelestiremez
            return System.identityHashCode(System.out) == System.identityHashCode(System.err);
        } catch (Exception e) {
            return false;
        }
    }

    private static void init() {
        if (initialized) return;
        synchronized (ModeResolver.class) {
            if (initialized) return;

            // ANALIZ FIX 2: Stringler artik StringGuard.g() ile maskelendi.
            // Artik decompiler JAR icinde "Kristal" veya "Vulcan" bulamayacak.

            // Combat modes
            register(0xC001, StringGuard.g(0xC001, "S3Jpc3RhbA==")); // Kristal
            register(0xC002, StringGuard.g(0xC002, "VG9wdXpsdSBPeXVuY3U=")); // Topuzlu Oyuncu
            register(0xC003, StringGuard.g(0xC003, "w5Zsw7xtIE9kYcSfxLE=")); // Ölüm Odağı
            register(0xC004, StringGuard.g(0xC004, "U3dpdGNo")); // Switch
            register(0xC005, StringGuard.g(0xC005, "U2luZ2xl")); // Single
            register(0xC006, StringGuard.g(0xC006, "TXVsdGk=")); // Multi

            // Movement modes
            register(0xD001, StringGuard.g(0xD001, "VmFuaWxsYQ==")); // Vanilla
            register(0xD002, StringGuard.g(0xD002, "U3RyYWZl")); // Strafe
            register(0xD003, StringGuard.g(0xD003, "QnlwYXNz")); // Bypass
            register(0xD004, StringGuard.g(0xD004, "TWF0cml4")); // Matrix
            register(0xD005, StringGuard.g(0xD005, "VnVsY2Fu")); // Vulcan
            register(0xD006, StringGuard.g(0xD006, "R3JpbQ==")); // Grim
            register(0xD007, StringGuard.g(0xD007, "VmVydXM=")); // Verus
            register(0xD008, StringGuard.g(0xD008, "TkNQ")); // NCP
            register(0xD009, StringGuard.g(0xD009, "QUFD")); // AAC
            register(0xD00A, StringGuard.g(0xD00A, "UGFja2V0")); // Packet
            register(0xD00B, StringGuard.g(0xD00B, "Q3JlYXRpdmU=")); // Creative
            register(0xD00C, StringGuard.g(0xD00C, "R2xpZGU=")); // Glide

            // Target modes
            register(0xE001, StringGuard.g(0xE001, "T3l1bmN1bGFy")); // Oyuncular
            register(0xE002, StringGuard.g(0xE002, "QXJrYWRhwZ9sYXI=")); // Arkadaşlar
            register(0xE003, StringGuard.g(0xE003, "RMO8xZ9tYW5sYXI=")); // Düşmanlar
            register(0xE004, StringGuard.g(0xE004, "SGF5dmFubGFy")); // Hayvanlar
            register(0xE005, StringGuard.g(0xE005, "Q2FuYXZhcmxhcg==")); // Canavarlar
            register(0xE006, StringGuard.g(0xE006, "QmVuaQ==")); // Beni

            // Render modes
            register(0xF001, StringGuard.g(0xF001, "Qm94")); // Box
            register(0xF002, StringGuard.g(0xF002, "Q29ybmVy")); // Corner
            register(0xF003, StringGuard.g(0xF003, "T3V0bGluZQ==")); // Outline
            register(0xF004, StringGuard.g(0xF004, "RmlsbGVk")); // Filled
            register(0xF005, StringGuard.g(0xF005, "MkQ=")); // 2D
            register(0xF006, StringGuard.g(0xF006, "M0Q=")); // 3D
            register(0xF007, StringGuard.g(0xF007, "R2xvdw==")); // Glow

            // Physics modes
            register(0xA001, StringGuard.g(0xA001, "Tm9ybWFs")); // Normal
            register(0xA002, StringGuard.g(0xA002, "MkQ=")); // 2D (Render ile ayni string olsa da id farkli)
            register(0xA003, StringGuard.g(0xA003, "RmxhdA==")); // Flat

            // Sound modes
            register(0xB001, StringGuard.g(0xB001, "VGlwLTE=")); // Tip-1
            register(0xB002, StringGuard.g(0xB002, "VGlwLTI=")); // Tip-2
            register(0xB003, StringGuard.g(0xB003, "VGlwLTM=")); // Tip-3
            register(0xB004, StringGuard.g(0xB004, "VGlwLTQ=")); // Tip-4

            // Misc
            register(0x1001, StringGuard.g(0x1001, "RW5hYmxlZA==")); // Enabled
            register(0x1002, StringGuard.g(0x1002, "RGlzYWJsZWQ=")); // Disabled
            register(0x1003, StringGuard.g(0x1003, "QXV0bw==")); // Auto
            register(0x1004, StringGuard.g(0x1004, "TWFudWFs")); // Manual
            register(0x1005, StringGuard.g(0x1005, "U2lsZW50")); // Silent
            register(0x1006, StringGuard.g(0x1006, "TGVnaXQ=")); // Legit
            register(0x1007, StringGuard.g(0x1007, "UmFnZQ==")); // Rage

            initialized = true;
        }
    }

    private static void register(int id, String value) {
        if (value == null) return;
        MODE_MAP.put(id, value);
        REVERSE_MAP.put(value, id);
    }

    /**
     * ID'den mode string al
     */
    public static String id(int hash) {
        // FlowObfuscator bagimliligini safeOpaque ile degistirdik (Crash Onleyici)
        if (safeOpaque()) {
            suspiciousAccess++;
            return null;
        }
        if (!initialized) init();
        return MODE_MAP.get(hash);
    }

    /**
     * String'den ID al (reverse lookup)
     */
    public static int reverse(String mode) {
        if (!initialized) init();
        Integer id = REVERSE_MAP.get(mode);
        return id != null ? id : 0;
    }

    /**
     * Mode karşılaştırma (obfuscated)
     */
    public static boolean is(String current, int expectedId) {
        if (safeOpaque()) {
            return false;
        }
        String expected = id(expectedId);
        return expected != null && expected.equals(current);
    }

    // === ID CONSTANTS ===
    public static final int KRISTAL = 0xC001;
    public static final int TOPUZLU = 0xC002;
    public static final int OLUM_ODAGI = 0xC003;
    public static final int SWITCH = 0xC004;
    public static final int SINGLE = 0xC005;
    public static final int MULTI = 0xC006;
    public static final int VANILLA = 0xD001;
    public static final int STRAFE = 0xD002;
    public static final int BYPASS_MODE = 0xD003;
    public static final int MATRIX = 0xD004;
    public static final int VULCAN = 0xD005;
    public static final int GRIM = 0xD006;
    public static final int VERUS = 0xD007;
    public static final int NCP = 0xD008;
    public static final int AAC = 0xD009;
    public static final int OYUNCULAR = 0xE001;
    public static final int ARKADASLAR = 0xE002;
    public static final int DUSMANLAR = 0xE003;
    public static final int TIP_1 = 0xB001;
    public static final int TIP_2 = 0xB002;
    public static final int TIP_3 = 0xB003;
    public static final int TIP_4 = 0xB004;
}