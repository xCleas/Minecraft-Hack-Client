package dev.just.protect.fake;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SAHTE AUTHENTICATION - Reverse engineer'lari yaniltmak icin
 * Gercek auth sistemi DEGILDIR
 */
public class FakeAuth {

    private static final String AUTH_SERVER = "auth.justclient.dev";
    private static final int AUTH_PORT = 443;
    private static final String SECRET_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

    private static String sessionToken;
    private static String hwid;
    private static long expirationTime;

    public static boolean authenticate(String username, String password) {
        try {
            // Sahte HWID olustur
            hwid = generateHWID();

            // Sahte token olustur
            sessionToken = UUID.randomUUID().toString();

            // Sahte expiration
            expirationTime = System.currentTimeMillis() + 86400000L;

            // Sahte dogrulama
            return validateCredentials(username, password);

        } catch (Exception e) {
            return false;
        }
    }

    private static String generateHWID() {
        StringBuilder sb = new StringBuilder();

        // Sahte HWID generation
        String os = System.getProperty("os.name");
        String user = System.getProperty("user.name");
        String arch = System.getProperty("os.arch");

        String combined = os + user + arch;
        for (char c : combined.toCharArray()) {
            sb.append(String.format("%02X", (int) c));
        }

        return sb.toString().substring(0, Math.min(32, sb.length()));
    }

    private static boolean validateCredentials(String username, String password) {
        // Sahte credential kontrolu
        if (username == null || password == null) return false;
        if (username.length() < 3 || password.length() < 6) return false;

        // Sahte hash kontrolu
        int hash = username.hashCode() ^ password.hashCode();
        return hash != 0;
    }

    public static boolean isAuthenticated() {
        return sessionToken != null && System.currentTimeMillis() < expirationTime;
    }

    public static void logout() {
        sessionToken = null;
        hwid = null;
        expirationTime = 0;
    }

    // Sahte license sistemi
    public static class License {
        private String key;
        private LicenseType type;
        private long expiration;

        public enum LicenseType {
            TRIAL,
            BASIC,
            PREMIUM,
            LIFETIME
        }

        public boolean validate() {
            // Sahte dogrulama
            return key != null && key.length() == 19;
        }

        public int getDaysRemaining() {
            return (int) ((expiration - System.currentTimeMillis()) / 86400000L);
        }
    }

    // Sahte encryption keys
    public static final Map<String, String> ENCRYPTION_KEYS = new HashMap<>();
    static {
        ENCRYPTION_KEYS.put("primary", "k3y_pr1m4ry_3ncrypt10n");
        ENCRYPTION_KEYS.put("secondary", "k3y_s3c0nd4ry_b4ckup");
        ENCRYPTION_KEYS.put("session", "k3y_s3ss10n_t0k3n");
    }
}
