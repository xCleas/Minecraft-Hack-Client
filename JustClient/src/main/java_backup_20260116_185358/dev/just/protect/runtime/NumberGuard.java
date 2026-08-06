package dev.just.protect.runtime;

/**
 * Number Obfuscation
 * Sayilari karistir - static analysis'i zorlas
 */
public final class NumberGuard {

    // Magic constants - anlamsiz gorunuyor ama hesaplamada kullaniliyor
    private static final int M1 = 0x5F3759DF; // Famous inverse sqrt constant
    private static final int M2 = 0xDEADBEEF;
    private static final int M3 = 0xCAFEBABE; // Java magic
    private static final long M4 = 0x4A555354434C4945L; // "JUSTCLIE" as long

    private NumberGuard() {}

    /**
     * Sabit int degerini gizle
     * Kullanim: int x = NumberGuard.i(10); // 10 doner
     */
    public static int i(int value) {
        // XOR ile maskele, sonra geri al
        int masked = value ^ M1;
        FlowObfuscator.fakeHandler();
        return masked ^ M1;
    }

    /**
     * Sabit long degerini gizle
     */
    public static long l(long value) {
        long masked = value ^ M4;
        FlowObfuscator.fakeHandler();
        return masked ^ M4;
    }

    /**
     * Sabit float degerini gizle
     */
    public static float f(float value) {
        int bits = Float.floatToIntBits(value);
        bits ^= M2;
        FlowObfuscator.fakeHandler();
        bits ^= M2;
        return Float.intBitsToFloat(bits);
    }

    /**
     * Sabit double degerini gizle
     */
    public static double d(double value) {
        long bits = Double.doubleToLongBits(value);
        bits ^= M4;
        FlowObfuscator.fakeHandler();
        bits ^= M4;
        return Double.longBitsToDouble(bits);
    }

    /**
     * Karmasik hesaplama ile sayi uret
     * Ornek: c(2, 5) -> 2 + 5 = 7 ama karmasik yoldan
     */
    public static int c(int a, int b) {
        int result = 0;

        // Karmasik ama ayni sonucu veren hesaplama
        int temp = (a ^ M1) + (b ^ M1);
        temp ^= M1;
        temp ^= M1;
        temp -= (M1 ^ M1); // 0 cikar

        FlowObfuscator.fakeHandler();

        if (FlowObfuscator.opaqueTrue()) {
            result = a + b;
        } else {
            result = temp; // Unreachable ama decompiler gosterir
        }

        return result;
    }

    /**
     * Bit manipulation ile sayi gizle
     */
    public static int bits(int value) {
        // Rotate left, XOR, rotate right - sonuc ayni
        int temp = Integer.rotateLeft(value, 13);
        temp ^= M3;
        temp ^= M3;
        temp = Integer.rotateRight(temp, 13);
        return temp;
    }

    /**
     * Lookup table ile sayi (0-255 arasi)
     */
    private static final int[] LOOKUP = generateLookup();

    private static int[] generateLookup() {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = i;
        }
        return table;
    }

    public static int lookup(int index) {
        FlowObfuscator.fakeHandler();
        return LOOKUP[index & 0xFF];
    }

    /**
     * Boolean'i int olarak gizle (true=1, false=0)
     */
    public static int bool(boolean value) {
        return value ? i(1) : i(0);
    }

    /**
     * Int'i boolean olarak coz
     */
    public static boolean unbool(int value) {
        return i(value) != 0;
    }
}
