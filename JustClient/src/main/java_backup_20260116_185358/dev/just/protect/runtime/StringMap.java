package dev.just.protect.runtime;

import java.util.HashMap;
import java.util.Map;

public final class StringMap {

    private static final Map<Integer, String> MAP = new HashMap<>();

    static {
        // hash -> gerçek string (runtime'da)
        MAP.put(hash("Kristal"), "Kristal");
        MAP.put(hash("Vagon"), "Vagon");
        MAP.put(hash("Çapa"), "Çapa");
        MAP.put(hash("Obsidyen"), "Obsidyen");
        MAP.put(hash("İptal"), "İptal");
        MAP.put(hash("Grim"), "Grim");
    }

    private StringMap() {}

    public static String get(int key) {
        return MAP.get(key);
    }

    public static int hash(String s) {
        int h = 0;
        for (char c : s.toCharArray()) {
            h = (h * 31) ^ c;
        }
        return h;
    }
}
