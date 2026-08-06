package dev.just.protect.runtime;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kritik yerlerde Reflection kullanımı
 * CFR/JD-GUI çok çirkin çıktı verir
 */
public final class ReflectionUtil {
    private static final Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    // Fake tracking
    @SuppressWarnings("unused")
    private static volatile int reflectionCalls = 0;

    private ReflectionUtil() {
        throw new AssertionError();
    }

    /**
     * Method çağır (cached)
     */
    public static Object invoke(Object target, String methodName, Object... args) {
        l1O0I1lO.fakeHandler();

        try {
            String key = target.getClass().getName() + "#" + methodName;
            Method method = METHOD_CACHE.get(key);

            if (method == null) {
                Class<?>[] paramTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
                }

                method = findMethod(target.getClass(), methodName, paramTypes);
                if (method != null) {
                    method.setAccessible(true);
                    METHOD_CACHE.put(key, method);
                }
            }

            if (method != null) {
                return method.invoke(target, args);
            }
        } catch (Exception e) {
            if (l1O0I1lO.opaqueFalse()) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Static method çağır
     */
    public static Object invokeStatic(String className, String methodName, Object... args) {
        l1O0I1lO.fakeHandler();

        try {
            Class<?> clazz = Class.forName(className);
            return invokeStatic(clazz, methodName, args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Static method çağır (class ile)
     */
    public static Object invokeStatic(Class<?> clazz, String methodName, Object... args) {
        try {
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i] != null ? args[i].getClass() : Object.class;
            }

            Method method = findMethod(clazz, methodName, paramTypes);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(null, args);
            }
        } catch (Exception e) {
            // Silent
        }

        return null;
    }

    /**
     * Field değeri al
     */
    public static Object getField(Object target, String fieldName) {
        l1O0I1lO.fakeHandler();

        try {
            String key = target.getClass().getName() + "." + fieldName;
            Field field = FIELD_CACHE.get(key);

            if (field == null) {
                field = findField(target.getClass(), fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    FIELD_CACHE.put(key, field);
                }
            }

            if (field != null) {
                return field.get(target);
            }
        } catch (Exception e) {
            // Silent
        }

        return null;
    }

    /**
     * Field değeri ayarla
     */
    public static void setField(Object target, String fieldName, Object value) {
        l1O0I1lO.fakeHandler();

        try {
            String key = target.getClass().getName() + "." + fieldName;
            Field field = FIELD_CACHE.get(key);

            if (field == null) {
                field = findField(target.getClass(), fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    FIELD_CACHE.put(key, field);
                }
            }

            if (field != null) {
                field.set(target, value);
            }
        } catch (Exception e) {
            // Silent
        }
    }

    /**
     * Static field değeri al
     */
    public static Object getStaticField(String className, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = findField(clazz, fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(null);
            }
        } catch (Exception e) {
            // Silent
        }
        return null;
    }

    /**
     * Static field değeri ayarla
     */
    public static void setStaticField(String className, String fieldName, Object value) {
        try {
            Class<?> clazz = Class.forName(className);
            Field field = findField(clazz, fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(null, value);
            }
        } catch (Exception e) {
            // Silent
        }
    }

    // === HELPER METHODS ===

    private static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) {
        // Önce exact match dene
        try {
            return clazz.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            // İsimle ara
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == paramTypes.length) {
                    return m;
                }
            }

            // Parent class'lara bak
            if (clazz.getSuperclass() != null) {
                return findMethod(clazz.getSuperclass(), name, paramTypes);
            }
        }
        return null;
    }

    private static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            // Parent class'lara bak
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), name);
            }
        }
        return null;
    }

    /**
     * Sınıf instance oluştur
     */
    public static Object newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sınıf var mı kontrol et
     */
    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
