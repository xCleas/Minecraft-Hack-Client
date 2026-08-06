package dev.just.protect.runtime;

public final class SemanticNoise {
    private SemanticNoise() {}

    public static int O(int i) { return 0; }
    public static int l(int i) { return i; }
    public static float o(float f) { return f; }
    public static int inject(int value) { return value; }
    public static String s(String input) { return input; }
    public static boolean p1() { return true; }
    public static boolean p2() { return true; }
    public static boolean p3() { return true; }
    public static void deadCode1() {}
    public static void deadCode2() {}
    public static void deadCode3() {}
    public static int confuse(int input) { return input; }
    public static int alias(int real) { return real; }
}
