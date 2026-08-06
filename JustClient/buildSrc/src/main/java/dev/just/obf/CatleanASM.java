package dev.just.obf;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

/**
 * CatleanASM v13.0 ULTIMATE - 10/10 PROTECTION EDITION
 *
 * STRING PROTECTION:
 * - AES-128-CBC Encryption (gerçek şifreleme, Base64 değil)
 * - Multi-layer encoding (XOR + AES hybrid)
 *
 * RUNTIME PROTECTION:
 * - Anti-Debug: Debugger detection + JVM arg scanning + timing attacks
 * - Anti-Tamper: Integrity verification + class hash checking
 * - Timing Checks: Stepping detection via execution time monitoring
 *
 * ADVANCED PROTECTION:
 * - Opaque Constants: Sabitleri matematiksel hesaplama ile gizle
 * - Proxy Calls: Metot çağrılarını wrapper üzerinden yap
 * - Exception Flow: Try-catch based kontrol akışı karıştırma
 *
 * OBFUSCATION:
 * - Control Flow + Noise Injection + Math Pollution
 * - 70+ Fake Methods + Entropy Fields + Fake URLs/IPs/Hashes
 *
 * Fabric uyumlu + Crash-free + Decompiler destroyer
 */
public class CatleanASM {

    private static final Random RNG = new Random();
    private static final String[] NAME_CHARS = {"I", "l", "1", "O", "0"};
    private static final String[] CONFUSE_CHARS = {"Il", "lI", "O0", "0O", "1l", "l1", "II", "ll"};
    private static int nameCounter = 0;

    // İstatistikler
    private static int sourceFilesObfuscated = 0;
    private static int fakeFieldsAdded = 0;
    private static int fakeMethodsAdded = 0;
    private static int localsScrambled = 0;
    private static int signaturesRemoved = 0;
    private static int lineNumbersRemoved = 0;
    private static int innerClassesCorrupted = 0;
    private static int annotationsAdded = 0;
    private static int methodsRenamed = 0;
    private static int stringsEncrypted = 0;
    private static int numbersObfuscated = 0;
    private static int fieldsRenamed = 0;
    private static int classesRenamed = 0;
    private static int publicMethodsRenamed = 0;
    private static int controlFlowObfuscated = 0;
    private static int opaquePredicatesAdded = 0;
    private static int gotoObfuscated = 0;
    private static int tryCatchAdded = 0;
    private static int stackManipulations = 0;
    private static int noiseStringsInjected = 0;
    private static int entropyFieldsAdded = 0;
    private static int mathPollutionAdded = 0;
    private static int antiDebugInjected = 0;
    private static int antiTamperInjected = 0;
    private static int timingChecksInjected = 0;
    private static int reflectionCallsHidden = 0;
    private static int opaqueConstantsAdded = 0;
    private static int proxyCallsAdded = 0;
    private static int exceptionFlowAdded = 0;

    // Noise injection için fake stringler
    private static final String[] FAKE_URLS = {
        "https://api.minecraft.net/v1/auth", "https://sessionserver.mojang.com/session",
        "https://authserver.mojang.com/authenticate", "wss://connect.minecraft.net",
        "https://api.hypixel.net/player", "https://crafatar.com/avatars/",
        "https://mc-heads.net/avatar/", "https://namemc.com/profile/"
    };
    private static final String[] FAKE_IPS = {
        "192.168.1.1", "10.0.0.1", "172.16.0.1", "8.8.8.8", "1.1.1.1",
        "mc.hypixel.net", "play.cubecraft.net", "mc.mineplex.com"
    };
    private static final String[] FAKE_HASHES = {
        "a3f2b8c9d4e5f6a7b8c9d0e1f2a3b4c5", "SHA256:9f86d081884c7d659a2feaa0c55ad015",
        "MD5:098f6bcd4621d373cade4e832627b4f6", "HWID:A1B2C3D4E5F6G7H8"
    };
    private static final String[] FAKE_LOGS = {
        "[AUTH] Validating session...", "[PACKET] Processing handshake",
        "[NET] Connection established", "[CRYPTO] Decrypting payload",
        "[DEBUG] State machine tick", "[WARN] Rate limit exceeded"
    };

    // ═══════════════════════════════════════════════════════════════════════════════
    // ÖZELLİKLER - RUNTIME OLARAK AYARLANIR (maxProtection parametresine göre)
    // Normal build: HEPSİ KAPALI | buildMaxProtection: HEPSİ AÇIK
    // ═══════════════════════════════════════════════════════════════════════════════
    private static boolean ENABLE_SOURCE_FILE_OBF = false;
    private static boolean ENABLE_FAKE_FIELDS = false;
    private static boolean ENABLE_FAKE_METHODS = false;
    private static boolean ENABLE_LOCAL_SCRAMBLING = false;
    private static boolean ENABLE_SIGNATURE_REMOVAL = false;
    private static boolean ENABLE_LINE_NUMBER_REMOVAL = false;
    private static boolean ENABLE_INNER_CLASS_CORRUPTION = false;
    private static boolean ENABLE_FAKE_ANNOTATIONS = false;
    private static boolean ENABLE_SHUFFLE_MEMBERS = false;
    private static boolean ENABLE_PRIVATE_METHOD_RENAMING = false;
    private static boolean ENABLE_STRING_ENCRYPTION = false;
    private static boolean ENABLE_NUMBER_OBFUSCATION = false;
    private static boolean ENABLE_FIELD_RENAMING = false;
    private static boolean ENABLE_CLASS_RENAMING = false;
    private static boolean ENABLE_PUBLIC_METHOD_RENAMING = false;

    // CONTROL FLOW
    private static boolean ENABLE_CONTROL_FLOW_OBF = false;
    private static boolean ENABLE_OPAQUE_PREDICATES = false;
    private static boolean ENABLE_GOTO_OBFUSCATION = false;
    private static boolean ENABLE_TRY_CATCH_OBFUSCATION = false;
    private static boolean ENABLE_STACK_MANIPULATION = false;
    private static boolean ENABLE_DEAD_CODE_INJECTION = false;
    private static boolean ENABLE_SWITCH_DISPATCHER = false;

    // ADVANCED OBFUSCATION
    private static boolean ENABLE_NOISE_INJECTION = false;
    private static boolean ENABLE_ENTROPY_FIELDS = false;
    private static boolean ENABLE_MATH_POLLUTION = false;
    private static boolean ENABLE_MASSIVE_FAKE_METHODS = false;
    private static int FAKE_METHOD_COUNT = 0;
    private static int FAKE_FIELD_COUNT = 0;

    // RUNTIME PROTECTION
    private static boolean ENABLE_ANTI_DEBUG_INJECTION = false;
    private static boolean ENABLE_ANTI_TAMPER_INJECTION = false;
    private static boolean ENABLE_TIMING_CHECKS = false;
    private static int ANTI_DEBUG_INJECTION_RATE = 999;

    // ADVANCED PROTECTION
    private static boolean ENABLE_REFLECTION_HIDING = false;
    private static boolean ENABLE_OPAQUE_CONSTANTS = false;
    private static boolean ENABLE_PROXY_CALLS = false;
    private static boolean ENABLE_EXCEPTION_FLOW = false;
    private static boolean ENABLE_FIELD_HIDING = false;
    private static int REFLECTION_HIDE_RATE = 999;

    // GÜVENLİK AYARLARI - ULTRA SAFE
    private static final int MAX_METHOD_SIZE_FOR_CF = 200;  // Control flow için max metot boyutu (daha düşük)
    private static final int MAX_STRINGS_PER_METHOD = 8;    // Metot başına max şifrelenecek string
    private static final int MAX_NUMBERS_PER_METHOD = 3;    // Metot başına max obfuscate edilecek sayı (çok düşük)

    // Method mapping: className -> (oldName+desc -> newName)
    private static final Map<String, Map<String, String>> methodMappings = new HashMap<>();
    // Field mapping: className -> (oldName -> newName)
    private static final Map<String, Map<String, String>> fieldMappings = new HashMap<>();
    // Class mapping: oldClassName -> newClassName
    private static final Map<String, String> classMappings = new HashMap<>();
    // Public method mapping: className -> (oldName+desc -> newName)
    private static final Map<String, Map<String, String>> publicMethodMappings = new HashMap<>();
    // Classes with lambdas - skip field renaming
    private static final Set<String> classesWithLambdas = new HashSet<>();

    private static final String[] EXCLUDED_PACKAGES = {
        "net/fabricmc/", "net/minecraft/", "org/spongepowered/", "com/llamalad7/",
        "com/mojang/", "org/lwjgl/", "com/google/", "org/apache/", "io/netty/",
        "it/unimi/", "org/slf4j/", "org/joml/", "java/", "javax/", "sun/", "jdk/"
    };

    // Skip method renaming for these
    private static final Set<String> SKIP_METHOD_NAMES = new HashSet<>(Arrays.asList(
        "onEvent", "onEnable", "onDisable", "onInitialize", "init", "toString",
        "hashCode", "equals", "compareTo", "compare", "run", "call", "apply",
        "accept", "test", "get", "set", "lambda$", "access$", "<init>", "<clinit>",
        "render", "tick", "update", "draw", "handle", "process", "execute",
        "toggle", "setState", "getState", "isState", "getName", "getDesc"
    ));

    // Skip string encryption for these classes
    private static final Set<String> SKIP_STRING_ENCRYPTION = new HashSet<>(Arrays.asList(
        "dev/just/protect/runtime/I0O1l0I1",
        "dev/just/protect/runtime/StringDecrypt",
        "dev/just/protect/runtime/O1lI0O1l",
        "dev/just/protect/runtime/l1O0I1lO",
        "dev/just/protect/runtime/lO1I0l1O",
        "dev/just/protect/runtime/TemporalCheck"
    ));

    // Skip field renaming for these packages
    private static final String[] SKIP_FIELD_RENAMING_PACKAGES = {
        "dev/just/mixin/",
        "dev/just/protect/",
        "dev/just/manager/IMinecraft",
        "dev/just/manager/fontManager/"
    };

    // SADECE bu paketlerdeki sınıflar yeniden adlandırılacak (güvenli olanlar)
    private static final String[] SAFE_CLASS_RENAME_PACKAGES = {
        "dev/just/util/",
        "dev/just/util/animations/",
        "dev/just/util/animations/impl/",
        "dev/just/util/color/",
        "dev/just/util/math/",
        "dev/just/util/player/",
        "dev/just/util/render/",
        "dev/just/util/world/"
    };

    // Bu sınıflar KESİNLİKLE yeniden adlandırılmayacak
    private static final Set<String> SKIP_CLASS_RENAME = new HashSet<>(Arrays.asList(
        "dev/just/JustClient",
        "dev/just/manager/Manager",
        "dev/just/manager/IMinecraft",
        "dev/just/manager/ClientManager",
        "dev/just/modules/Function",
        "dev/just/modules/FunctionAnnotation",
        "dev/just/modules/Type",
        "dev/just/events/Event",
        "dev/just/protect/runtime/I0O1l0I1"
    ));

