/**
 * JustClient Native Protection Library
 *
 * Provides:
 * - Native string decryption (faster, harder to hook)
 * - Anti-debugging at native level
 * - Hardware fingerprinting
 * - Memory protection
 *
 * Compile for Windows:
 *   cl /LD /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" native_protect.c /Fe:justclient.dll
 *
 * Compile for Linux:
 *   gcc -shared -fPIC -I$JAVA_HOME/include -I$JAVA_HOME/include/linux native_protect.c -o libjustclient.so
 *
 * Compile for macOS:
 *   gcc -shared -fPIC -I$JAVA_HOME/include -I$JAVA_HOME/include/darwin native_protect.c -o libjustclient.dylib
 */

#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

#ifdef _WIN32
#include <windows.h>
#include <intrin.h>
#else
#include <unistd.h>
#include <sys/time.h>
#include <sys/ptrace.h>
#endif

#include "dev_just_protect_runtime__Native.h"

/* Version number */
#define NATIVE_VERSION 1

/* Encryption keys - MUST MATCH _S.java */
#define ENC_K1 0x6A09E667
#define ENC_K2 0xBB67AE85
#define ENC_K3 0x3C6EF372

/* 32-bit mix function (MurmurHash3 finalizer) */
static uint32_t mix(uint32_t h) {
    h ^= h >> 16;
    h *= 0x85EBCA6B;
    h ^= h >> 13;
    h *= 0xC2B2AE35;
    h ^= h >> 16;
    return h;
}

/* Rotate left */
static uint32_t rotl(uint32_t x, int n) {
    return (x << n) | (x >> (32 - n));
}

/* Key table (computed once) */
static uint32_t kt[256];
static int kt_initialized = 0;

static void init_kt() {
    if (kt_initialized) return;

    uint32_t k1 = mix(ENC_K1);
    uint32_t k2 = mix(ENC_K2);
    uint32_t k3 = mix(ENC_K3);

    uint32_t k = k1 ^ k2;
    for (int i = 0; i < 256; i++) {
        k = mix(k ^ i ^ k3);
        kt[i] = k;
    }

    kt_initialized = 1;
}

/*
 * Native string decryption
 * Matches _S.d(int id, int[] e) decryption logic
 */
JNIEXPORT jstring JNICALL Java_dev_just_protect_runtime__1Native_nDecryptString
  (JNIEnv *env, jclass cls, jbyteArray encrypted, jint key)
{
    init_kt();

    if (encrypted == NULL) {
        return (*env)->NewStringUTF(env, "");
    }

    jsize len = (*env)->GetArrayLength(env, encrypted);
    if (len == 0 || len % 4 != 0) {
        return (*env)->NewStringUTF(env, "");
    }

    jbyte *bytes = (*env)->GetByteArrayElements(env, encrypted, NULL);
    if (bytes == NULL) {
        return (*env)->NewStringUTF(env, "");
    }

    /* Number of int values */
    int numInts = len / 4;

    /* Convert bytes to ints (big-endian) */
    uint32_t *e = (uint32_t *)malloc(numInts * sizeof(uint32_t));
    for (int i = 0; i < numInts; i++) {
        e[i] = ((uint32_t)(bytes[i * 4] & 0xFF) << 24) |
               ((uint32_t)(bytes[i * 4 + 1] & 0xFF) << 16) |
               ((uint32_t)(bytes[i * 4 + 2] & 0xFF) << 8) |
               ((uint32_t)(bytes[i * 4 + 3] & 0xFF));
    }

    (*env)->ReleaseByteArrayElements(env, encrypted, bytes, JNI_ABORT);

    /* Allocate result buffer (UTF-16 chars) */
    jchar *result = (jchar *)malloc(numInts * sizeof(jchar));

    /* Derive keys */
    uint32_t k1 = mix(ENC_K1);

    /* Initial key */
    uint32_t k = mix(k1 ^ mix(ENC_K2) ^ mix(ENC_K3) ^ key);

    /* Decrypt each character */
    for (int i = 0; i < numInts; i++) {
        uint32_t t = kt[i & 0xFF];

        /* Decrypt character (k is used BEFORE update) */
        result[i] = (jchar)((e[i] ^ k ^ (i * 0x1337) ^ t ^ k1) & 0xFFFF);

        /* Update key AFTER decryption */
        k = rotl(k, 7) ^ e[i];
    }

    free(e);

    /* Create Java string */
    jstring str = (*env)->NewString(env, result, numInts);
    free(result);

    return str;
}

/*
 * Check if debugger is present (native level)
 */
