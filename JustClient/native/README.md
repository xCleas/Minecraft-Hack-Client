# JustClient Native Library

Native obfuscation layer for JustClient. Provides:
- String decryption in native code
- Constant-time validation
- Data transformation
- Integrity checks

## Requirements

### Windows
- CMake 3.16+
- Visual Studio 2022 (or MinGW-w64)
- JDK 17+ (JAVA_HOME set)

### Linux
- CMake 3.16+
- GCC 9+
- JDK 17+ (JAVA_HOME set)

### macOS
- CMake 3.16+
- Xcode Command Line Tools
- JDK 17+ (JAVA_HOME set)

## Building

### Windows
```cmd
cd native
build-windows.bat
```

### Linux
```bash
cd native
chmod +x build-linux.sh
./build-linux.sh
```

### macOS
```bash
cd native
chmod +x build-macos.sh
./build-macos.sh
```

## Output

After building, native libraries are copied to:
```
src/main/resources/natives/
├── windows-x64/justcore.dll
├── linux-x64/libjustcore.so
├── macos-x64/libjustcore.dylib
└── macos-arm64/libjustcore.dylib
```

## Usage in Java

```java
import dev.just.protect.NativeLib;

// Library auto-loads on first use
String decrypted = NativeLib.decrypt(encryptedBytes, key);

// Check if native is available
if (NativeLib.isLoaded()) {
    // Using native implementation
} else {
    // Using Java fallback
}
```

## Fallback Mode

If native library fails to load (missing, incompatible, etc.), the system automatically falls back to pure Java implementations. This ensures the client works on all platforms even without native libraries.

## Security Notes

- Native code is harder to reverse engineer than Java bytecode
- String decryption keys are embedded in native code
- Constant-time comparison prevents timing attacks
- Symbols are stripped in release builds