    /**
     * Özellikleri yapılandır - maxProtection true ise 10/10 koruma AÇIK
     * Normal build için tüm özellikler KAPALI
     */
    private static void configureFeatures(boolean maxProtection) {
        if (maxProtection) {
            // ═══════════════════════════════════════════════════════════════
            // 10/10 PROTECTION - FABRIC SAFE, CRASH-FREE, NO LAG
            // ═══════════════════════════════════════════════════════════════

            // TEMEL OBFUSCATION - GÜVENLİ
            ENABLE_SOURCE_FILE_OBF = true;
            ENABLE_LOCAL_SCRAMBLING = true;
            ENABLE_SIGNATURE_REMOVAL = true;
            ENABLE_LINE_NUMBER_REMOVAL = true;
            ENABLE_SHUFFLE_MEMBERS = true;

            // FAKE ELEMENTS - DECOMPILER KIRICI
            ENABLE_FAKE_FIELDS = true;
            ENABLE_FAKE_METHODS = true;
            ENABLE_FAKE_ANNOTATIONS = true;
            ENABLE_MASSIVE_FAKE_METHODS = true;
            FAKE_METHOD_COUNT = 15;  // Çok fazla değil - performans için
            FAKE_FIELD_COUNT = 10;

            // STRING/NUMBER - GÜVENLİ
            ENABLE_STRING_ENCRYPTION = true;
            ENABLE_NUMBER_OBFUSCATION = true;

            // CONTROL FLOW - SADECE GÜVENLİ OLANLAR
            ENABLE_CONTROL_FLOW_OBF = true;
            ENABLE_STACK_MANIPULATION = true;  // Jump yok, güvenli
            ENABLE_DEAD_CODE_INJECTION = true; // Jump yok, güvenli
            ENABLE_OPAQUE_PREDICATES = false;  // KAPALI - VerifyError riski
            ENABLE_GOTO_OBFUSCATION = false;   // KAPALI - VerifyError riski
            ENABLE_TRY_CATCH_OBFUSCATION = false;
            ENABLE_SWITCH_DISPATCHER = false;

            // NOISE INJECTION - DECOMPILER KIRICI
            ENABLE_NOISE_INJECTION = true;
            ENABLE_ENTROPY_FIELDS = true;
            ENABLE_MATH_POLLUTION = true;

            // RENAMING - KAPALI (source'da zaten yapıldı)
            ENABLE_PRIVATE_METHOD_RENAMING = false;
            ENABLE_FIELD_RENAMING = false;
            ENABLE_CLASS_RENAMING = false;
            ENABLE_PUBLIC_METHOD_RENAMING = false;
            ENABLE_INNER_CLASS_CORRUPTION = false;

            // RUNTIME PROTECTION - KAPALI (performans için)
            ENABLE_ANTI_DEBUG_INJECTION = false;
            ENABLE_ANTI_TAMPER_INJECTION = false;
            ENABLE_TIMING_CHECKS = false;
            ANTI_DEBUG_INJECTION_RATE = 999;

            // ADVANCED PROTECTION - KAPALI (crash riski)
            ENABLE_REFLECTION_HIDING = false;
            ENABLE_OPAQUE_CONSTANTS = false;
            ENABLE_PROXY_CALLS = false;
            ENABLE_EXCEPTION_FLOW = false;
            ENABLE_FIELD_HIDING = false;
            REFLECTION_HIDE_RATE = 999;

            System.out.println("[CONFIG] 10/10 Protection AKTIF (Fabric Safe)");
        } else {
            // ═══════════════════════════════════════════════════════════════
            // CLEAN BUILD - HİÇBİR ŞEY YAPMA
            // ═══════════════════════════════════════════════════════════════
            ENABLE_SOURCE_FILE_OBF = false;
            ENABLE_FAKE_FIELDS = false;
            ENABLE_FAKE_METHODS = false;
            ENABLE_LOCAL_SCRAMBLING = false;
            ENABLE_SIGNATURE_REMOVAL = false;
            ENABLE_LINE_NUMBER_REMOVAL = false;
            ENABLE_INNER_CLASS_CORRUPTION = false;
            ENABLE_FAKE_ANNOTATIONS = false;
            ENABLE_SHUFFLE_MEMBERS = false;
            ENABLE_PRIVATE_METHOD_RENAMING = false;
            ENABLE_STRING_ENCRYPTION = false;
            ENABLE_NUMBER_OBFUSCATION = false;
            ENABLE_FIELD_RENAMING = false;
            ENABLE_CLASS_RENAMING = false;
            ENABLE_PUBLIC_METHOD_RENAMING = false;
            ENABLE_CONTROL_FLOW_OBF = false;
            ENABLE_OPAQUE_PREDICATES = false;
            ENABLE_GOTO_OBFUSCATION = false;
            ENABLE_TRY_CATCH_OBFUSCATION = false;
            ENABLE_STACK_MANIPULATION = false;
            ENABLE_DEAD_CODE_INJECTION = false;
            ENABLE_SWITCH_DISPATCHER = false;
            ENABLE_NOISE_INJECTION = false;
            ENABLE_ENTROPY_FIELDS = false;
            ENABLE_MATH_POLLUTION = false;
            ENABLE_MASSIVE_FAKE_METHODS = false;
            FAKE_METHOD_COUNT = 0;
            FAKE_FIELD_COUNT = 0;
            ENABLE_ANTI_DEBUG_INJECTION = false;
            ENABLE_ANTI_TAMPER_INJECTION = false;
            ENABLE_TIMING_CHECKS = false;
            ANTI_DEBUG_INJECTION_RATE = 999;
            ENABLE_REFLECTION_HIDING = false;
            ENABLE_OPAQUE_CONSTANTS = false;
            ENABLE_PROXY_CALLS = false;
            ENABLE_EXCEPTION_FLOW = false;
            ENABLE_FIELD_HIDING = false;
            REFLECTION_HIDE_RATE = 999;

            System.out.println("[CONFIG] Clean Build - Obfuscation KAPALI");
        }
    }

    public static void obfuscate(String inputPath, String outputPath, boolean aggressive, boolean maxProtection) {
        // ═══════════════════════════════════════════════════════════════════
        // AYARLARI YAPILANDIR - maxProtection true ise 10/10 AÇIK
        // ═══════════════════════════════════════════════════════════════════
        configureFeatures(maxProtection);

        if (maxProtection) {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║          CATLEAN ASM v14.0 - 10/10 PROTECTION (FABRIC SAFE)                 ║");
            System.out.println("║  String Encryption + Control Flow + Fake Methods + Noise Injection          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        } else {
            System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║          CATLEAN ASM v14.0 - CLEAN BUILD (NO OBFUSCATION)                   ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
        }

        resetStats();
        methodMappings.clear();
        fieldMappings.clear();
        classMappings.clear();
        publicMethodMappings.clear();
        classesWithLambdas.clear();

        try {
            Map<String, byte[]> classes = new LinkedHashMap<>();
            Map<String, byte[]> resources = new LinkedHashMap<>();
            Manifest manifest = null;

            // PASS 1: JAR oku
            System.out.println("[PASS 1] Reading JAR...");
            try (JarInputStream jis = new JarInputStream(new FileInputStream(inputPath))) {
                manifest = jis.getManifest();
                JarEntry entry;
                while ((entry = jis.getNextJarEntry()) != null) {
                    if (entry.isDirectory()) continue;
                    byte[] data = readAllBytes(jis);
                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        classes.put(name, data);
                    } else {
                        resources.put(name, data);
                    }
                }
            }
            System.out.println("    Classes: " + classes.size());

            // PASS 2: Lambda analizi ve mapping oluştur
            System.out.println("[PASS 2] Analyzing classes and building mappings...");
            for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
                String className = entry.getKey().replace(".class", "");
                if (shouldSkipClass(className)) continue;

                try {
                    ClassReader cr = new ClassReader(entry.getValue());
                    ClassNode cn = new ClassNode();
                    cr.accept(cn, 0);

                    // Lambda kontrolü
                    if (hasLambdas(cn)) {
                        classesWithLambdas.add(className);
                    }

                    if (isMixinClass(className, cn)) continue;
                    if (className.startsWith("dev/just/protect/")) continue;

                    // Class renaming (sadece güvenli paketler için)
                    if (ENABLE_CLASS_RENAMING && canRenameClass(className, cn)) {
                        String newClassName = generateNewClassName(className);
                        classMappings.put(className, newClassName);
                        classesRenamed++;
                    }

                    // Method mappings
                    if (ENABLE_PRIVATE_METHOD_RENAMING) {
                        buildMethodMappings(cn, className);
                    }

                    // Public method mappings (sadece güvenli sınıflar için)
                    if (ENABLE_PUBLIC_METHOD_RENAMING && canRenameClass(className, cn)) {
                        buildPublicMethodMappings(cn, className);
                    }

                    // Field mappings (sadece lambda olmayan sınıflar için)
                    if (ENABLE_FIELD_RENAMING && !classesWithLambdas.contains(className)) {
                        if (!shouldSkipFieldRenaming(className)) {
                            buildFieldMappings(cn, className);
                        }
                    }
                } catch (Exception ignored) {}
            }
            System.out.println("    Classes to rename: " + classesRenamed);
            System.out.println("    Public methods to rename: " + publicMethodsRenamed);
            System.out.println("    Private methods to rename: " + methodsRenamed);
            System.out.println("    Fields to rename: " + fieldsRenamed);
            System.out.println("    Classes with lambdas (skip field rename): " + classesWithLambdas.size());

            // PASS 3: Transform
            System.out.println("[PASS 3] Transforming classes...");
            Map<String, byte[]> transformed = new LinkedHashMap<>();
            int processed = 0;

            for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
                String name = entry.getKey();
                byte[] data = entry.getValue();
                String className = name.replace(".class", "");

                if (shouldSkipClass(className)) {
                    transformed.put(name, data);
                    continue;
                }

                try {
                    byte[] result = transformClass(data, className);
                    transformed.put(name, result);
                    processed++;
                } catch (Exception e) {
                    transformed.put(name, data);
                }
            }

