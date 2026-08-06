/**
 * JustClient Native Core
 * JNI implementation for secure operations
 *
 * Cross-platform: Windows, Linux, macOS
 */

#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#ifdef _WIN32
    #include <windows.h>
    #define EXPORT __declspec(dllexport)
#else
    #include <sys/time.h>
    #define EXPORT __attribute__((visibility("default")))
#endif

// ============================================================
// INTERNAL HELPERS
// ============================================================

// LCG random with seed
static unsigned int lcg_next(unsigned int* seed) {
    *seed = (*seed * 1103515245u + 12345u) & 0x7FFFFFFFu;
    return (*seed >> 16) & 0xFF;
}

// Secure memcmp (constant time)
static int secure_compare(const unsigned char* a, const unsigned char* b, size_t len) {
    int result = 0;
    for (size_t i = 0; i < len; i++) {
        result |= a[i] ^ b[i];
    }
    return result;
}

// Simple obfuscated string storage
static const unsigned char KEY_BYTES[] = {
    0x4A, 0x75, 0x73, 0x74, 0x43, 0x6C, 0x69, 0x65,
    0x6E, 0x74, 0x5F, 0x4E, 0x61, 0x74, 0x69, 0x76
};
static const int KEY_LEN = sizeof(KEY_BYTES);

// ============================================================
// JNI IMPLEMENTATIONS
// ============================================================

/**
 * nDecrypt - Decrypt byte array with key
 */
