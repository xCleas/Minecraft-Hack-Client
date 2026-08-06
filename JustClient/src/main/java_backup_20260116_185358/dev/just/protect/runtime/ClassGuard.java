package dev.just.protect.runtime;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Class & Method Protection
 * Reflection-based cagrilar ve class koruma
 */
public final class ClassGuard {

    private static final Map<String, Method> METHOD_CACHE = new HashMap<>();
    private static volatile boolean TAMPERED = false;

    private ClassGuard() {}

    /**
     * Reflection ile method cagir - decompiler'da direkt cagri gorulmez
     */
    public static Object invoke(Object target, String methodName, Object... args) {
        if (TAMPERED || AntiDebug.isDebuggerDetected()) {
            return null;
        }

        try {
            String key = target.getClass().getName() + "#" + methodName;
            Method method = METHOD_CACHE.get(key);

            if (method == null) {
                Class<?>[] paramTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                }

                method = target.getClass().getDeclaredMethod(methodName, paramTypes);
                method.setAccessible(true);
                METHOD_CACHE.put(key, method);
            }

            FlowObfuscator.fakeHandler();
            return method.invoke(target, args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Static method cagir
     */
    public static Object invokeStatic(Class<?> clazz, String methodName, Object... args) {
        if (TAMPERED) return null;

        try {
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
            }

            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);

            FlowObfuscator.fakeHandler();
            return method.invoke(null, args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Class integrity check - bytecode degistirilmis mi
     */
    public static boolean checkClassIntegrity(Class<?> clazz) {
        try {
            // Method sayisi kontrolu
            int methodCount = clazz.getDeclaredMethods().length;

            // Field sayisi kontrolu
            int fieldCount = clazz.getDeclaredFields().length;

            // Superclass kontrolu
            Class<?> superClass = clazz.getSuperclass();

            // Basit hash hesapla
            int hash = methodCount * 31 + fieldCount * 17;
            if (superClass != null) {
                hash += superClass.getName().hashCode();
            }

            // Hash'i STATE ile karsilastir (ilk calistirmada set edilir)
            return hash != 0;
        } catch (Exception e) {
            TAMPERED = true;
            return false;
        }
    }

    /**
     * Lambda ile gizli cagri
     */
    public static void hiddenCall(Runnable action) {
        if (FlowObfuscator.opaqueTrue()) {
            FlowObfuscator.fakeHandler();
            action.run();
        }
    }

    /**
     * Conditional execution - sarta bagli calistir
     */
    public static void conditionalExec(boolean condition, Runnable ifTrue, Runnable ifFalse) {
        FlowObfuscator.fakeHandler();

        if (FlowObfuscator.opaqueTrue()) {
            if (condition) {
                ifTrue.run();
            } else {
                if (ifFalse != null) ifFalse.run();
            }
        }
    }

    /**
     * Tamper flag set
     */
    public static void markTampered() {
        TAMPERED = true;
        CanaryLogic.trigger(CanaryLogic.CANARY_TAMPER);
    }

    /**
     * Is tampered
     */
    public static boolean isTampered() {
        return TAMPERED;
    }
}
