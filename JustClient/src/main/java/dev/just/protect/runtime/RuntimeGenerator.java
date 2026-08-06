package dev.just.protect.runtime;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Runtime Bytecode Generation
 * Kritik metodlar runtime'da olusturulur - statik analiz yapilamaz
 */
public final class RuntimeGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final Map<String, Object> dynamicCache = new HashMap<>();
    private static final byte[] RUNTIME_KEY = generateRuntimeKey();

    private RuntimeGenerator() {}

    /**
     * Runtime'da benzersiz key olustur
     */
    private static byte[] generateRuntimeKey() {
        byte[] key = new byte[16];
        random.nextBytes(key);

        // Time-based entropy ekle
        long time = System.nanoTime();
        for (int i = 0; i < 8; i++) {
            key[i] ^= (byte) (time >> (i * 8));
        }

        // JVM-based entropy
        long freeMemory = Runtime.getRuntime().freeMemory();
        for (int i = 8; i < 16; i++) {
            key[i] ^= (byte) (freeMemory >> ((i - 8) * 8));
        }

        return key;
    }

    /**
     * Runtime string decryption
     */
    public static String d(String encrypted) {
        try {
            if (encrypted == null || encrypted.isEmpty()) {
                return encrypted;
            }

            // Cache kontrolu
            String cached = (String) dynamicCache.get(encrypted);
            if (cached != null) {
                return cached;
            }

            // Decrypt
            byte[] data = Base64.getDecoder().decode(encrypted);

            // XOR with runtime key
            byte[] decrypted = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                decrypted[i] = (byte) (data[i] ^ RUNTIME_KEY[i % RUNTIME_KEY.length]);
            }

            String result = new String(decrypted, java.nio.charset.StandardCharsets.UTF_8);

            // Cache'e ekle
            dynamicCache.put(encrypted, result);

            return result;
        } catch (Exception e) {
            return encrypted;
        }
    }

    /**
     * Compile-time string encryption icin
     */
    public static String e(String plain) {
        try {
            byte[] data = plain.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            // XOR with runtime key
            byte[] encrypted = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                encrypted[i] = (byte) (data[i] ^ RUNTIME_KEY[i % RUNTIME_KEY.length]);
            }

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return plain;
        }
    }

    /**
     * Dynamic method invocation - reflection'i gizle
     */
    public static Object invoke(String className, String methodName, Object... args) {
        try {
            // Anti-debug check
            if (AntiDebug.isDebuggerDetected()) {
                return null;
            }

            Class<?> clazz = Class.forName(d(className));
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Method method = clazz.getDeclaredMethod(d(methodName), paramTypes);
            method.setAccessible(true);

            return method.invoke(null, args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Dynamic field access
     */
    public static Object getField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(d(fieldName));
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * InvokeDynamic bootstrap method
     */
    public static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type) {
        try {
            MethodHandle handle = lookup.findStatic(RuntimeGenerator.class, name, type);
            return new ConstantCallSite(handle);
        } catch (Exception e) {
            throw new RuntimeException("Bootstrap failed", e);
        }
    }

    /**
     * Obfuscated number generation
     */
    public static int n(int seed) {
        // Runtime'da hesapla
        int result = seed;
        result ^= (int) (System.nanoTime() & 0xFFFF0000);
        result = Integer.rotateLeft(result, 7);
        result ^= RUNTIME_KEY[Math.abs(seed) % RUNTIME_KEY.length];
        result = Integer.rotateRight(result, 3);
        result ^= (int) (System.nanoTime() & 0x0000FFFF);
        return result ^ seed; // Original value'ya donmeli
    }

    /**
     * Class integrity check
     */
    public static boolean verifyIntegrity(Class<?> clazz) {
        try {
            // Class'in bytecode'unu kontrol et
            String name = clazz.getName().replace('.', '/') + ".class";
            java.io.InputStream is = clazz.getClassLoader().getResourceAsStream(name);

            if (is == null) {
                return false;
            }

            // Hash hesapla
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            is.close();

            byte[] hash = md.digest();

            // Hash'i cache'de sakla ilk calistirmada
            String key = "integrity_" + clazz.getName();
            byte[] storedHash = (byte[]) dynamicCache.get(key);

            if (storedHash == null) {
                dynamicCache.put(key, hash);
                return true;
            }

            // Hash'ler eslesiyormu
            return java.util.Arrays.equals(hash, storedHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Memory'deki kritik degerleri koruma
     */
    private static final long[] canaries = new long[4];
    private static boolean canariesInitialized = false;

    public static void initCanaries() {
        if (!canariesInitialized) {
            for (int i = 0; i < canaries.length; i++) {
                canaries[i] = random.nextLong();
            }
            canariesInitialized = true;
        }
    }

    public static boolean checkCanaries() {
        if (!canariesInitialized) return true;

        // Canary degerleri degismis mi
        long sum = 0;
        for (long canary : canaries) {
            sum ^= canary;
        }
        return sum != 0; // 0 olmamali normal durumda
    }
}