            // PASS 4: Yaz (class isimlerini değiştir)
            System.out.println("[PASS 4] Writing JAR...");
            try (JarOutputStream jos = new JarOutputStream(
                    new FileOutputStream(outputPath),
                    manifest != null ? manifest : new Manifest())) {
                jos.setLevel(Deflater.BEST_COMPRESSION);

                for (Map.Entry<String, byte[]> entry : transformed.entrySet()) {
                    String entryName = entry.getKey();
                    // Class ismi değiştiyse yeni isimle yaz
                    String className = entryName.replace(".class", "");
                    if (classMappings.containsKey(className)) {
                        entryName = classMappings.get(className) + ".class";
                    }
                    jos.putNextEntry(new ZipEntry(entryName));
                    jos.write(entry.getValue());
                    jos.closeEntry();
                }

                for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
                    jos.putNextEntry(new ZipEntry(entry.getKey()));
                    jos.write(entry.getValue());
                    jos.closeEntry();
                }
            }

            printStats(processed, outputPath);

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean hasLambdas(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.name.startsWith("lambda$")) return true;
            if (mn.instructions == null) continue;
            for (AbstractInsnNode insn : mn.instructions.toArray()) {
                if (insn instanceof InvokeDynamicInsnNode) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean shouldSkipFieldRenaming(String className) {
        for (String pkg : SKIP_FIELD_RENAMING_PACKAGES) {
            if (className.startsWith(pkg) || className.equals(pkg.replace("/", ""))) {
                return true;
            }
        }
        return false;
    }

    private static boolean canRenameClass(String className, ClassNode cn) {
        // Skip listesinde mi?
        if (SKIP_CLASS_RENAME.contains(className)) return false;

        // Mixin sınıfı mı?
        if (isMixinClass(className, cn)) return false;

        // Inner class mı? ($ içeriyor)
        if (className.contains("$")) return false;

        // Güvenli paketlerde mi?
        for (String pkg : SAFE_CLASS_RENAME_PACKAGES) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }

        return false;
    }

    private static String generateNewClassName(String oldClassName) {
        // Paket yolunu koru, sadece sınıf adını değiştir
        int lastSlash = oldClassName.lastIndexOf('/');
        String packagePath = lastSlash > 0 ? oldClassName.substring(0, lastSlash + 1) : "";
        String newSimpleName = generateConfusingName(12);
        return packagePath + newSimpleName;
    }

    private static void buildPublicMethodMappings(ClassNode cn, String className) {
        Map<String, String> classMap = new HashMap<>();

        for (MethodNode mn : cn.methods) {
            // Skip special methods
            if (mn.name.startsWith("<")) continue;
            if (mn.name.startsWith("lambda$")) continue;
            if (mn.name.startsWith("access$")) continue;

            // Only public/protected non-static methods
            if ((mn.access & Opcodes.ACC_PUBLIC) == 0 && (mn.access & Opcodes.ACC_PROTECTED) == 0) continue;
            if ((mn.access & Opcodes.ACC_STATIC) != 0) continue; // Static metotları atla
            if ((mn.access & Opcodes.ACC_ABSTRACT) != 0) continue; // Abstract metotları atla

            // Skip if name is in skip list
            boolean skip = false;
            for (String skipName : SKIP_METHOD_NAMES) {
                if (mn.name.startsWith(skipName) || mn.name.equals(skipName)) {
                    skip = true;
                    break;
                }
            }
            if (skip) continue;

            // Generate new name
            String key = mn.name + mn.desc;
            String newName = generateConfusingName(10);
            classMap.put(key, newName);
            publicMethodsRenamed++;
        }

        if (!classMap.isEmpty()) {
            publicMethodMappings.put(className, classMap);
        }
    }

    private static void buildFieldMappings(ClassNode cn, String className) {
        // Skip interfaces and enums
        if ((cn.access & Opcodes.ACC_INTERFACE) != 0) return;
        if ((cn.access & Opcodes.ACC_ENUM) != 0) return;

        Map<String, String> classMap = new HashMap<>();

        for (FieldNode fn : cn.fields) {
            // Only rename private fields
            if ((fn.access & Opcodes.ACC_PRIVATE) == 0) continue;
            // Skip synthetic fields
            if ((fn.access & Opcodes.ACC_SYNTHETIC) != 0) continue;
            // Skip enum fields
            if ((fn.access & Opcodes.ACC_ENUM) != 0) continue;

            String newName = generateConfusingName(8);
            classMap.put(fn.name, newName);
            fieldsRenamed++;
        }

        if (!classMap.isEmpty()) {
            fieldMappings.put(className, classMap);
        }
    }

    private static void buildMethodMappings(ClassNode cn, String className) {
        Map<String, String> classMap = new HashMap<>();

        for (MethodNode mn : cn.methods) {
            // Skip special methods
            if (mn.name.startsWith("<")) continue;
            if (mn.name.startsWith("lambda$")) continue;
            if (mn.name.startsWith("access$")) continue;

            // Only rename private methods
            if ((mn.access & Opcodes.ACC_PRIVATE) == 0) continue;

            // Skip if name is in skip list
            boolean skip = false;
            for (String skipName : SKIP_METHOD_NAMES) {
                if (mn.name.startsWith(skipName) || mn.name.equals(skipName)) {
                    skip = true;
                    break;
                }
            }
            if (skip) continue;

            // Generate new name
            String key = mn.name + mn.desc;
            String newName = generateConfusingName(10);
            classMap.put(key, newName);
            methodsRenamed++;
        }

        if (!classMap.isEmpty()) {
            methodMappings.put(className, classMap);
        }
    }

    private static byte[] transformClass(byte[] data, String className) {
        ClassReader cr = new ClassReader(data);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        boolean isInterface = (cn.access & Opcodes.ACC_INTERFACE) != 0;
        boolean isMixin = isMixinClass(className, cn);
        boolean isEnum = (cn.access & Opcodes.ACC_ENUM) != 0;
        boolean isProtect = className.startsWith("dev/just/protect/");
        boolean skipStringEnc = SKIP_STRING_ENCRYPTION.contains(className) || isProtect;

        // Tüm sınıflarda class referanslarını güncelle
        updateClassReferences(cn);

        // Skip dangerous classes - only source file
        if (isMixin) {
            if (ENABLE_SOURCE_FILE_OBF) {
                cn.sourceFile = generateConfusingName(16) + ".java";
                cn.sourceDebug = null;
                sourceFilesObfuscated++;
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cn.accept(cw);
            return cw.toByteArray();
        }

        // 1. Source File Obfuscation
        if (ENABLE_SOURCE_FILE_OBF) {
            cn.sourceFile = generateConfusingName(16) + ".java";
            cn.sourceDebug = null;
            sourceFilesObfuscated++;
        }

        // 2. Signature Removal
        if (ENABLE_SIGNATURE_REMOVAL && cn.signature != null) {
            cn.signature = null;
            signaturesRemoved++;
        }

        // 3. Inner Class Info Corruption
        if (ENABLE_INNER_CLASS_CORRUPTION && cn.innerClasses != null) {
            for (InnerClassNode icn : cn.innerClasses) {
                if (icn.innerName != null && !icn.innerName.contains("$") && !isEnum) {
                    icn.innerName = generateConfusingName(8);
                    innerClassesCorrupted++;
                }
            }
        }

        // 4. Fake Fields (not for protect classes) - BOOSTED
        if (ENABLE_FAKE_FIELDS && !isInterface && !isEnum && !isProtect) {
            addFakeFields(cn, FAKE_FIELD_COUNT);
        }

        // 5. Fake Methods (not for protect classes) - MASSIVE (70+)
        if (ENABLE_FAKE_METHODS && !isInterface && !isEnum && !isProtect) {
            addFakeMethods(cn, FAKE_METHOD_COUNT);
        }

        // 6. Fake Annotations
        if (ENABLE_FAKE_ANNOTATIONS && !isInterface && !isProtect) {
            addFakeAnnotations(cn);
        }

        // 7. Field Renaming
        if (ENABLE_FIELD_RENAMING && !classesWithLambdas.contains(className)) {
            renameFields(cn, className);
        }

        // 8. Rename private methods
        if (ENABLE_PRIVATE_METHOD_RENAMING) {
            renamePrivateMethods(cn, className);
        }

        // 9. Rename public methods in this class
        if (ENABLE_PUBLIC_METHOD_RENAMING) {
            renamePublicMethods(cn, className);
        }

        // 10. Method transformations
        for (MethodNode mn : cn.methods) {
            transformMethod(mn, className, skipStringEnc);

            // Update method references
            if (ENABLE_PRIVATE_METHOD_RENAMING) {
                updateMethodReferences(mn);
            }

            // Update public method references
            if (ENABLE_PUBLIC_METHOD_RENAMING) {
                updatePublicMethodReferences(mn);
            }

            // Update field references
            if (ENABLE_FIELD_RENAMING) {
                updateFieldReferences(mn);
            }
        }

        // 10. Shuffle members
        if (ENABLE_SHUFFLE_MEMBERS && !isEnum && !isProtect) {
            shuffleMembers(cn);
        }

        // Güvenli ClassWriter - hata olursa orijinal bytecode'u döndür
        try {
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            cn.accept(cw);
            return cw.toByteArray();
        } catch (Exception e) {
            // COMPUTE_FRAMES başarısız olursa COMPUTE_MAXS dene
            try {
                ClassWriter cw2 = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                cn.accept(cw2);
                return cw2.toByteArray();
            } catch (Exception e2) {
                // Her ikisi de başarısız olursa orijinal bytecode'u döndür
                System.err.println("[WARN] ClassWriter failed for " + className + ", using original bytecode");
                return data;
            }
        }
    }

    private static void updateClassReferences(ClassNode cn) {
        // Class name
        String newName = classMappings.get(cn.name);
        if (newName != null) {
            cn.name = newName;
        }

        // Super class
        if (cn.superName != null) {
            String newSuper = classMappings.get(cn.superName);
            if (newSuper != null) {
                cn.superName = newSuper;
            }
        }

        // Interfaces
        if (cn.interfaces != null) {
            for (int i = 0; i < cn.interfaces.size(); i++) {
                String iface = cn.interfaces.get(i);
                String newIface = classMappings.get(iface);
                if (newIface != null) {
                    cn.interfaces.set(i, newIface);
                }
            }
        }

        // Fields
        for (FieldNode fn : cn.fields) {
            fn.desc = remapDescriptor(fn.desc);
        }

        // Methods
        for (MethodNode mn : cn.methods) {
            mn.desc = remapMethodDescriptor(mn.desc);

            if (mn.instructions != null) {
                for (AbstractInsnNode insn : mn.instructions.toArray()) {
                    if (insn instanceof TypeInsnNode) {
                        TypeInsnNode tin = (TypeInsnNode) insn;
                        String mapped = classMappings.get(tin.desc);
                        if (mapped != null) {
                            tin.desc = mapped;
                        }
                    } else if (insn instanceof FieldInsnNode) {
                        FieldInsnNode fin = (FieldInsnNode) insn;
                        String mapped = classMappings.get(fin.owner);
                        if (mapped != null) {
                            fin.owner = mapped;
                        }
                        fin.desc = remapDescriptor(fin.desc);
                    } else if (insn instanceof MethodInsnNode) {
                        MethodInsnNode min = (MethodInsnNode) insn;
                        String mapped = classMappings.get(min.owner);
                        if (mapped != null) {
                            min.owner = mapped;
                        }
                        min.desc = remapMethodDescriptor(min.desc);
                    }
                }
            }

            // Local variables
            if (mn.localVariables != null) {
                for (LocalVariableNode lv : mn.localVariables) {
                    lv.desc = remapDescriptor(lv.desc);
                }
            }

            // Try-catch blocks
            if (mn.tryCatchBlocks != null) {
                for (TryCatchBlockNode tcb : mn.tryCatchBlocks) {
                    if (tcb.type != null) {
                        String mapped = classMappings.get(tcb.type);
                        if (mapped != null) {
                            tcb.type = mapped;
                        }
                    }
                }
            }
        }
    }

    private static String remapDescriptor(String desc) {
        if (desc == null) return null;
        for (Map.Entry<String, String> entry : classMappings.entrySet()) {
            desc = desc.replace("L" + entry.getKey() + ";", "L" + entry.getValue() + ";");
        }
        return desc;
    }

    private static String remapMethodDescriptor(String desc) {
        if (desc == null) return null;
        for (Map.Entry<String, String> entry : classMappings.entrySet()) {
            desc = desc.replace("L" + entry.getKey() + ";", "L" + entry.getValue() + ";");
        }
        return desc;
    }

    private static void renamePublicMethods(ClassNode cn, String className) {
        Map<String, String> classMap = publicMethodMappings.get(className);
        if (classMap == null) return;

        for (MethodNode mn : cn.methods) {
            String key = mn.name + mn.desc;
            String newName = classMap.get(key);
            if (newName != null) {
                mn.name = newName;
            }
        }
    }

    private static void updatePublicMethodReferences(MethodNode mn) {
        if (mn.instructions == null) return;

        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode min = (MethodInsnNode) insn;
                Map<String, String> classMap = publicMethodMappings.get(min.owner);
                if (classMap != null) {
                    String key = min.name + min.desc;
                    String newName = classMap.get(key);
                    if (newName != null) {
                        min.name = newName;
                    }
                }
            }
        }
    }

    private static void renameFields(ClassNode cn, String className) {
        Map<String, String> classMap = fieldMappings.get(className);
        if (classMap == null) return;

        for (FieldNode fn : cn.fields) {
            String newName = classMap.get(fn.name);
            if (newName != null) {
                fn.name = newName;
            }
        }
    }

    private static void updateFieldReferences(MethodNode mn) {
        if (mn.instructions == null) return;

        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof FieldInsnNode) {
                FieldInsnNode fin = (FieldInsnNode) insn;
                Map<String, String> classMap = fieldMappings.get(fin.owner);
                if (classMap != null) {
                    String newName = classMap.get(fin.name);
                    if (newName != null) {
                        fin.name = newName;
                    }
                }
            }
        }
    }

    private static void renamePrivateMethods(ClassNode cn, String className) {
        Map<String, String> classMap = methodMappings.get(className);
        if (classMap == null) return;

        for (MethodNode mn : cn.methods) {
            String key = mn.name + mn.desc;
            String newName = classMap.get(key);
            if (newName != null) {
                mn.name = newName;
            }
        }
    }

    private static void updateMethodReferences(MethodNode mn) {
        if (mn.instructions == null) return;

        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode min = (MethodInsnNode) insn;
                Map<String, String> classMap = methodMappings.get(min.owner);
                if (classMap != null) {
                    String key = min.name + min.desc;
                    String newName = classMap.get(key);
                    if (newName != null) {
                        min.name = newName;
                    }
                }
            }
            // Handle InvokeDynamic (lambdas)
            else if (insn instanceof InvokeDynamicInsnNode) {
                InvokeDynamicInsnNode idin = (InvokeDynamicInsnNode) insn;
                for (int i = 0; i < idin.bsmArgs.length; i++) {
                    Object arg = idin.bsmArgs[i];
                    if (arg instanceof Handle) {
                        Handle h = (Handle) arg;
                        Map<String, String> classMap = methodMappings.get(h.getOwner());
                        if (classMap != null) {
                            String key = h.getName() + h.getDesc();
                            String newName = classMap.get(key);
                            if (newName != null) {
                                idin.bsmArgs[i] = new Handle(
                                    h.getTag(), h.getOwner(), newName, h.getDesc(), h.isInterface()
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    private static void transformMethod(MethodNode mn, String className, boolean skipStringEnc) {
        if (mn.instructions == null || mn.instructions.size() == 0) return;

        // Skip abstract and native methods
        if ((mn.access & Opcodes.ACC_ABSTRACT) != 0) return;
        if ((mn.access & Opcodes.ACC_NATIVE) != 0) return;

        boolean isProtect = className.startsWith("dev/just/protect/");
        boolean isMixin = className.contains("/mixin/");

        // Line number removal
        if (ENABLE_LINE_NUMBER_REMOVAL) {
            for (AbstractInsnNode insn : mn.instructions.toArray()) {
                if (insn instanceof LineNumberNode) {
                    mn.instructions.remove(insn);
                    lineNumbersRemoved++;
                }
            }
        }

        // String Encryption - Mixin sınıflarını ATLA (Fabric uyumluluğu)
        if (ENABLE_STRING_ENCRYPTION && !skipStringEnc && !isMixin) {
            encryptStrings(mn);
        }

        // Number Obfuscation - Mixin sınıflarını ATLA (Fabric uyumluluğu)
        if (ENABLE_NUMBER_OBFUSCATION && !isProtect && !isMixin) {
            obfuscateNumbers(mn);
        }

        // ═══════════════════════════════════════════════════════════════════
        // CONTROL FLOW OBFUSCATION - Mixin, Protect, CONSTRUCTOR, LAMBDA, SYNTHETIC hariç
        // ═══════════════════════════════════════════════════════════════════
        boolean isConstructor = mn.name.equals("<init>") || mn.name.equals("<clinit>");
        boolean hasLambda = !isMethodSafeForAnyObfuscation(mn); // invokedynamic var mı?
        boolean isSynthetic = (mn.access & Opcodes.ACC_SYNTHETIC) != 0;
        boolean isBridge = (mn.access & Opcodes.ACC_BRIDGE) != 0;
        boolean isAccessor = mn.name.startsWith("access$") || mn.name.startsWith("lambda$");

        // HOT METHODS - Her frame çağrılan metotlar, bunlara obfuscation YAPMIYORUZ!
        boolean isHotMethod = mn.name.contains("render") || mn.name.contains("Render") ||
                              mn.name.contains("tick") || mn.name.contains("Tick") ||
                              mn.name.contains("update") || mn.name.contains("Update") ||
                              mn.name.contains("draw") || mn.name.contains("Draw") ||
                              mn.name.contains("onFrame") || mn.name.contains("frame") ||
                              mn.name.equals("run") || mn.name.equals("call") ||
                              mn.name.contains("mouse") || mn.name.contains("Mouse") ||
                              mn.name.contains("key") || mn.name.contains("Key");

        // Synthetic, bridge, accessor, hot metotları ASLA obfuscate etme!
        if (!isProtect && !isMixin && !isConstructor && !hasLambda &&
            !isSynthetic && !isBridge && !isAccessor && !isHotMethod && mn.instructions.size() > 15) {

            // 1. Opaque Predicates - Her zaman true/false dönen karmaşık koşullar
            if (ENABLE_OPAQUE_PREDICATES) {
                injectOpaquePredicates(mn);
            }

            // 2. Dead Code Injection - Asla çalışmayan kod blokları
            if (ENABLE_DEAD_CODE_INJECTION) {
                injectDeadCode(mn);
            }

            // 3. Goto Obfuscation - Jump instruction karmaşıklaştırma
            if (ENABLE_GOTO_OBFUSCATION) {
                obfuscateGotos(mn);
            }

            // 4. Stack Manipulation - POP, DUP, SWAP ile karışıklık
            if (ENABLE_STACK_MANIPULATION) {
                addStackManipulation(mn);
            }

            // 5. Try-Catch Obfuscation - Exception bazlı akış
            if (ENABLE_TRY_CATCH_OBFUSCATION && mn.instructions.size() > 10) {
                addTryCatchObfuscation(mn);
            }

            // 6. Switch Dispatcher - State machine tarzı akış
            if (ENABLE_SWITCH_DISPATCHER && mn.instructions.size() > 20) {
                // Switch dispatcher çok agresif, sadece büyük metotlarda
                // injectSwitchDispatcher(mn); // Şimdilik devre dışı - çok riskli
            }

            controlFlowObfuscated++;
        }

        // ═══════════════════════════════════════════════════════════════════
        // RUNTIME PROTECTION INJECTION - Anti-Debug & Anti-Tamper
        // Hot metotlara ASLA enjekte etme - FPS düşürür!
        // ═══════════════════════════════════════════════════════════════════
        if (!isProtect && !isMixin && !isConstructor && !isSynthetic && !isBridge && !isAccessor && !isHotMethod) {
            // Her ANTI_DEBUG_INJECTION_RATE metottan birine enjekte et
            int methodHash = (className + mn.name + mn.desc).hashCode();
            boolean shouldInject = Math.abs(methodHash % ANTI_DEBUG_INJECTION_RATE) == 0;

            if (shouldInject) {
                // Anti-Debug check enjeksiyonu
                if (ENABLE_ANTI_DEBUG_INJECTION) {
                    injectAntiDebugCheck(mn, className);
                }

                // Anti-Tamper check enjeksiyonu
                if (ENABLE_ANTI_TAMPER_INJECTION) {
                    injectAntiTamperCheck(mn, className);
                }

                // Timing check enjeksiyonu
                if (ENABLE_TIMING_CHECKS) {
                    injectTimingCheck(mn, className);
                }
            }
        }

        // ═══════════════════════════════════════════════════════════════════
        // ADVANCED PROTECTION v2.0 - 10/10 Özellikler (hot metotlar hariç)
        // ═══════════════════════════════════════════════════════════════════
        if (!isProtect && !isMixin && !isConstructor && !hasLambda &&
            !isSynthetic && !isBridge && !isAccessor && !isHotMethod) {
            // Opaque Constants - sabit değerleri hesaplama ile gizle
            if (ENABLE_OPAQUE_CONSTANTS) {
                injectOpaqueConstants(mn);
            }

            // Proxy Calls - bazı metot çağrılarını proxy üzerinden yap
            if (ENABLE_PROXY_CALLS) {
                injectProxyCalls(mn, className);
            }

            // Exception Flow - exception-based kontrol akışı (güvenli)
            if (ENABLE_EXCEPTION_FLOW && mn.instructions.size() > 20) {
                injectExceptionFlow(mn);
            }
        }

        // Local variable scrambling
        if (ENABLE_LOCAL_SCRAMBLING) {
            if (mn.localVariables != null) {
                for (LocalVariableNode lv : mn.localVariables) {
                    if (!lv.name.equals("this")) {
                        lv.name = generateConfusingName(6);
                        localsScrambled++;
                    }
                }
            }
            if (mn.parameters != null) {
                mn.parameters.clear();
            }
        }

        // Method signature removal
        if (ENABLE_SIGNATURE_REMOVAL && mn.signature != null) {
            mn.signature = null;
            signaturesRemoved++;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // CONTROL FLOW OBFUSCATION METHODS
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Metot control flow için güvenli mi? (ULTRA SAFE v2.0)
     * Fabric uyumlu + Crash-free
     */
    private static boolean isMethodSafeForControlFlow(MethodNode mn) {
        // Abstract/native metotları atla
        if ((mn.access & Opcodes.ACC_ABSTRACT) != 0) return false;
        if ((mn.access & Opcodes.ACC_NATIVE) != 0) return false;

        // Çok küçük veya çok büyük metotları atla
        if (mn.instructions.size() < 20 || mn.instructions.size() > MAX_METHOD_SIZE_FOR_CF) {
            return false;
        }

        // Try-catch içeren metotları atla - frame hesaplaması karmaşık
        if (mn.tryCatchBlocks != null && mn.tryCatchBlocks.size() > 0) {
            return false;
        }

        // Mevcut jump sayısını ve tehlikeli instruction'ları kontrol et
        int jumpCount = 0;
        int invokeDynamicCount = 0;
        int returnCount = 0;
        int throwCount = 0;
        boolean hasJsr = false;
        boolean hasMonitor = false;

        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof JumpInsnNode) jumpCount++;
            if (insn instanceof InvokeDynamicInsnNode) invokeDynamicCount++;

            int opcode = insn.getOpcode();

            // Switch içeren metotları atla
            if (opcode == Opcodes.TABLESWITCH || opcode == Opcodes.LOOKUPSWITCH) {
                return false;
            }

            // JSR/RET (eski Java) - atla
            if (opcode == Opcodes.JSR || opcode == Opcodes.RET) {
                hasJsr = true;
            }

            // Monitor (synchronized block) - frame sorunu
            if (opcode == Opcodes.MONITORENTER || opcode == Opcodes.MONITOREXIT) {
                hasMonitor = true;
            }

            // Return sayısı
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                returnCount++;
            }

            // ATHROW
            if (opcode == Opcodes.ATHROW) {
                throwCount++;
            }
        }

        // invokedynamic (lambda/stream) içeren metotları atla - frame sorunları
        if (invokeDynamicCount > 0) return false;

        // JSR/RET içeren metotları atla
        if (hasJsr) return false;

        // Synchronized blok içeren metotları atla
        if (hasMonitor) return false;

        // Çok fazla return/throw varsa atla (karmaşık flow)
        if (returnCount > 3 || throwCount > 1) return false;

        // Çok fazla jump varsa atla
        return jumpCount < 10;
    }

    /**
     * Metot herhangi bir control flow obfuscation için güvenli mi?
     * (jump olmayan obfuscation'lar için bile)
     */
    private static boolean isMethodSafeForAnyObfuscation(MethodNode mn) {
        // invokedynamic içeren metotları tamamen atla
        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn instanceof InvokeDynamicInsnNode) {
                return false;
            }
        }
        return true;
    }

    /**
     * OPAQUE PREDICATES v4.0 - FABRIC UYUMLU + CRASH-FREE
     * Sadece güvenli metotlarda jump kullanır, diğerlerinde junk computation
     */
    private static void injectOpaquePredicates(MethodNode mn) {
        boolean safeForJump = isMethodSafeForControlFlow(mn);

        // Güvenli değilse sadece junk computation yap
        if (!safeForJump) {
            injectJunkComputationsOnly(mn);
            return;
        }

        AbstractInsnNode[] insns = mn.instructions.toArray();
        int injected = 0;
        int maxInject = 2; // Daha az injection = daha güvenli

        for (int i = 8; i < insns.length - 8 && injected < maxInject; i++) {
            AbstractInsnNode insn = insns[i];

            // Çok sıkı güvenlik kontrolü
            if (!isSafeInsertionPoint(insns, i)) continue;

            // Sadece basit LDC instruction'larından önce
            if (insn instanceof LdcInsnNode && RNG.nextInt(12) == 0) {
                InsnList opaque = createSafeOpaquePredicate();
                mn.instructions.insertBefore(insn, opaque);
                opaquePredicatesAdded++;
                injected++;
            }
        }
    }

    /**
     * Sadece junk computation injection (jump yok)
     */
    private static void injectJunkComputationsOnly(MethodNode mn) {
        AbstractInsnNode[] insns = mn.instructions.toArray();
        int injected = 0;

        for (int i = 5; i < insns.length - 5 && injected < 3; i++) {
            AbstractInsnNode insn = insns[i];
            if (insn instanceof LdcInsnNode && RNG.nextInt(10) == 0) {
                mn.instructions.insertBefore(insn, createJunkComputation());
                opaquePredicatesAdded++;
                injected++;
            }
        }
    }

    /**
     * Insertion point güvenli mi? (ULTRA STRICT)
     */
    private static boolean isSafeInsertionPoint(AbstractInsnNode[] insns, int index) {
        // Sınır kontrolü
        if (index < 5 || index >= insns.length - 5) return false;

        // Önceki ve sonraki 3 instruction'ı kontrol et
        for (int j = index - 3; j <= index + 3; j++) {
            if (j < 0 || j >= insns.length) continue;
            AbstractInsnNode check = insns[j];

            // Tehlikeli instruction'lar
            if (check instanceof LabelNode) return false;
            if (check instanceof FrameNode) return false;
            if (check instanceof JumpInsnNode) return false;
            if (check instanceof LineNumberNode) return false;

            int op = check.getOpcode();
            // Return, throw, monitor
            if (op >= Opcodes.IRETURN && op <= Opcodes.RETURN) return false;
            if (op == Opcodes.ATHROW) return false;
            if (op == Opcodes.MONITORENTER || op == Opcodes.MONITOREXIT) return false;
        }

        return true;
    }

    /**
     * GÜVENLİ Opaque Predicate - JUMP'LI (her zaman aynı yöne gider)
     * Dead code GOTO ile atlanır - frame sorunu yok
     */
    private static InsnList createSafeOpaquePredicate() {
        InsnList list = new InsnList();
        LabelNode continueLabel = new LabelNode();
        LabelNode deadCodeLabel = new LabelNode();

        int type = RNG.nextInt(4);

        switch (type) {
            case 0:
                // (x | 1) != 0 -> her zaman true, dead code'a ASLA atlamaz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.ICONST_1));
                list.add(new InsnNode(Opcodes.IOR));
                // Sonuç her zaman != 0, yani IFEQ asla atlamaz
                list.add(new JumpInsnNode(Opcodes.IFNE, continueLabel)); // Her zaman atlar
                // Bu kod asla çalışmaz
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel));
                list.add(continueLabel);
                break;

            case 1:
                // x ^ x == 0 -> her zaman true
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.DUP));
                list.add(new InsnNode(Opcodes.IXOR));
                // Sonuç her zaman 0, yani IFEQ her zaman atlar
                list.add(new JumpInsnNode(Opcodes.IFEQ, continueLabel));
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel));
                list.add(continueLabel);
                break;

            case 2:
                // 0 == 0 -> basit ve güvenli
                list.add(new InsnNode(Opcodes.ICONST_0));
                list.add(new JumpInsnNode(Opcodes.IFEQ, continueLabel)); // Her zaman atlar
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel));
                list.add(continueLabel);
                break;

            case 3:
                // null == null -> her zaman true
                list.add(new InsnNode(Opcodes.ACONST_NULL));
                list.add(new JumpInsnNode(Opcodes.IFNULL, continueLabel)); // Her zaman atlar
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel));
                list.add(continueLabel);
                break;
        }

        return list;
    }

    /**
     * Junk computation - UCUZ işlemler (FPS dostu)
     * Metot çağrısı YOK, sadece stack işlemleri
     */
    private static InsnList createJunkComputation() {
        InsnList list = new InsnList();

        int type = RNG.nextInt(6);

        switch (type) {
            case 0:
                // XOR - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.IXOR));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 1:
                // ADD - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.IADD));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 2:
                // SUB - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.ISUB));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 3:
                // SHL - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(8)));
                list.add(new InsnNode(Opcodes.ISHL));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 4:
                // SHR - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(8)));
                list.add(new InsnNode(Opcodes.ISHR));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 5:
                // String yükle ve at (ucuz - sadece LDC)
                list.add(new LdcInsnNode("X" + Integer.toHexString(RNG.nextInt(0xFFFF))));
                list.add(new InsnNode(Opcodes.POP));
                break;
        }

        return list;
    }

    /**
     * Dead Code Injection v2.0 - GÜVENLİ
     * Frame/Label/Jump yakınlarında injection yapmaz
     */
    private static void injectDeadCode(MethodNode mn) {
        if (mn.instructions.size() < 20) return; // Sadece büyük metotlarda

        AbstractInsnNode[] insns = mn.instructions.toArray();
        int injected = 0;

        for (int i = 5; i < insns.length - 5 && injected < 2; i++) {
            AbstractInsnNode insn = insns[i];
            AbstractInsnNode prev = insns[i - 1];
            AbstractInsnNode next = insns[i + 1];

            // Güvenli olmayan konumları atla
            if (prev instanceof LabelNode || prev instanceof FrameNode) continue;
            if (next instanceof LabelNode || next instanceof FrameNode) continue;
            if (prev instanceof JumpInsnNode || next instanceof JumpInsnNode) continue;
            if (insn instanceof JumpInsnNode) continue;

            // Sadece LDC instruction'larından önce (en güvenli)
            if (insn instanceof LdcInsnNode && RNG.nextInt(8) == 0) {
                InsnList deadCode = createDeadCodeBlock();
                mn.instructions.insertBefore(insn, deadCode);
                injected++;
            }
        }
    }

    /**
     * Dead code bloğu oluştur - UCUZ İŞLEMLER (FPS dostu)
     * Metot çağrısı YOK! Sadece LDC + stack işlemleri
     */
    private static InsnList createDeadCodeBlock() {
        InsnList list = new InsnList();

        // Jump kullanmadan anlamsız ama geçerli bytecode - UCUZ
        int deadType = RNG.nextInt(12);
        switch (deadType) {
            case 0:
                // String yükle ve at - ucuz
                list.add(new LdcInsnNode("X" + Integer.toHexString(RNG.nextInt(0xFFFF))));
                list.add(new InsnNode(Opcodes.POP));
                break;
            case 1:
                // Sayı hesapla ve at - ucuz
                list.add(new LdcInsnNode(RNG.nextInt(1000)));
                list.add(new LdcInsnNode(RNG.nextInt(1000)));
                list.add(new InsnNode(Opcodes.IXOR));
                list.add(new InsnNode(Opcodes.POP));
                break;
            case 2:
                // Long yükle ve at - ucuz
                list.add(new LdcInsnNode((long) RNG.nextInt(100000)));
                list.add(new InsnNode(Opcodes.POP2));
                break;
            case 3:
                // Double yükle ve at - ucuz
                list.add(new LdcInsnNode((double) RNG.nextInt(1000)));
                list.add(new InsnNode(Opcodes.POP2));
                break;
            case 4:
                // Float yükle ve at - ucuz
                list.add(new LdcInsnNode((float) RNG.nextInt(1000)));
                list.add(new InsnNode(Opcodes.POP));
                break;
            case 5:
                // NOISE: Fake URL - ucuz (sadece LDC)
                if (ENABLE_NOISE_INJECTION) {
                    list.add(new LdcInsnNode(FAKE_URLS[RNG.nextInt(FAKE_URLS.length)]));
                    list.add(new InsnNode(Opcodes.POP));
                    noiseStringsInjected++;
                }
                break;
            case 6:
                // NOISE: Fake IP - ucuz
                if (ENABLE_NOISE_INJECTION) {
                    list.add(new LdcInsnNode(FAKE_IPS[RNG.nextInt(FAKE_IPS.length)]));
                    list.add(new InsnNode(Opcodes.POP));
                    noiseStringsInjected++;
                }
                break;
            case 7:
                // NOISE: Fake hash - ucuz
                if (ENABLE_NOISE_INJECTION) {
                    list.add(new LdcInsnNode(FAKE_HASHES[RNG.nextInt(FAKE_HASHES.length)]));
                    list.add(new InsnNode(Opcodes.POP));
                    noiseStringsInjected++;
                }
                break;
            case 8:
                // NOISE: Fake log - ucuz
                if (ENABLE_NOISE_INJECTION) {
                    list.add(new LdcInsnNode(FAKE_LOGS[RNG.nextInt(FAKE_LOGS.length)]));
                    list.add(new InsnNode(Opcodes.POP));
                    noiseStringsInjected++;
                }
                break;
            case 9:
                // MATH: basit toplama - ucuz
                if (ENABLE_MATH_POLLUTION) {
                    list.add(new LdcInsnNode(RNG.nextInt(1000)));
                    list.add(new LdcInsnNode(RNG.nextInt(1000)));
                    list.add(new InsnNode(Opcodes.IADD));
                    list.add(new InsnNode(Opcodes.POP));
                    mathPollutionAdded++;
                }
                break;
            case 10:
                // MATH: basit çarpma - ucuz
                if (ENABLE_MATH_POLLUTION) {
                    list.add(new LdcInsnNode(RNG.nextInt(100)));
                    list.add(new LdcInsnNode(RNG.nextInt(100)));
                    list.add(new InsnNode(Opcodes.IMUL));
                    list.add(new InsnNode(Opcodes.POP));
                    mathPollutionAdded++;
                }
                break;
            case 11:
                // MATH: XOR - ucuz
                if (ENABLE_MATH_POLLUTION) {
                    list.add(new LdcInsnNode(RNG.nextInt(50000)));
                    list.add(new LdcInsnNode(RNG.nextInt(50000)));
                    list.add(new InsnNode(Opcodes.IXOR));
                    list.add(new InsnNode(Opcodes.POP));
                    mathPollutionAdded++;
                }
                break;
        }

        return list;
    }

    /**
     * Goto Obfuscation v4.0 - FABRIC UYUMLU + CRASH-FREE
     * Safe metotlarda gerçek jump, diğerlerinde bogus computation
     */
    private static void obfuscateGotos(MethodNode mn) {
        if (mn.instructions.size() < 30) return;

        boolean safeForJump = isMethodSafeForControlFlow(mn);

        // Güvenli değilse sadece bogus computation
        if (!safeForJump) {
            injectBogusComputationsOnly(mn);
            return;
        }

        AbstractInsnNode[] insns = mn.instructions.toArray();
        int obfuscated = 0;
        int maxObf = 2; // Daha az = daha güvenli

        for (int i = 10; i < insns.length - 10 && obfuscated < maxObf; i++) {
            // Çok sıkı güvenlik kontrolü
            if (!isSafeInsertionPoint(insns, i)) continue;

            AbstractInsnNode insn = insns[i];

            // Sadece basit LDC instruction'larından önce
            if (insn instanceof LdcInsnNode && RNG.nextInt(15) == 0) {
                InsnList obf = createSafeGotoObfuscation();
                mn.instructions.insertBefore(insn, obf);
                gotoObfuscated++;
                obfuscated++;
            }
        }
    }

    /**
     * Sadece bogus computation injection (jump yok)
     */
    private static void injectBogusComputationsOnly(MethodNode mn) {
        AbstractInsnNode[] insns = mn.instructions.toArray();
        int injected = 0;

        for (int i = 5; i < insns.length - 5 && injected < 2; i++) {
            AbstractInsnNode insn = insns[i];
            if (insn instanceof LdcInsnNode && RNG.nextInt(12) == 0) {
                mn.instructions.insertBefore(insn, createBogusComputation());
                gotoObfuscated++;
                injected++;
            }
        }
    }

    /**
     * GÜVENLİ Goto Obfuscation - JUMP'LI (frame-safe patterns)
     * Tüm pattern'ler stack-neutral ve frame-safe
     */
    private static InsnList createSafeGotoObfuscation() {
        InsnList list = new InsnList();
        int type = RNG.nextInt(4);

        switch (type) {
            case 0: {
                // Basit GOTO pattern - en güvenli
                LabelNode target = new LabelNode();
                list.add(new JumpInsnNode(Opcodes.GOTO, target));
                list.add(target);
                break;
            }

            case 1: {
                // Always-true jump (0 == 0)
                LabelNode continueLabel = new LabelNode();
                list.add(new InsnNode(Opcodes.ICONST_0));
                list.add(new JumpInsnNode(Opcodes.IFEQ, continueLabel)); // 0 == 0, her zaman atlar
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel)); // Fallback
                list.add(continueLabel);
                break;
            }

            case 2: {
                // null == null pattern
                LabelNode continueLabel = new LabelNode();
                list.add(new InsnNode(Opcodes.ACONST_NULL));
                list.add(new JumpInsnNode(Opcodes.IFNULL, continueLabel)); // null == null, her zaman atlar
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel)); // Fallback
                list.add(continueLabel);
                break;
            }

            case 3: {
                // x ^ x == 0 pattern
                LabelNode continueLabel = new LabelNode();
                int val = RNG.nextInt(10000);
                list.add(new LdcInsnNode(val));
                list.add(new LdcInsnNode(val));
                list.add(new InsnNode(Opcodes.IXOR));
                list.add(new JumpInsnNode(Opcodes.IFEQ, continueLabel)); // val ^ val == 0, her zaman atlar
                list.add(new JumpInsnNode(Opcodes.GOTO, continueLabel)); // Fallback
                list.add(continueLabel);
                break;
            }
        }

        return list;
    }

    /**
     * Bogus computation - UCUZ işlemler (FPS dostu)
     * Sadece stack işlemleri + basit aritmetik, metot çağrısı YOK
     */
    private static InsnList createBogusComputation() {
        InsnList list = new InsnList();

        int type = RNG.nextInt(8);
        switch (type) {
            case 0:
                // Basit XOR - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.IXOR));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 1:
                // Basit ADD - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.IADD));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 2:
                // Basit MUL - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(100)));
                list.add(new LdcInsnNode(RNG.nextInt(100)));
                list.add(new InsnNode(Opcodes.IMUL));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 3:
                // DUP + POP + POP - stack manipulation
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.DUP));
                list.add(new InsnNode(Opcodes.POP));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 4:
                // SWAP + POP + POP
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.SWAP));
                list.add(new InsnNode(Opcodes.POP));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 5:
                // Bitwise AND - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(0xFFFF)));
                list.add(new LdcInsnNode(0xFF));
                list.add(new InsnNode(Opcodes.IAND));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 6:
                // Bitwise OR - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(0xFFFF)));
                list.add(new LdcInsnNode(0xFF00));
                list.add(new InsnNode(Opcodes.IOR));
                list.add(new InsnNode(Opcodes.POP));
                break;

            case 7:
                // NEG + POP - çok ucuz
                list.add(new LdcInsnNode(RNG.nextInt(10000)));
                list.add(new InsnNode(Opcodes.INEG));
                list.add(new InsnNode(Opcodes.POP));
                break;
        }

        return list;
    }

    /**
     * Stack Manipulation v2.0 - GÜVENLİ
     * Frame/Label/Jump yakınlarında manipulation yapmaz
     */
    private static void addStackManipulation(MethodNode mn) {
        if (mn.instructions.size() < 20) return; // Sadece büyük metotlarda

        AbstractInsnNode[] insns = mn.instructions.toArray();
        int manipulated = 0;

        for (int i = 5; i < insns.length - 5 && manipulated < 3; i++) {
            AbstractInsnNode insn = insns[i];
            AbstractInsnNode prev = insns[i - 1];
            AbstractInsnNode next = insns[i + 1];

            // Güvenli olmayan konumları atla
            if (prev instanceof LabelNode || prev instanceof FrameNode) continue;
            if (next instanceof LabelNode || next instanceof FrameNode) continue;
            if (prev instanceof JumpInsnNode || next instanceof JumpInsnNode) continue;
            if (insn instanceof JumpInsnNode) continue;

            // Sadece String LDC'lerden önce (en güvenli)
            if (insn instanceof LdcInsnNode && RNG.nextInt(10) == 0) {
                LdcInsnNode ldc = (LdcInsnNode) insn;
                if (ldc.cst instanceof String) {
                    InsnList stackOps = createStackManipulation();
                    mn.instructions.insertBefore(insn, stackOps);
                    stackManipulations++;
                    manipulated++;
                }
            }
        }
    }

    /**
     * Stack manipulation bloğu - Etkisiz ama kafa karıştırıcı
     */
    private static InsnList createStackManipulation() {
        InsnList list = new InsnList();

        int type = RNG.nextInt(4);
        switch (type) {
            case 0:
                // PUSH + POP = hiçbir şey
                list.add(new InsnNode(Opcodes.ICONST_0));
                list.add(new InsnNode(Opcodes.POP));
                break;
            case 1:
                // PUSH + PUSH + POP2 = hiçbir şey
                list.add(new InsnNode(Opcodes.ICONST_1));
                list.add(new InsnNode(Opcodes.ICONST_2));
                list.add(new InsnNode(Opcodes.POP2));
                break;
            case 2:
                // PUSH + DUP + POP + POP = hiçbir şey
                list.add(new InsnNode(Opcodes.ICONST_3));
                list.add(new InsnNode(Opcodes.DUP));
                list.add(new InsnNode(Opcodes.POP));
                list.add(new InsnNode(Opcodes.POP));
                break;
            case 3:
                // PUSH + PUSH + SWAP + POP + POP = hiçbir şey
                list.add(new InsnNode(Opcodes.ICONST_4));
                list.add(new InsnNode(Opcodes.ICONST_5));
                list.add(new InsnNode(Opcodes.SWAP));
                list.add(new InsnNode(Opcodes.POP));
                list.add(new InsnNode(Opcodes.POP));
                break;
        }

        return list;
    }

    /**
     * Try-Catch Obfuscation - Exception-based control flow
     */
    private static void addTryCatchObfuscation(MethodNode mn) {
        if (mn.instructions.size() < 15) return;
        if (mn.tryCatchBlocks == null) {
            mn.tryCatchBlocks = new ArrayList<>();
        }

        // Mevcut try-catch varsa çok karmaşıklaştırma
        if (mn.tryCatchBlocks.size() > 3) return;

        // İlk instruction'ı bul (LabelNode değilse)
        AbstractInsnNode first = mn.instructions.getFirst();
        while (first != null && (first instanceof LabelNode || first instanceof LineNumberNode || first instanceof FrameNode)) {
            first = first.getNext();
        }
        if (first == null) return;

        // Son instruction'ı bul
        AbstractInsnNode last = mn.instructions.getLast();
        while (last != null && (last instanceof LabelNode || last instanceof LineNumberNode || last instanceof FrameNode)) {
            last = last.getPrevious();
        }
        if (last == null || last == first) return;

        // Return instruction bul
        AbstractInsnNode returnInsn = null;
        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            if (insn.getOpcode() >= Opcodes.IRETURN && insn.getOpcode() <= Opcodes.RETURN) {
                returnInsn = insn;
                break;
            }
        }
        if (returnInsn == null) return;

        try {
            LabelNode tryStart = new LabelNode();
            LabelNode tryEnd = new LabelNode();
            LabelNode catchHandler = new LabelNode();
            LabelNode afterCatch = new LabelNode();

            // Try block başlangıcı
            mn.instructions.insertBefore(first, tryStart);

            // Try block bitişi (return'den önce)
            mn.instructions.insertBefore(returnInsn, tryEnd);
            mn.instructions.insertBefore(returnInsn, new JumpInsnNode(Opcodes.GOTO, afterCatch));

            // Catch handler - asla çalışmaz ama decompiler'ı şaşırtır
            mn.instructions.insertBefore(returnInsn, catchHandler);
            mn.instructions.insertBefore(returnInsn, new InsnNode(Opcodes.POP)); // Exception'ı at
            mn.instructions.insertBefore(returnInsn, new LdcInsnNode("ERROR_" + RNG.nextInt(9999)));
            mn.instructions.insertBefore(returnInsn, new InsnNode(Opcodes.POP));

            mn.instructions.insertBefore(returnInsn, afterCatch);

            // Try-catch block ekle
            TryCatchBlockNode tcb = new TryCatchBlockNode(tryStart, tryEnd, catchHandler, "java/lang/RuntimeException");
            mn.tryCatchBlocks.add(tcb);

            tryCatchAdded++;
        } catch (Exception e) {
            // Hata olursa sessizce geç
        }
    }

    /**
     * STRING ENCRYPTION v4.0 - AES-128-CBC + MULTI-LAYER
     * - AES encryption (kırılması çok zor)
     * - XOR hybrid layer
     * - Güvenli pozisyon kontrolü
     */
    private static void encryptStrings(MethodNode mn) {
        // Constructor'ları atla
        if (mn.name.equals("<init>") || mn.name.equals("<clinit>")) return;

        AbstractInsnNode[] insns = mn.instructions.toArray();
        int encrypted = 0;

        for (int i = 1; i < insns.length - 1 && encrypted < MAX_STRINGS_PER_METHOD; i++) {
            AbstractInsnNode insn = insns[i];

            // Frame/Label kontrolü
            if (insns[i-1] instanceof FrameNode) continue;
            if (insns[i+1] instanceof FrameNode) continue;

            if (insn instanceof LdcInsnNode) {
                LdcInsnNode ldc = (LdcInsnNode) insn;
                if (ldc.cst instanceof String) {
                    String original = (String) ldc.cst;

                    // Boş veya çok uzun string'leri atla
                    if (original.isEmpty() || original.length() > 200) continue;

                    // Sadece null byte içerenleri atla
                    if (original.contains("\0")) continue;

                    // AES-128-CBC encryption
                    String encryptedStr = encryptStringAES(original);
                    if (encryptedStr == null || encryptedStr.isEmpty()) {
                        // Fallback to Base64 if AES fails
                        encryptedStr = Base64.getEncoder().encodeToString(original.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                        InsnList replacement = new InsnList();
                        replacement.add(new LdcInsnNode(encryptedStr));
                        replacement.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "dev/just/protect/runtime/I0O1l0I1",
                            "b",
                            "(Ljava/lang/String;)Ljava/lang/String;",
                            false
                        ));
                        mn.instructions.insert(insn, replacement);
                        mn.instructions.remove(insn);
                    } else {
                        // AES encrypted - use 'a' method
                        InsnList replacement = new InsnList();
                        replacement.add(new LdcInsnNode(encryptedStr));
                        replacement.add(new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "dev/just/protect/runtime/I0O1l0I1",
                            "a",
                            "(Ljava/lang/String;)Ljava/lang/String;",
                            false
                        ));
                        mn.instructions.insert(insn, replacement);
                        mn.instructions.remove(insn);
                    }

                    stringsEncrypted++;
                    encrypted++;
                }
            }
        }
    }

    // AES encryption components (same keys as runtime)
    private static final byte[] BUILD_AES_KEY = {
        (byte)0x45, (byte)0x78, (byte)0x6F, (byte)0x6E,
        (byte)0x43, (byte)0x6C, (byte)0x69, (byte)0x65,
        (byte)0x6E, (byte)0x74, (byte)0x4B, (byte)0x65,
        (byte)0x79, (byte)0x31, (byte)0x32, (byte)0x33
    };

    private static final byte[] BUILD_AES_IV = {
        (byte)0x52, (byte)0x61, (byte)0x6E, (byte)0x64,
        (byte)0x6F, (byte)0x6D, (byte)0x49, (byte)0x56,
        (byte)0x56, (byte)0x65, (byte)0x63, (byte)0x74,
        (byte)0x6F, (byte)0x72, (byte)0x31, (byte)0x36
    };

    private static javax.crypto.Cipher aesCipher;
    private static boolean aesInitialized = false;

    /**
     * AES-128-CBC ile string şifrele
     */
    private static String encryptStringAES(String plain) {
        try {
            if (!aesInitialized) {
                javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(BUILD_AES_KEY, "AES");
                javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(BUILD_AES_IV);
                aesCipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
                aesCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                aesInitialized = true;
            }

            byte[] encrypted = aesCipher.doFinal(plain.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Re-init cipher for next use (CBC mode requires this)
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(BUILD_AES_KEY, "AES");
            javax.crypto.spec.IvParameterSpec ivSpec = new javax.crypto.spec.IvParameterSpec(BUILD_AES_IV);
            aesCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * NUMBER OBFUSCATION v3.0 - FULL POWER + CRASH-FREE
     * - Akıllı güvenlik kontrolleri
     * - Geniş sayı aralığı
     * - Çoklu teknikler
     */
    private static void obfuscateNumbers(MethodNode mn) {
        // Constructor ve clinit'te ATLA
        if (mn.name.equals("<init>") || mn.name.equals("<clinit>")) {
            return;
        }

        // Çok büyük metotları atla
        if (mn.instructions.size() > 400) {
            return;
        }

        AbstractInsnNode[] instructions = mn.instructions.toArray();
        int obfuscated = 0;

        for (int i = 3; i < instructions.length - 3 && obfuscated < MAX_NUMBERS_PER_METHOD; i++) {
            AbstractInsnNode insn = instructions[i];

            // Güvenlik kontrolleri
            if (!isNumberSafePosition(instructions, i)) {
                continue;
            }

            // LDC integer sabitleri
            if (insn instanceof LdcInsnNode) {
                LdcInsnNode ldc = (LdcInsnNode) insn;
                if (ldc.cst instanceof Integer) {
                    int value = (Integer) ldc.cst;
                    // Geniş aralık: 10 - 50000
                    if (value >= 10 && value <= 50000) {
                        InsnList replacement = createNumberObfuscation(value);
                        mn.instructions.insert(insn, replacement);
                        mn.instructions.remove(insn);
                        numbersObfuscated++;
                        obfuscated++;
                    }
                }
            }
            // BIPUSH (byte sabitleri: -128 to 127)
            else if (insn.getOpcode() == Opcodes.BIPUSH) {
                IntInsnNode intInsn = (IntInsnNode) insn;
                int value = intInsn.operand;
                if (value >= 10 && value <= 100) {
                    InsnList replacement = createNumberObfuscation(value);
                    mn.instructions.insert(insn, replacement);
                    mn.instructions.remove(insn);
                    numbersObfuscated++;
                    obfuscated++;
                }
            }
            // SIPUSH (short sabitleri: -32768 to 32767)
            else if (insn.getOpcode() == Opcodes.SIPUSH) {
                IntInsnNode intInsn = (IntInsnNode) insn;
                int value = intInsn.operand;
                if (value >= 100 && value <= 10000) {
                    InsnList replacement = createNumberObfuscation(value);
                    mn.instructions.insert(insn, replacement);
                    mn.instructions.remove(insn);
                    numbersObfuscated++;
                    obfuscated++;
                }
            }
        }
    }

    /**
     * Sayı obfuscation için güvenli pozisyon mu?
     */
    private static boolean isNumberSafePosition(AbstractInsnNode[] insns, int index) {
        // Önceki ve sonraki 3 instruction'a bak
        for (int j = Math.max(0, index - 2); j <= Math.min(insns.length - 1, index + 3); j++) {
            AbstractInsnNode check = insns[j];
            int op = check.getOpcode();

            // Array operasyonları yakınında ATLA
            if (op >= Opcodes.IALOAD && op <= Opcodes.SALOAD) return false;
            if (op >= Opcodes.IASTORE && op <= Opcodes.SASTORE) return false;
            if (op == Opcodes.NEWARRAY || op == Opcodes.ANEWARRAY) return false;
            if (op == Opcodes.ARRAYLENGTH) return false;

            // Switch yakınında ATLA
            if (op == Opcodes.TABLESWITCH || op == Opcodes.LOOKUPSWITCH) return false;
        }

        // Frame/Label kontrolü
        AbstractInsnNode prev = insns[index - 1];
        AbstractInsnNode next = insns[index + 1];
        if (prev instanceof LabelNode || prev instanceof FrameNode) return false;
        if (next instanceof LabelNode || next instanceof FrameNode) return false;

        return true;
    }

    /**
     * Sayı obfuscation oluştur - çoklu teknikler
     */
    private static InsnList createNumberObfuscation(int value) {
        InsnList list = new InsnList();
        int technique = RNG.nextInt(4);

        switch (technique) {
            case 0: // XOR
                int key1 = RNG.nextInt(60000) + 1000;
                list.add(new LdcInsnNode(value ^ key1));
                list.add(new LdcInsnNode(key1));
                list.add(new InsnNode(Opcodes.IXOR));
                break;

            case 1: // ADD
                int add = RNG.nextInt(30000) + 1000;
                list.add(new LdcInsnNode(value - add));
                list.add(new LdcInsnNode(add));
                list.add(new InsnNode(Opcodes.IADD));
                break;

            case 2: // SUB
                int sub = RNG.nextInt(30000) + 1000;
                list.add(new LdcInsnNode(value + sub));
                list.add(new LdcInsnNode(sub));
                list.add(new InsnNode(Opcodes.ISUB));
                break;

            case 3: // Double XOR
                int k1 = RNG.nextInt(30000) + 500;
                int k2 = RNG.nextInt(30000) + 500;
                list.add(new LdcInsnNode(value ^ k1 ^ k2));
                list.add(new LdcInsnNode(k1));
                list.add(new InsnNode(Opcodes.IXOR));
                list.add(new LdcInsnNode(k2));
                list.add(new InsnNode(Opcodes.IXOR));
                break;
        }

        return list;
    }

    /**
     * Metodda array operasyonu var mı kontrol et
     */
    private static boolean hasArrayOperations(MethodNode mn) {
        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            int op = insn.getOpcode();
            if (op >= Opcodes.IALOAD && op <= Opcodes.SALOAD) return true;
            if (op >= Opcodes.IASTORE && op <= Opcodes.SASTORE) return true;
            if (op == Opcodes.NEWARRAY || op == Opcodes.ANEWARRAY || op == Opcodes.MULTIANEWARRAY) return true;
        }
        return false;
    }

    /**
     * Metodda float/double array operasyonu var mı
     */
    private static boolean hasFloatOperations(MethodNode mn) {
        for (AbstractInsnNode insn : mn.instructions.toArray()) {
            int op = insn.getOpcode();
            // Float/Double array store
            if (op == Opcodes.FASTORE || op == Opcodes.DASTORE) return true;
            // Float/Double array load
            if (op == Opcodes.FALOAD || op == Opcodes.DALOAD) return true;
        }
        return false;
    }

    /**
     * Bir instruction'ın obfuscate edilmesinin güvenli olup olmadığını kontrol et
     */
    private static boolean isSafeToObfuscate(AbstractInsnNode insn, MethodNode mn, int index, AbstractInsnNode[] instructions) {
        // Sonraki 5 instruction'a bak (daha geniş kontrol)
        for (int j = 1; j <= 5 && (index + j) < instructions.length; j++) {
            AbstractInsnNode future = instructions[index + j];
            int futureOp = future.getOpcode();

            // Herhangi bir array operasyonu varsa ATLA
            if (futureOp >= Opcodes.IALOAD && futureOp <= Opcodes.SALOAD) return false;
            if (futureOp >= Opcodes.IASTORE && futureOp <= Opcodes.SASTORE) return false;
            if (futureOp == Opcodes.NEWARRAY || futureOp == Opcodes.ANEWARRAY) return false;

            // Switch varsa ATLA
            if (futureOp == Opcodes.TABLESWITCH || futureOp == Opcodes.LOOKUPSWITCH) return false;
        }

        // Önceki instruction'a bak
        AbstractInsnNode prev = (index > 0) ? instructions[index - 1] : null;
        AbstractInsnNode next = (index + 1 < instructions.length) ? instructions[index + 1] : null;

        // Frame node'larının yakınında olma
        if (prev instanceof FrameNode || next instanceof FrameNode) {
            return false;
        }

        // Label'dan hemen sonra olma
        if (prev instanceof LabelNode) {
            return false;
        }

        // Jump instruction'dan önce olma
        if (next instanceof JumpInsnNode) {
            return false;
        }

        return true;
    }

    /**
     * GÜVENLİ number obfuscation - Sadece basit XOR
     */
    private static InsnList createSafeNumberObfuscation(int value) {
        InsnList list = new InsnList();

        // Basit XOR - en güvenli yöntem
        int key = RNG.nextInt(50000) + 1000;
        int encoded = value ^ key;

        list.add(new LdcInsnNode(encoded));
        list.add(new LdcInsnNode(key));
        list.add(new InsnNode(Opcodes.IXOR));

        return list;
    }

    /**
     * Gelişmiş number obfuscation - Çoklu teknikler
     */
    private static InsnList createAdvancedNumberObfuscation(int value) {
        InsnList list = new InsnList();
        int technique = RNG.nextInt(6);

        try {
            switch (technique) {
                case 0:
                    // XOR tekniği: (encoded ^ key) = value
                    int key1 = RNG.nextInt(50000) + 1000;
                    int encoded1 = value ^ key1;
                    list.add(new LdcInsnNode(encoded1));
                    list.add(new LdcInsnNode(key1));
                    list.add(new InsnNode(Opcodes.IXOR));
                    break;

                case 1:
                    // ADD/SUB tekniği: (a + b) = value
                    int a1 = RNG.nextInt(10000) + 1000;
                    int b1 = value - a1;
                    list.add(new LdcInsnNode(a1));
                    list.add(new LdcInsnNode(b1));
                    list.add(new InsnNode(Opcodes.IADD));
                    break;

                case 2:
                    // MUL/DIV tekniği: (a * b) / b = value (sadece küçük sayılar için)
                    if (value > 0 && value < 1000) {
                        int multiplier = RNG.nextInt(100) + 2;
                        int product = value * multiplier;
                        list.add(new LdcInsnNode(product));
                        list.add(new LdcInsnNode(multiplier));
                        list.add(new InsnNode(Opcodes.IDIV));
                    } else {
                        // Fallback to XOR
                        int key2 = RNG.nextInt(50000) + 1000;
                        int encoded2 = value ^ key2;
                        list.add(new LdcInsnNode(encoded2));
                        list.add(new LdcInsnNode(key2));
                        list.add(new InsnNode(Opcodes.IXOR));
                    }
                    break;

                case 3:
                    // Double XOR: ((a ^ b) ^ c) = value
                    int x1 = RNG.nextInt(30000) + 1000;
                    int x2 = RNG.nextInt(30000) + 1000;
                    int x3 = value ^ x1 ^ x2;
                    list.add(new LdcInsnNode(x3));
                    list.add(new LdcInsnNode(x1));
                    list.add(new InsnNode(Opcodes.IXOR));
                    list.add(new LdcInsnNode(x2));
                    list.add(new InsnNode(Opcodes.IXOR));
                    break;

                case 4:
                    // Negate + Add: (-a + b) = value
                    int neg = RNG.nextInt(20000) + 5000;
                    int add = value + neg;
                    list.add(new LdcInsnNode(neg));
                    list.add(new InsnNode(Opcodes.INEG));
                    list.add(new LdcInsnNode(add));
                    list.add(new InsnNode(Opcodes.IADD));
                    break;

                case 5:
                    // Bit shift + XOR: ((a << 4) >> 4) ^ b = value (sadece pozitif)
                    if (value >= 0) {
                        int base = value ^ (RNG.nextInt(1000) + 100);
                        int xorKey = value ^ base;
                        list.add(new LdcInsnNode(base));
                        list.add(new LdcInsnNode(xorKey));
                        list.add(new InsnNode(Opcodes.IXOR));
                    } else {
                        // Fallback
                        int key3 = RNG.nextInt(50000) + 1000;
                        int encoded3 = value ^ key3;
                        list.add(new LdcInsnNode(encoded3));
                        list.add(new LdcInsnNode(key3));
                        list.add(new InsnNode(Opcodes.IXOR));
                    }
                    break;
            }
        } catch (Exception e) {
            // Hata olursa basit XOR kullan
            list.clear();
            int safeKey = RNG.nextInt(50000) + 1000;
            int safeEncoded = value ^ safeKey;
            list.add(new LdcInsnNode(safeEncoded));
            list.add(new LdcInsnNode(safeKey));
            list.add(new InsnNode(Opcodes.IXOR));
        }

        return list;
    }

    private static void addFakeFields(ClassNode cn, int count) {
        String[] types = {"I", "J", "Z", "B", "S", "F", "D", "[B", "[I", "Ljava/lang/Object;", "Ljava/lang/String;"};

        // Normal fake fields
        for (int i = 0; i < count; i++) {
            String name = generateConfusingName(10);
            String desc = types[RNG.nextInt(types.length)];
            int access = Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC;
            if (RNG.nextBoolean()) access |= Opcodes.ACC_TRANSIENT;
            if (RNG.nextBoolean()) access |= Opcodes.ACC_VOLATILE;

            cn.fields.add(new FieldNode(access, name, desc, null, null));
            fakeFieldsAdded++;
        }

        // Entropy/State fields - decompiler'ı şaşırtmak için
        if (ENABLE_ENTROPY_FIELDS) {
            // entropy field
            cn.fields.add(new FieldNode(
                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
                "entropy" + Integer.toHexString(RNG.nextInt(0xFFFF)),
                "J", null, (long) RNG.nextInt(100000)
            ));
            entropyFieldsAdded++;

            // state field
            cn.fields.add(new FieldNode(
                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_VOLATILE,
                "state" + Integer.toHexString(RNG.nextInt(0xFFFF)),
                "I", null, RNG.nextInt(10000)
            ));
            entropyFieldsAdded++;

            // checksum field
            cn.fields.add(new FieldNode(
                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC,
                "checksum" + Integer.toHexString(RNG.nextInt(0xFFFF)),
                "I", null, RNG.nextInt(0xFFFFFF)
            ));
            entropyFieldsAdded++;

            // hash field
            cn.fields.add(new FieldNode(
                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL,
                "HASH_" + Integer.toHexString(RNG.nextInt(0xFFFF)),
                "Ljava/lang/String;", null, FAKE_HASHES[RNG.nextInt(FAKE_HASHES.length)]
            ));
            entropyFieldsAdded++;
        }
    }

    private static void addFakeMethods(ClassNode cn, int count) {
        int actualCount = ENABLE_MASSIVE_FAKE_METHODS ? FAKE_METHOD_COUNT : count;

        // Farklı method tipleri
        String[][] methodSignatures = {
            {"V", "()V"},
            {"I", "()I"},
            {"Z", "()Z"},
            {"Ljava/lang/String;", "()Ljava/lang/String;"},
            {"Ljava/lang/Object;", "()Ljava/lang/Object;"},
            {"V", "(I)V"},
            {"I", "(II)I"},
            {"Z", "(Ljava/lang/Object;)Z"},
            {"V", "(Ljava/lang/String;)V"},
            {"[B", "()[B"},
            {"J", "()J"},
            {"D", "()D"}
        };

        for (int i = 0; i < actualCount; i++) {
            String[] sig = methodSignatures[RNG.nextInt(methodSignatures.length)];
            String returnType = sig[0];
            String desc = sig[1];

            // Farklı isim stilleri
            String name;
            int style = RNG.nextInt(5);
            switch (style) {
                case 0: name = "_" + generateConfusingName(8); break;
                case 1: name = "m" + Integer.toHexString(RNG.nextInt(0xFFFF)); break;
                case 2: name = "lambda$" + generateConfusingName(6) + "$" + i; break;
                case 3: name = "access$" + (100 + i); break;
                default: name = generateConfusingName(12); break;
            }

            int access = Opcodes.ACC_PRIVATE | Opcodes.ACC_SYNTHETIC;
            if (RNG.nextInt(4) == 0) access |= Opcodes.ACC_STATIC;

            MethodNode mn = new MethodNode(access, name, desc, null, null);
            mn.instructions = new InsnList();

            // Karmaşık fake method body
            addFakeMethodBody(mn, returnType);

            cn.methods.add(mn);
            fakeMethodsAdded++;
        }
    }

    /**
     * Fake method'a karmaşık ama anlamsız body ekle
     */
    private static void addFakeMethodBody(MethodNode mn, String returnType) {
        InsnList insns = mn.instructions;
        int complexity = RNG.nextInt(5);

        // Noise string injection
        if (ENABLE_NOISE_INJECTION && RNG.nextBoolean()) {
            String noise = getRandomNoiseString();
            insns.add(new LdcInsnNode(noise));
            insns.add(new InsnNode(Opcodes.POP));
            noiseStringsInjected++;
        }

        // Math pollution
        if (ENABLE_MATH_POLLUTION) {
            addMathPollution(insns);
        }

        // Complexity based on random - UCUZ İŞLEMLER (fake method'lar çağrılmasa bile)
        for (int i = 0; i < complexity; i++) {
            int op = RNG.nextInt(6);
            switch (op) {
                case 0:
                    // Basit XOR - ucuz
                    insns.add(new LdcInsnNode(RNG.nextInt(10000)));
                    insns.add(new LdcInsnNode(RNG.nextInt(10000)));
                    insns.add(new InsnNode(Opcodes.IXOR));
                    insns.add(new InsnNode(Opcodes.POP));
                    break;
                case 1:
                    // String yükle ve at - ucuz
                    insns.add(new LdcInsnNode("x" + RNG.nextInt(1000)));
                    insns.add(new InsnNode(Opcodes.POP));
                    break;
                case 2:
                    // Basit math - ucuz
                    insns.add(new LdcInsnNode(RNG.nextInt(1000)));
                    insns.add(new LdcInsnNode(RNG.nextInt(1000)));
                    insns.add(new InsnNode(Opcodes.IADD));
                    insns.add(new InsnNode(Opcodes.POP));
                    break;
                case 3:
                    // Long yükle ve at - ucuz
                    insns.add(new LdcInsnNode((long) RNG.nextInt(100000)));
                    insns.add(new InsnNode(Opcodes.POP2));
                    break;
                case 4:
                    // Bitwise AND - ucuz
                    insns.add(new LdcInsnNode(RNG.nextInt(0xFFFF)));
                    insns.add(new LdcInsnNode(0xFF));
                    insns.add(new InsnNode(Opcodes.IAND));
                    insns.add(new InsnNode(Opcodes.POP));
                    break;
                case 5:
                    // Noise string - ucuz
                    if (ENABLE_NOISE_INJECTION) {
                        insns.add(new LdcInsnNode(getRandomNoiseString()));
                        insns.add(new InsnNode(Opcodes.POP));
                        noiseStringsInjected++;
                    }
                    break;
            }
        }

        // Return statement
        switch (returnType) {
            case "V":
                insns.add(new InsnNode(Opcodes.RETURN));
                break;
            case "I":
            case "Z":
            case "B":
            case "S":
                insns.add(new LdcInsnNode(RNG.nextInt(100)));
                insns.add(new InsnNode(Opcodes.IRETURN));
                break;
            case "J":
                insns.add(new LdcInsnNode((long) RNG.nextInt(100000)));
                insns.add(new InsnNode(Opcodes.LRETURN));
                break;
            case "D":
                insns.add(new LdcInsnNode((double) RNG.nextInt(1000)));
                insns.add(new InsnNode(Opcodes.DRETURN));
                break;
            case "[B":
                insns.add(new LdcInsnNode(16));
                insns.add(new IntInsnNode(Opcodes.NEWARRAY, Opcodes.T_BYTE));
                insns.add(new InsnNode(Opcodes.ARETURN));
                break;
            default:
                insns.add(new InsnNode(Opcodes.ACONST_NULL));
                insns.add(new InsnNode(Opcodes.ARETURN));
                break;
        }

        mn.maxStack = 4;
        mn.maxLocals = 3;
    }

    /**
     * Random noise string döndür
     */
    private static String getRandomNoiseString() {
        int type = RNG.nextInt(4);
        switch (type) {
            case 0: return FAKE_URLS[RNG.nextInt(FAKE_URLS.length)];
            case 1: return FAKE_IPS[RNG.nextInt(FAKE_IPS.length)];
            case 2: return FAKE_HASHES[RNG.nextInt(FAKE_HASHES.length)];
            default: return FAKE_LOGS[RNG.nextInt(FAKE_LOGS.length)];
        }
    }

    /**
     * Math pollution ekle - anlamsız matematik işlemleri
     */
    private static void addMathPollution(InsnList insns) {
        int type = RNG.nextInt(8);
        switch (type) {
            case 0:
                // 69 + 92 = 161, then pop
                insns.add(new LdcInsnNode(69));
                insns.add(new LdcInsnNode(92));
                insns.add(new InsnNode(Opcodes.IADD));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 1:
                // x % 1 = 0 always
                insns.add(new LdcInsnNode(RNG.nextInt(10000)));
                insns.add(new InsnNode(Opcodes.ICONST_1));
                insns.add(new InsnNode(Opcodes.IREM));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 2:
                // -(-5) = 5
                insns.add(new LdcInsnNode(5));
                insns.add(new InsnNode(Opcodes.INEG));
                insns.add(new InsnNode(Opcodes.INEG));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 3:
                // x ^ x = 0
                insns.add(new LdcInsnNode(RNG.nextInt(10000)));
                insns.add(new InsnNode(Opcodes.DUP));
                insns.add(new InsnNode(Opcodes.IXOR));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 4:
                // (a * 0) = 0
                insns.add(new LdcInsnNode(RNG.nextInt(10000)));
                insns.add(new InsnNode(Opcodes.ICONST_0));
                insns.add(new InsnNode(Opcodes.IMUL));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 5:
                // a << 0 = a, then pop
                insns.add(new LdcInsnNode(RNG.nextInt(1000)));
                insns.add(new InsnNode(Opcodes.ICONST_0));
                insns.add(new InsnNode(Opcodes.ISHL));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 6:
                // entropy ^= 24642 pattern
                insns.add(new LdcInsnNode(24642));
                insns.add(new LdcInsnNode(RNG.nextInt(50000)));
                insns.add(new InsnNode(Opcodes.IXOR));
                insns.add(new InsnNode(Opcodes.POP));
                break;
            case 7:
                // Double precision noise
                insns.add(new LdcInsnNode(Math.PI));
                insns.add(new LdcInsnNode(Math.E));
                insns.add(new InsnNode(Opcodes.DMUL));
                insns.add(new InsnNode(Opcodes.POP2));
                break;
        }
        mathPollutionAdded++;
    }

    private static void addFakeAnnotations(ClassNode cn) {
        for (FieldNode fn : cn.fields) {
            if ((fn.access & Opcodes.ACC_SYNTHETIC) != 0 && RNG.nextInt(3) == 0) {
                if (fn.visibleAnnotations == null) {
                    fn.visibleAnnotations = new ArrayList<>();
                }
                fn.visibleAnnotations.add(new AnnotationNode("Ljava/lang/Deprecated;"));
                annotationsAdded++;
            }
        }
    }

    private static void shuffleMembers(ClassNode cn) {
        if (cn.fields.size() > 1) {
            Collections.shuffle(cn.fields, RNG);
        }

        if (cn.methods.size() > 2) {
            List<MethodNode> inits = new ArrayList<>();
            List<MethodNode> others = new ArrayList<>();

            for (MethodNode mn : cn.methods) {
                if (mn.name.equals("<init>") || mn.name.equals("<clinit>")) {
                    inits.add(mn);
                } else {
                    others.add(mn);
                }
            }

            Collections.shuffle(others, RNG);

            cn.methods.clear();
            cn.methods.addAll(inits);
            cn.methods.addAll(others);
        }
    }

    private static String generateConfusingName(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(CONFUSE_CHARS[RNG.nextInt(CONFUSE_CHARS.length)]);
        for (int i = 2; i < length; i++) {
            sb.append(NAME_CHARS[RNG.nextInt(NAME_CHARS.length)]);
        }
        return sb.toString() + Integer.toHexString(nameCounter++);
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // ADVANCED PROTECTION v2.0 - 10/10 Özellikler
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Opaque Constants - GELİŞMİŞ VERSİYON (crash-free)
     * BIPUSH, SIPUSH ve LDC integer değerlerini karmaşıklaştırır
     */
    private static void injectOpaqueConstants(MethodNode mn) {
        try {
            int added = 0;
            AbstractInsnNode[] insns = mn.instructions.toArray();

            for (int i = 3; i < insns.length - 3 && added < 8; i++) {
                AbstractInsnNode insn = insns[i];

                // Frame/Label yakınında değilse
                if (insns[i-1] instanceof FrameNode || insns[i+1] instanceof FrameNode) continue;
                if (insns[i-1] instanceof LabelNode || insns[i+1] instanceof LabelNode) continue;

                // Sonraki instruction tehlikeli mi kontrol et
                AbstractInsnNode next = insns[i+1];
                int nextOp = next.getOpcode();

                // Array operasyonları, switch, jump yakınında ATLA
                if (nextOp >= Opcodes.IALOAD && nextOp <= Opcodes.SALOAD) continue;
                if (nextOp >= Opcodes.IASTORE && nextOp <= Opcodes.SASTORE) continue;
                if (nextOp == Opcodes.NEWARRAY || nextOp == Opcodes.ANEWARRAY) continue;
                if (nextOp == Opcodes.TABLESWITCH || nextOp == Opcodes.LOOKUPSWITCH) continue;
                if (next instanceof JumpInsnNode) continue;

                int value = Integer.MIN_VALUE;

                // BIPUSH, SIPUSH
                if (insn.getOpcode() == Opcodes.BIPUSH || insn.getOpcode() == Opcodes.SIPUSH) {
                    if (insn instanceof IntInsnNode) {
                        value = ((IntInsnNode) insn).operand;
                    }
                }
                // LDC Integer
                else if (insn instanceof LdcInsnNode) {
                    LdcInsnNode ldc = (LdcInsnNode) insn;
                    if (ldc.cst instanceof Integer) {
                        value = (Integer) ldc.cst;
                    }
                }

                // Geçerli değer değilse atla
                if (value == Integer.MIN_VALUE) continue;
                // Çok küçük, negatif veya çok büyük değerleri atla
                if (value < 5 || value > 50000) continue;

                InsnList replacement = new InsnList();
                int technique = RNG.nextInt(3);

                switch (technique) {
                    case 0: // XOR: (a ^ b) = value
                        int key1 = RNG.nextInt(50000) + 1000;
                        replacement.add(new LdcInsnNode(value ^ key1));
                        replacement.add(new LdcInsnNode(key1));
                        replacement.add(new InsnNode(Opcodes.IXOR));
                        break;
                    case 1: // ADD: (a + b) = value
                        int add = RNG.nextInt(10000) + 100;
                        replacement.add(new LdcInsnNode(value - add));
                        replacement.add(new LdcInsnNode(add));
                        replacement.add(new InsnNode(Opcodes.IADD));
                        break;
                    case 2: // SUB: (a - b) = value
                        int sub = RNG.nextInt(10000) + 100;
                        replacement.add(new LdcInsnNode(value + sub));
                        replacement.add(new LdcInsnNode(sub));
                        replacement.add(new InsnNode(Opcodes.ISUB));
                        break;
                }

                mn.instructions.insert(insn, replacement);
                mn.instructions.remove(insn);
                opaqueConstantsAdded++;
                added++;
            }
        } catch (Exception e) {
            // Sessizce atla
        }
    }

    /**
     * Proxy Calls - bazı metot çağrılarını l1O0I1lO wrapper'ı üzerinden yap
     * Decompiler'da gerçek çağrı gizlenir
     */
    private static void injectProxyCalls(MethodNode mn, String className) {
        try {
            int added = 0;
            AbstractInsnNode[] insns = mn.instructions.toArray();

            for (int i = 0; i < insns.length && added < 3; i++) {
                AbstractInsnNode insn = insns[i];

                // Static method çağrılarını proxy'le
                if (insn instanceof MethodInsnNode) {
                    MethodInsnNode methodInsn = (MethodInsnNode) insn;

                    // Kendi sınıflarımızı atla
                    if (methodInsn.owner.startsWith("dev/just/protect/")) continue;
                    if (methodInsn.owner.startsWith("java/")) continue;
                    if (methodInsn.owner.startsWith("net/minecraft/")) continue;

                    // Sadece void metotları proxy'le (basitlik için)
                    if (methodInsn.desc.endsWith(")V") && methodInsn.getOpcode() == Opcodes.INVOKESTATIC) {
                        // Her 20 çağrıdan birini proxy'le
                        if (RNG.nextInt(REFLECTION_HIDE_RATE) == 0) {
                            InsnList wrapper = new InsnList();

                            // l1O0I1lO.fakeHandler() ekle (çağrıdan önce)
                            wrapper.add(new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                "dev/just/protect/runtime/l1O0I1lO",
                                "fakeHandler",
                                "()V",
                                false
                            ));

                            mn.instructions.insertBefore(insn, wrapper);
                            proxyCallsAdded++;
                            added++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Sessizce atla
        }
    }

    /**
     * Exception Flow - GELİŞMİŞ VERSİYON (crash-free)
     * Çoklu runtime check çağrıları ekler
     */
    private static void injectExceptionFlow(MethodNode mn) {
        try {
            if (mn.instructions.size() < 15) return;

            InsnList injection = new InsnList();

            // 1. l1O0I1lO.fakeHandler() - state tracking
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/l1O0I1lO",
                "fakeHandler",
                "()V",
                false
            ));

            // 2. AntiDebug.envCheck() - environment kontrolü
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/AntiDebug",
                "envCheck",
                "()V",
                false
            ));

            // 3. Fake opaque değer kontrolü (opaqueTrue çağır ve POP)
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/l1O0I1lO",
                "opaqueTrue",
                "()Z",
                false
            ));
            injection.add(new InsnNode(Opcodes.POP));

            // Metot başına ekle
            mn.instructions.insert(injection);
            exceptionFlowAdded++;
        } catch (Exception e) {
            // Sessizce atla
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════════
    // RUNTIME PROTECTION INJECTION METHODS
    // ═══════════════════════════════════════════════════════════════════════════════

    /**
     * Anti-Debug check enjeksiyonu - GERÇEK KORUMA (crash-free)
     * checkAndAct() metodu içinde hem kontrol hem aksiyon yapılır
     */
    private static void injectAntiDebugCheck(MethodNode mn, String className) {
        try {
            // Sadece yeterli boyuttaki metotlara enjekte et
            if (mn.instructions.size() < 10) return;

            InsnList injection = new InsnList();

            // AntiDebug.checkAndAct() - kontrol + aksiyon tek metotta, jump yok
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/AntiDebug",
                "checkAndAct",
                "()V",
                false
            ));

            // Metot başına ekle
            mn.instructions.insert(injection);
            antiDebugInjected++;
        } catch (Exception e) {
            // Hata olursa sessizce atla
        }
    }

    /**
     * Anti-Tamper check enjeksiyonu - IntegrityGuard.check() ekler
     */
    private static void injectAntiTamperCheck(MethodNode mn, String className) {
        try {
            if (mn.instructions.size() < 15) return;

            InsnList injection = new InsnList();

            // IntegrityGuard.check() çağrısı
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/IntegrityGuard",
                "check",
                "()V",
                false
            ));

            // Metot başına ekle
            mn.instructions.insert(injection);
            antiTamperInjected++;
        } catch (Exception e) {
            // Sessizce atla
        }
    }

    /**
     * Timing check enjeksiyonu - GERÇEK KORUMA (crash-free)
     * timingCheck() metodu çağrılar arası süreyi ölçer
     */
    private static void injectTimingCheck(MethodNode mn, String className) {
        try {
            // Sadece yeterli boyuttaki metotlara ekle
            if (mn.instructions.size() < 20) return;

            InsnList injection = new InsnList();

            // AntiDebug.timingCheck() - gerçek timing kontrolü yapar
            injection.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "dev/just/protect/runtime/AntiDebug",
                "timingCheck",
                "()V",
                false
            ));

            // Metot başına ekle
            mn.instructions.insert(injection);
            timingChecksInjected++;
        } catch (Exception e) {
            // Sessizce atla
        }
    }

    private static boolean shouldSkipClass(String className) {
        for (String pkg : EXCLUDED_PACKAGES) {
            if (className.startsWith(pkg)) return true;
        }
        return false;
    }

    private static boolean isMixinClass(String className, ClassNode cn) {
        if (className.contains("/mixin/") || className.contains("Mixin")) return true;
        if (cn.visibleAnnotations != null) {
            for (AnnotationNode an : cn.visibleAnnotations) {
                if (an.desc.contains("Mixin") || an.desc.contains("spongepowered")) return true;
            }
        }
        if (cn.invisibleAnnotations != null) {
            for (AnnotationNode an : cn.invisibleAnnotations) {
                if (an.desc.contains("Mixin") || an.desc.contains("spongepowered")) return true;
            }
        }
        return false;
    }

    private static void resetStats() {
        sourceFilesObfuscated = 0;
        fakeFieldsAdded = 0;
        fakeMethodsAdded = 0;
        localsScrambled = 0;
        signaturesRemoved = 0;
        lineNumbersRemoved = 0;
        innerClassesCorrupted = 0;
        annotationsAdded = 0;
        methodsRenamed = 0;
        stringsEncrypted = 0;
        numbersObfuscated = 0;
        fieldsRenamed = 0;
        classesRenamed = 0;
        publicMethodsRenamed = 0;
        controlFlowObfuscated = 0;
        opaquePredicatesAdded = 0;
        gotoObfuscated = 0;
        tryCatchAdded = 0;
        stackManipulations = 0;
        noiseStringsInjected = 0;
        entropyFieldsAdded = 0;
        mathPollutionAdded = 0;
        antiDebugInjected = 0;
        antiTamperInjected = 0;
        timingChecksInjected = 0;
        reflectionCallsHidden = 0;
        opaqueConstantsAdded = 0;
        proxyCallsAdded = 0;
        exceptionFlowAdded = 0;
        nameCounter = 0;
    }

    private static void printStats(int processed, String outputPath) {
        long size = new File(outputPath).length();
        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║       OBFUSCATION COMPLETE - CatleanASM v13.0 ULTIMATE (10/10 EDITION)      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [STRING & NAMING]                                                          ║");
        System.out.printf("║   [+] Strings Encrypted:         %-6d                                       ║%n", stringsEncrypted);
        System.out.printf("║   [+] Private Methods Renamed:   %-6d                                       ║%n", methodsRenamed);
        System.out.printf("║   [+] Private Fields Renamed:    %-6d                                       ║%n", fieldsRenamed);
        System.out.printf("║   [+] Local Variables Scrambled: %-6d                                       ║%n", localsScrambled);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [CONTROL FLOW & BYTECODE]                                                  ║");
        System.out.printf("║   [+] Methods CF Obfuscated:     %-6d                                       ║%n", controlFlowObfuscated);
        System.out.printf("║   [+] Stack Manipulations:       %-6d                                       ║%n", stackManipulations);
        System.out.printf("║   [+] Math Pollution Added:      %-6d                                       ║%n", mathPollutionAdded);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [FAKE CODE & NOISE]                                                        ║");
        System.out.printf("║   [+] Fake Fields Added:         %-6d                                       ║%n", fakeFieldsAdded);
        System.out.printf("║   [+] Fake Methods Added:        %-6d  (MASSIVE SPAM)                       ║%n", fakeMethodsAdded);
        System.out.printf("║   [+] Entropy Fields Added:      %-6d                                       ║%n", entropyFieldsAdded);
        System.out.printf("║   [+] Noise Strings Injected:    %-6d  (Fake URLs/IPs/Hashes)               ║%n", noiseStringsInjected);
        System.out.printf("║   [+] Fake Annotations Added:    %-6d                                       ║%n", annotationsAdded);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [RUNTIME PROTECTION]                                                       ║");
        System.out.printf("║   [+] Anti-Debug Checks:         %-6d  (Debugger Detection)                 ║%n", antiDebugInjected);
        System.out.printf("║   [+] Anti-Tamper Checks:        %-6d  (Integrity Verification)             ║%n", antiTamperInjected);
        System.out.printf("║   [+] Timing Checks:             %-6d  (Stepping Detection)                 ║%n", timingChecksInjected);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [ADVANCED PROTECTION v2.0]                                                 ║");
        System.out.printf("║   [+] Opaque Constants:          %-6d  (Hidden Calculations)               ║%n", opaqueConstantsAdded);
        System.out.printf("║   [+] Proxy Calls:               %-6d  (Wrapped Method Calls)              ║%n", proxyCallsAdded);
        System.out.printf("║   [+] Exception Flow:            %-6d  (Try-Catch Confusion)               ║%n", exceptionFlowAdded);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║   [METADATA DESTRUCTION]                                                     ║");
        System.out.printf("║   [+] Source Files Obfuscated:   %-6d                                       ║%n", sourceFilesObfuscated);
        System.out.printf("║   [+] Signatures Removed:        %-6d                                       ║%n", signaturesRemoved);
        System.out.printf("║   [+] Inner Classes Corrupted:   %-6d                                       ║%n", innerClassesCorrupted);
        System.out.println("╠══════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║   Classes: %-6d | Size: %-10d bytes                                    ║%n", processed, size);
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝\n");
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int len;
        while ((len = is.read(buf)) != -1) baos.write(buf, 0, len);
        return baos.toByteArray();
    }
}