JNIEXPORT jboolean JNICALL Java_dev_just_protect_runtime__1Native_nIsDebuggerPresent
  (JNIEnv *env, jclass cls)
{
#ifdef _WIN32
    /* Windows: Use IsDebuggerPresent API */
    if (IsDebuggerPresent()) {
        return JNI_TRUE;
    }

    /* Check PEB for BeingDebugged flag */
    BOOL debugged = FALSE;
    CheckRemoteDebuggerPresent(GetCurrentProcess(), &debugged);
    if (debugged) {
        return JNI_TRUE;
    }

    /* Check for common debugger processes */
    /* (simplified - real implementation would enumerate processes) */

    return JNI_FALSE;
#else
    /* Linux/macOS: Use ptrace */
    /* If ptrace fails, likely being traced */
    #ifdef __linux__
    if (ptrace(PTRACE_TRACEME, 0, NULL, NULL) == -1) {
        return JNI_TRUE;
    }
    ptrace(PTRACE_DETACH, 0, NULL, NULL);
    #endif

    /* Check /proc/self/status for TracerPid */
    FILE *f = fopen("/proc/self/status", "r");
    if (f) {
        char line[256];
        while (fgets(line, sizeof(line), f)) {
            if (strncmp(line, "TracerPid:", 10) == 0) {
                int pid = atoi(line + 10);
                fclose(f);
                return pid != 0 ? JNI_TRUE : JNI_FALSE;
            }
        }
        fclose(f);
    }

    return JNI_FALSE;
#endif
}

/*
 * Protect memory region (prevent read/write)
 */
JNIEXPORT jboolean JNICALL Java_dev_just_protect_runtime__1Native_nProtectMemory
  (JNIEnv *env, jclass cls, jlong address, jint size)
{
#ifdef _WIN32
    DWORD oldProtect;
    if (VirtualProtect((LPVOID)address, size, PAGE_NOACCESS, &oldProtect)) {
        return JNI_TRUE;
    }
#else
    /* Linux/macOS: mprotect */
    /* Note: address must be page-aligned */
    /* Skipped for simplicity */
#endif
    return JNI_FALSE;
}

/*
 * Get hardware ID (native level, more accurate)
 */
JNIEXPORT jlong JNICALL Java_dev_just_protect_runtime__1Native_nGetHardwareId
  (JNIEnv *env, jclass cls)
{
    uint64_t hwid = 0;

#ifdef _WIN32
    /* Use CPUID for CPU info */
    int cpuInfo[4] = {0};
    __cpuid(cpuInfo, 0);
    hwid ^= ((uint64_t)cpuInfo[1] << 32) | cpuInfo[3];
    __cpuid(cpuInfo, 1);
    hwid ^= cpuInfo[0];

    /* Get volume serial number */
    DWORD serial = 0;
    GetVolumeInformationA("C:\\", NULL, 0, &serial, NULL, NULL, NULL, 0);
    hwid ^= serial;

    /* Get computer name hash */
    char computerName[256] = {0};
    DWORD size = sizeof(computerName);
    GetComputerNameA(computerName, &size);
    for (int i = 0; computerName[i]; i++) {
        hwid ^= ((uint64_t)computerName[i]) << ((i * 5) % 64);
    }
#else
    /* Linux: Use /etc/machine-id if available */
    FILE *f = fopen("/etc/machine-id", "r");
    if (f) {
        char id[64] = {0};
        fgets(id, sizeof(id), f);
        fclose(f);
        for (int i = 0; id[i] && id[i] != '\n'; i++) {
            hwid ^= ((uint64_t)id[i]) << ((i * 5) % 64);
        }
    }

    /* Add hostname */
    char hostname[256] = {0};
    gethostname(hostname, sizeof(hostname));
    for (int i = 0; hostname[i]; i++) {
        hwid ^= ((uint64_t)hostname[i]) << ((i * 7) % 64);
    }
#endif

    /* Mix the result */
    hwid ^= hwid >> 33;
    hwid *= 0xFF51AFD7ED558CCDULL;
    hwid ^= hwid >> 33;
    hwid *= 0xC4CEB9FE1A85EC53ULL;
    hwid ^= hwid >> 33;

    return (jlong)hwid;
}

/*
 * Get native library version
 */
JNIEXPORT jint JNICALL Java_dev_just_protect_runtime__1Native_nGetVersion
  (JNIEnv *env, jclass cls)
{
    return NATIVE_VERSION;
}

/*
 * Get precise time (native level)
 */
JNIEXPORT jlong JNICALL Java_dev_just_protect_runtime__1Native_nGetPreciseTime
  (JNIEnv *env, jclass cls)
{
#ifdef _WIN32
    LARGE_INTEGER freq, counter;
    QueryPerformanceFrequency(&freq);
    QueryPerformanceCounter(&counter);
    return (jlong)((counter.QuadPart * 1000000000LL) / freq.QuadPart);
#else
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return (jlong)(tv.tv_sec * 1000000000LL + tv.tv_usec * 1000LL);
#endif
}

/*
 * JNI_OnLoad - called when library is loaded
 */
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    /* Initialize key table */
    init_kt();

    /* Perform anti-debugging check on load */
    #ifdef _WIN32
    if (IsDebuggerPresent()) {
        /* Optional: crash or return wrong version */
    }
    #endif

    return JNI_VERSION_1_8;
}