JNIEXPORT jstring JNICALL Java_dev_just_protect_NativeLib_nDecrypt(
    JNIEnv* env,
    jclass cls,
    jbyteArray data,
    jint key
) {
    if (data == NULL) {
        return (*env)->NewStringUTF(env, "");
    }

    jsize len = (*env)->GetArrayLength(env, data);
    if (len == 0) {
        return (*env)->NewStringUTF(env, "");
    }

    // Get bytes
    jbyte* bytes = (*env)->GetByteArrayElements(env, data, NULL);
    if (bytes == NULL) {
        return (*env)->NewStringUTF(env, "");
    }

    // Allocate result buffer (+1 for null terminator)
    char* result = (char*)malloc(len + 1);
    if (result == NULL) {
        (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
        return (*env)->NewStringUTF(env, "");
    }

    // Decrypt using LCG
    unsigned int seed = (unsigned int)key;
    for (jsize i = 0; i < len; i++) {
        unsigned char k = (unsigned char)lcg_next(&seed);
        result[i] = (char)(bytes[i] ^ k ^ KEY_BYTES[i % KEY_LEN]);
    }
    result[len] = '\0';

    // Release and return
    (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
    jstring jresult = (*env)->NewStringUTF(env, result);
    free(result);

    return jresult;
}

/**
 * nValidate - Constant-time comparison
 * Returns 0 if equal, non-zero otherwise
 */
JNIEXPORT jint JNICALL Java_dev_just_protect_NativeLib_nValidate(
    JNIEnv* env,
    jclass cls,
    jbyteArray input,
    jbyteArray expected
) {
    if (input == NULL || expected == NULL) {
        return -1;
    }

    jsize inputLen = (*env)->GetArrayLength(env, input);
    jsize expectedLen = (*env)->GetArrayLength(env, expected);

    if (inputLen != expectedLen) {
        return 1;
    }

    jbyte* inputBytes = (*env)->GetByteArrayElements(env, input, NULL);
    jbyte* expectedBytes = (*env)->GetByteArrayElements(env, expected, NULL);

    if (inputBytes == NULL || expectedBytes == NULL) {
        if (inputBytes) (*env)->ReleaseByteArrayElements(env, input, inputBytes, JNI_ABORT);
        if (expectedBytes) (*env)->ReleaseByteArrayElements(env, expected, expectedBytes, JNI_ABORT);
        return -2;
    }

    int result = secure_compare(
        (unsigned char*)inputBytes,
        (unsigned char*)expectedBytes,
        (size_t)inputLen
    );

    (*env)->ReleaseByteArrayElements(env, input, inputBytes, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, expected, expectedBytes, JNI_ABORT);

    return result;
}

/**
 * nTransform - Encode/decode data
 * mode 0 = encode, mode 1 = decode
 */
JNIEXPORT jbyteArray JNICALL Java_dev_just_protect_NativeLib_nTransform(
    JNIEnv* env,
    jclass cls,
    jbyteArray data,
    jint mode
) {
    if (data == NULL) {
        return (*env)->NewByteArray(env, 0);
    }

    jsize len = (*env)->GetArrayLength(env, data);
    if (len == 0) {
        return (*env)->NewByteArray(env, 0);
    }

    jbyte* bytes = (*env)->GetByteArrayElements(env, data, NULL);
    if (bytes == NULL) {
        return (*env)->NewByteArray(env, 0);
    }

    // Allocate result
    jbyteArray result = (*env)->NewByteArray(env, len);
    if (result == NULL) {
        (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
        return (*env)->NewByteArray(env, 0);
    }

    jbyte* resultBytes = (*env)->GetByteArrayElements(env, result, NULL);
    if (resultBytes == NULL) {
        (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
        return (*env)->NewByteArray(env, 0);
    }

    // Transform
    if (mode == 0) {
        // Encode
        for (jsize i = 0; i < len; i++) {
            unsigned char b = (unsigned char)bytes[i];
            b = (b + 0x5A + i) ^ 0xAB;
            b ^= KEY_BYTES[i % KEY_LEN];
            resultBytes[i] = (jbyte)b;
        }
    } else {
        // Decode
        for (jsize i = 0; i < len; i++) {
            unsigned char b = (unsigned char)bytes[i];
            b ^= KEY_BYTES[i % KEY_LEN];
            b = (b ^ 0xAB) - 0x5A - i;
            resultBytes[i] = (jbyte)b;
        }
    }

    (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, result, resultBytes, 0);

    return result;
}

/**
 * nGetTimestamp - Get current timestamp (milliseconds)
 */
JNIEXPORT jlong JNICALL Java_dev_just_protect_NativeLib_nGetTimestamp(
    JNIEnv* env,
    jclass cls
) {
#ifdef _WIN32
    FILETIME ft;
    ULARGE_INTEGER uli;

    GetSystemTimeAsFileTime(&ft);
    uli.LowPart = ft.dwLowDateTime;
    uli.HighPart = ft.dwHighDateTime;

    // Convert to milliseconds since Unix epoch
    return (jlong)((uli.QuadPart - 116444736000000000ULL) / 10000ULL);
#else
    struct timeval tv;
    gettimeofday(&tv, NULL);
    return (jlong)(tv.tv_sec * 1000LL + tv.tv_usec / 1000LL);
#endif
}

/**
 * nCheckIntegrity - Basic integrity check
 * Returns 0 if valid
 */
JNIEXPORT jint JNICALL Java_dev_just_protect_NativeLib_nCheckIntegrity(
    JNIEnv* env,
    jclass cls,
    jstring className
) {
    if (className == NULL) {
        return -1;
    }

    const char* name = (*env)->GetStringUTFChars(env, className, NULL);
    if (name == NULL) {
        return -2;
    }

    // Basic check - ensure class name starts with expected package
    int result = 0;
    if (strncmp(name, "dev.just.", 9) != 0 &&
        strncmp(name, "dev/just/", 9) != 0) {
        result = 1;
    }

    (*env)->ReleaseStringUTFChars(env, className, name);
    return result;
}

// ============================================================
// LIBRARY INITIALIZATION
// ============================================================

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    (void)reserved;

    JNIEnv* env;
    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_8) != JNI_OK) {
        return JNI_ERR;
    }

    // Library loaded successfully
    return JNI_VERSION_1_8;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    (void)vm;
    (void)reserved;
    // Cleanup if needed
}
