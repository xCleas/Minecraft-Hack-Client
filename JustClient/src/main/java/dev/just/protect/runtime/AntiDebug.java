package dev.just.protect.runtime;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Anti-Debug & Anti-Attach Protection - STRENGTHENED VERSION
 * Method names and structure strictly preserved.
 */
public final class AntiDebug {

    private static volatile boolean debuggerDetected = false;
    
    // GIZLI SABITLER: Artik "jdwp" veya "intellij" yazilari JAR icinde aratinca bulunamaz.
    private static final String[] S_A = {
        "LWFnZW50bGliOmpkd3A=", "LVhkZWJ1Zw==", "LVhydW5qZHdw", 
        "LWphdmFhZ2VudDo=", "c3VzcGVuZD0=", "dHJhbnNwb3J0PWR0X3NvY2tldA==",
        "LWFnZW50cGF0aDo=", "SkFWQV9UT09MX09QVElPTlM="
    };

    private static final String[] S_C = {
        "Y29tLnN1bi5qZGku", "b3JnLm5ldGJlYW5zLg==", "Y29tLmludGVsbGlqLg==", 
        "b3JnLmVjbGlwc2UuamR0Lg==", "Y29tLnlvdXJraXQu", "Y29tLmpwcm9maWxlci4=",
        "b3JnLmpib3NzLmJ5dGVtYW4u"
    };

    private AntiDebug() {}

    /**
     * Dahili String Cozucu - Reverse Engineer'larin isini zorlastirir.
     */
    private static String d(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }

