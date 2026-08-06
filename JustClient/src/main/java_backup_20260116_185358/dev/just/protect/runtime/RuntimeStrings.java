package dev.just.protect.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * Runtime String Map - Tüm stringler burada encrypted olarak saklanır
 * JAR'da anlamlı string görünmez
 */
public final class RuntimeStrings {
    private static final Map<Integer, byte[]> STRINGS = new HashMap<>(512);
    private static volatile boolean initialized = false;
    private static final Object LOCK = new Object();

    // Fake constants
    @SuppressWarnings("unused")
    private static final int SERVER_SYNC_INTERVAL = 30000;
    @SuppressWarnings("unused")
    private static final String HWID_SALT = "x9Kp2mNv";

    private RuntimeStrings() {
        throw new AssertionError();
    }

    static {
        init();
    }

    /**
     * String verilerini yükle
     */
    private static void init() {
        if (initialized) return;
        synchronized (LOCK) {
            if (initialized) return;

            // === TÜRKÇE MODÜL İSİMLERİ ===
            // Combat
            put("Kristal", -0x1A2B3C01);
            put("Topuzlu Oyuncu", -0x1A2B3C02);
            put("Ölüm Odağı", -0x1A2B3C03);
            put("Otomatik", -0x1A2B3C04);
            put("Tek Vuruş", -0x1A2B3C05);
            put("Kritik", -0x1A2B3C06);

            // Movement
            put("Uçuş", -0x2A2B3C01);
            put("Hız", -0x2A2B3C02);
            put("Zıplama", -0x2A2B3C03);
            put("Sprint", -0x2A2B3C04);
            put("Düşme", -0x2A2B3C05);
            put("Su Yürüyüşü", -0x2A2B3C06);

            // Player
            put("Otomatik Zırh", -0x3A2B3C01);
            put("Otomatik Yeme", -0x3A2B3C02);
            put("Otomatik Totem", -0x3A2B3C03);
            put("Hızlı Kullanım", -0x3A2B3C04);
            put("Envanter", -0x3A2B3C05);

            // Render
            put("ESP", -0x4A2B3C01);
            put("İsim Etiketi", -0x4A2B3C02);
            put("Parçacık", -0x4A2B3C03);
            put("Tam Parlaklık", -0x4A2B3C04);
            put("Yakınlaştırma", -0x4A2B3C05);

            // Settings
            put("Aktif", -0x5A2B3C01);
            put("Devre Dışı", -0x5A2B3C02);
            put("Mod", -0x5A2B3C03);
            put("Hız", -0x5A2B3C04);
            put("Mesafe", -0x5A2B3C05);
            put("Gecikme", -0x5A2B3C06);

            // Mode values
            put("Normal", -0x6A2B3C01);
            put("Vanilla", -0x6A2B3C02);
            put("Bypass", -0x6A2B3C03);
            put("Strafe", -0x6A2B3C04);
            put("Matrix", -0x6A2B3C05);
            put("Vulcan", -0x6A2B3C06);
            put("Grim", -0x6A2B3C07);
            put("Verus", -0x6A2B3C08);
            put("NCP", -0x6A2B3C09);
            put("AAC", -0x6A2B3C0A);

            // UI
            put("Oyuncular", -0x7A2B3C01);
            put("Arkadaşlar", -0x7A2B3C02);
            put("Düşmanlar", -0x7A2B3C03);
            put("Hayvanlar", -0x7A2B3C04);
            put("Canavarlar", -0x7A2B3C05);

            // Misc strings
            put("Beni", -0x8A2B3C01);
            put("Hedef", -0x8A2B3C02);
            put("Yarıçap", -0x8A2B3C03);
            put("Boyut", -0x8A2B3C04);
            put("Renk", -0x8A2B3C05);

            initialized = true;
        }
    }

    /**
     * String'i encrypted olarak kaydet
     */
    private static void put(String value, int hash) {
        STRINGS.put(hash, StringEncrypt.encrypt(value));
    }

    /**
     * Hash'ten encrypted data al
     */
    public static byte[] getData(int hash) {
        if (!initialized) init();
        return STRINGS.get(hash);
    }

    /**
     * Hash'ten string al (convenience method)
     */
    public static String str(int hash) {
        return StringEncrypt.get(hash);
    }

    // === HASH CONSTANTS ===
    // Combat
    public static final int KRISTAL = -0x1A2B3C01;
    public static final int TOPUZLU_OYUNCU = -0x1A2B3C02;
    public static final int OLUM_ODAGI = -0x1A2B3C03;
    public static final int OTOMATIK = -0x1A2B3C04;
    public static final int TEK_VURUS = -0x1A2B3C05;
    public static final int KRITIK = -0x1A2B3C06;

    // Movement
    public static final int UCUS = -0x2A2B3C01;
    public static final int HIZ = -0x2A2B3C02;
    public static final int ZIPLAMA = -0x2A2B3C03;
    public static final int SPRINT = -0x2A2B3C04;
    public static final int DUSME = -0x2A2B3C05;
    public static final int SU_YURUYUSU = -0x2A2B3C06;

    // Modes
    public static final int NORMAL = -0x6A2B3C01;
    public static final int VANILLA = -0x6A2B3C02;
    public static final int BYPASS = -0x6A2B3C03;
    public static final int STRAFE = -0x6A2B3C04;
    public static final int MATRIX = -0x6A2B3C05;
    public static final int VULCAN = -0x6A2B3C06;
    public static final int GRIM = -0x6A2B3C07;
    public static final int VERUS = -0x6A2B3C08;

    // Settings
    public static final int AKTIF = -0x5A2B3C01;
    public static final int DEVRE_DISI = -0x5A2B3C02;
}
