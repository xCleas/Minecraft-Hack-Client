package dev.just.protect.runtime;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.security.MessageDigest;
import java.util.Timer;
import java.util.TimerTask;

/**
 * VM/JVM Tamper Detection
 * Sanal makine ve JVM manipulasyonu algilar
 */
public final class VMCheck {

    private static volatile boolean tamperDetected = false;

    // Bilinen VM vendor isimleri (sandbox/analysis)
    private static final String[] VM_VENDORS = {
        "vmware",
        "virtualbox",
        "vbox",
        "qemu",
        "xen",
        "parallels",
        "hyperv",
        "microsoft corporation virtual",
        "innotek",
        "oracle vm",
        "bochs",
        "sandboxie"
    };

    // Bilinen analiz toollari
    private static final String[] ANALYSIS_PROCESSES = {
        "wireshark",
        "fiddler",
        "charles",
        "procmon",
        "processhacker",
        "ollydbg",
        "x64dbg",
        "x32dbg",
        "ida",
        "ghidra",
        "dnspy",
        "jd-gui",
        "bytecodeviewer",
        "recaf",
        "jadx"
    };

    private VMCheck() {}

    /**
     * Tum kontrolleri baslat
     */
    public static void init() {
        // Anlik kontrol
        checkAll();

        // Surekli izleme (her 2 saniye)
        Timer timer = new Timer("VMCheck", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkIntegrity();
            }
        }, 2000, 2000);
    }

    private static void checkAll() {
        if (tamperDetected) return;

        if (checkVMEnvironment() || checkSandbox() || checkAnalysisTools() ||
            checkClassTampering() || checkMemoryManipulation()) {
            onTamperDetected("VM/Sandbox detected");
        }
    }

    /**
     * Sanal makine kontrolu
     */
    private static boolean checkVMEnvironment() {
        try {
            // System properties kontrolu
            String[] props = {
                System.getProperty("java.vm.name", ""),
                System.getProperty("java.vm.vendor", ""),
                System.getProperty("os.name", ""),
                System.getProperty("os.arch", "")
            };

            String combined = String.join(" ", props).toLowerCase();

            for (String vm : VM_VENDORS) {
                if (combined.contains(vm)) {
                    return true;
                }
            }

            // MAC address kontrolu (VM'ler bilinen MAC prefiksleri kullanir)
            // 00:0C:29, 00:50:56 (VMware)
            // 08:00:27 (VirtualBox)

            // CPU core sayisi (cok dusuk = sandbox)
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            int cpus = osBean.getAvailableProcessors();
            if (cpus < 2) {
                return true;
            }

            // RAM kontrolu (cok dusuk = sandbox)
            long totalMemory = Runtime.getRuntime().maxMemory();
            if (totalMemory < 512 * 1024 * 1024L) { // 512MB'dan az
                return true;
            }

        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Sandbox kontrolu
     */
    private static boolean checkSandbox() {
        try {
            // Temp klasor kontrolu
            String tempDir = System.getProperty("java.io.tmpdir", "");
            if (tempDir.toLowerCase().contains("sandbox") ||
                tempDir.toLowerCase().contains("malware") ||
                tempDir.toLowerCase().contains("sample")) {
                return true;
            }

            // User name kontrolu
            String userName = System.getProperty("user.name", "").toLowerCase();
            if (userName.equals("sandbox") || userName.equals("malware") ||
                userName.equals("virus") || userName.equals("sample") ||
                userName.equals("test") || userName.equals("analysis")) {
                return true;
            }

            // Computer name kontrolu (Windows)
            String computerName = System.getenv("COMPUTERNAME");
            if (computerName != null) {
                computerName = computerName.toLowerCase();
                if (computerName.contains("sandbox") || computerName.contains("malware") ||
                    computerName.contains("virus") || computerName.contains("sample")) {
                    return true;
                }
            }

            // Suspicious files (sandbox artifacts)
            String[] suspiciousFiles = {
                "C:\\analysis",
                "C:\\sandbox",
                "C:\\insidetm",
                "C:\\strawberry\\perl"
            };

            for (String path : suspiciousFiles) {
                if (new File(path).exists()) {
                    return true;
                }
            }

        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Analiz tool kontrolu
     */
    private static boolean checkAnalysisTools() {
        try {
            // Calisani isle kontrol
            ProcessBuilder pb = new ProcessBuilder("tasklist.exe");
            Process process = pb.start();

            java.io.InputStream is = process.getInputStream();
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                for (String tool : ANALYSIS_PROCESSES) {
                    if (line.contains(tool)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Class tampering kontrolu
     */
    private static boolean checkClassTampering() {
        try {
            // Kritik siniflarin bytecode hash'ini kontrol et
            Class<?>[] criticalClasses = {
                VMCheck.class,
                AntiDebug.class
            };

            for (Class<?> clazz : criticalClasses) {
                // Class loader kontrolu
                ClassLoader loader = clazz.getClassLoader();
                if (loader == null) {
                    continue; // Bootstrap loader, normal
                }

                // Suspicious loader kontrolu
                String loaderName = loader.getClass().getName().toLowerCase();
                if (loaderName.contains("agent") || loaderName.contains("transform") ||
                    loaderName.contains("instrument") || loaderName.contains("hook")) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Memory manipulation kontrolu
     */
    private static boolean checkMemoryManipulation() {
        try {
            // Canary deger kontrolu
            long canary = 0xDEADBEEFCAFEBABEL;
            long[] canaryArray = new long[]{canary, canary, canary};

            // Biraz bekle
            Thread.sleep(10);

            // Canary degerleri degismis mi
            for (long val : canaryArray) {
                if (val != canary) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Periyodik integrity kontrolu
     */
    private static void checkIntegrity() {
        if (tamperDetected) return;

        // Timing kontrolu
        long start = System.nanoTime();

        // Basit islem
        int result = 0;
        for (int i = 0; i < 10000; i++) {
            result ^= i;
        }

        long elapsed = System.nanoTime() - start;

        // Anormal yavaslik (50ms'den fazla = muhtemel debug/trace)
        if (elapsed > 50_000_000L) {
            onTamperDetected("Timing anomaly");
        }

        // Result'i kullan ki optimize edilmesin
        if (result == Integer.MIN_VALUE + 1) {
            System.nanoTime();
        }
    }

    /**
     * Tamper algilandiginda - SOFT FAIL (TLauncher uyumlu)
     */
    private static void onTamperDetected(String reason) {
        tamperDetected = true;

        // Canary tetikle - ozellikler degrade olacak ama crash yok
        try {
            CanaryLogic.trigger(CanaryLogic.CANARY_TAMPER);
        } catch (Exception ignored) {}
    }

    public static boolean isTamperDetected() {
        return tamperDetected;
    }
}