    /**
     * Tum anti-debug kontrollerini baslat
     */
    public static void init() {
        // Anlik kontrol
        checkAll();

        // Surekli izleme (her 500ms) - TLauncher/Fabric uyumlu Daemon thread
        Timer timer = new Timer(d("QW50aURlYnVn"), true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    checkAll();
                } catch (Throwable t) {
                    // Hata durumunda sessiz kal ki client cokmesin
                }
            }
        }, 500, 500);
    }

    private static void checkAll() {
        if (debuggerDetected) return;

        // Mantik siralamasi ve metodlar birebir ayni
        if (checkJVMArgs() || checkDebuggerAttached() || checkSuspiciousClasses() ||
            checkTimingAttack() || checkAgentLoaded() || checkBreakpoints()) {
            onDebuggerDetected();
        }
    }

    /**
     * JVM argumanlari kontrolu
     */
    private static boolean checkJVMArgs() {
        try {
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            List<String> args = runtimeBean.getInputArguments();
            
            for (String arg : args) {
                String lowArg = arg.toLowerCase();
                for (String suspicious : S_A) {
                    if (lowArg.contains(d(suspicious).toLowerCase())) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Debugger bagli mi kontrolu
     */
    private static boolean checkDebuggerAttached() {
        try {
            // JDWP Kontrolu - Runtime adina bakma
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            if (runtime.getName().toLowerCase().contains(d("ZGVidWc="))) { // "debug"
                return true;
            }

            // Native debugger kontrolu (Windows/Linux/Mac uyumlu)
            if (isNativeDebuggerPresent()) {
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Native debugger kontrolu
     */
    private static boolean isNativeDebuggerPresent() {
        try {
            // Thread hiyerarsisini tarayarak gizli debug threadlerini bulur
            ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
            ThreadGroup parentGroup;
            while ((parentGroup = rootGroup.getParent()) != null) {
                rootGroup = parentGroup;
            }

            Thread[] threads = new Thread[rootGroup.activeCount() + 10];
            rootGroup.enumerate(threads);

            for (Thread t : threads) {
                if (t != null) {
                    String name = t.getName().toLowerCase();
                    // JDWP ve Attach thread'lerini yakalar
                    if (name.contains(d("amR3cA==")) || name.contains(d("YXR0YWNo")) || 
                        name.contains(d("ZXZlbnQtaGFuZGxlcg==")) || name.contains(d("cHJvZmlsZXI="))) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Suphe verici siniflar yuklenmis mi
     */
    private static boolean checkSuspiciousClasses() {
        for (String className : S_C) {
            String decodedName = d(className);
            try {
                // ClassLoader hilesi ile debugger classlarini arar
                Class.forName(decodedName + d("Qm9vdHN0cmFw"), false, AntiDebug.class.getClassLoader());
                return true;
            } catch (ClassNotFoundException ignored) {}

            try {
                Class.forName(decodedName + d("VmlydHVhbE1hY2hpbmU="), false, AntiDebug.class.getClassLoader());
                return true;
            } catch (ClassNotFoundException ignored) {}
        }
        return false;
    }

    private static long lastCheck = 0;
    private static int slowCount = 0;

    /**
     * Timing attack kontrolu - debugger yavaslatir
     */
    private static boolean checkTimingAttack() {
        long now = System.nanoTime();
        if (lastCheck > 0) {
            long diff = now - lastCheck;
            // Eger bir breakpoint tetiklenirse, bu fark milisaniyelerden saniyelere cikar.
            if (diff > 150_000_000L) { // 150ms limit (Fabric icin optimize edildi)
                slowCount++;
                if (slowCount > 2) {
                    return true;
                }
            } else {
                slowCount = Math.max(0, slowCount - 1);
            }
        }
        lastCheck = now;
        return false;
    }

    /**
     * Java agent yuklenmis mi
     */
    private static boolean checkAgentLoaded() {
        try {
            // Java Agent tespit sistemi
            String bootPath = System.getProperty(d("c3VuLmJvb3QuY2xhc3MucGF0aA=="), "");
            if (bootPath.toLowerCase().contains(d("YWdlbnQ="))) {
                return true;
            }
            
            // Instrumentation API varligi kontrolu
            try {
                Class.forName(d("amF2YS5sYW5nLmluc3RydW1lbnQuSW5zdHJ1bWVudGF0aW9u"));
                // Agent yuklu degilse normalde bu sinifa bu sekilde erisilmez
            } catch (ClassNotFoundException e) {
                // Normal durum
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Breakpoint kontrolu
     */
    private static boolean checkBreakpoints() {
        try {
            long start = System.nanoTime();
            
            // JIT compiler'in optimize edemeyecegi bir islem dizisi
            double x = Math.random();
            for(int i = 0; i < 500; i++) {
                x = Math.atan(Math.sqrt(x + i));
            }

            long elapsed = System.nanoTime() - start;

            // 15ms'den uzun surmesi bir debugger'in adim adim (stepping) calistigini gosterir
            if (elapsed > 15_000_000L) {
                return true;
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Debugger algilandiginda - SOFT FAIL (TLauncher uyumlu)
     */
    private static void onDebuggerDetected() {
        debuggerDetected = true;

        // Canary tetikle - ozellikler degrade olacak ama crash yok
        // Bu kisim hileyi kirmaya calisani delirtir cunku client kapanmaz ama modlar calismaz.
        try {
            CanaryLogic.trigger(CanaryLogic.CANARY_DEBUG);
        } catch (Exception ignored) {
            // Fail-safe: Eger CanaryLogic yoksa direkt sistemi kapa
            System.exit(0);
        }
    }

    public static boolean isDebuggerDetected() {
        return debuggerDetected;
    }

    /**
     * Check ve aksiyon tek metotta - jump gerektirmez
     * Debugger varsa thread'i yavaşlatır ve canary tetikler
     */
    public static void checkAndAct() {
        if (debuggerDetected) {
            // Debugger tespit edildi - yavaşlat ve canary tetikle
            try {
                Thread.sleep(50); // Performansı düşür
                CanaryLogic.trigger(CanaryLogic.CANARY_DEBUG);
            } catch (Exception ignored) {}
        }
    }

    /**
     * Timing-based check - çağrıldığında süreyi kontrol eder
     */
    private static long lastCallTime = 0;
    private static int slowCallCount = 0;

    public static void timingCheck() {
        long now = System.nanoTime();
        if (lastCallTime > 0) {
            long diff = now - lastCallTime;
            // 100ms'den uzun sürdüyse şüpheli
            if (diff > 100_000_000L) {
                slowCallCount++;
                if (slowCallCount > 3) {
                    debuggerDetected = true;
                    onDebuggerDetected();
                }
            } else {
                slowCallCount = Math.max(0, slowCallCount - 1);
            }
        }
        lastCallTime = now;
    }

    /**
     * Environment check - VM/sandbox kontrolü
     */
    public static void envCheck() {
        try {
            String[] suspicious = {"JAVA_TOOL_OPTIONS", "JDK_JAVA_OPTIONS", "_JAVA_OPTIONS"};
            for (String env : suspicious) {
                String val = System.getenv(env);
                if (val != null && (val.contains("agent") || val.contains("debug"))) {
                    debuggerDetected = true;
                    onDebuggerDetected();
                    return;
                }
            }
        } catch (Exception ignored) {}
    }
}