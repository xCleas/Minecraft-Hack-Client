#!/bin/bash
# JustClient Native Build Script - macOS
# Requires: CMake, Xcode Command Line Tools, JDK

set -e

echo "============================================"
echo "JustClient Native Library Builder - macOS"
echo "============================================"

# Check dependencies
command -v cmake >/dev/null 2>&1 || { echo "ERROR: CMake not found"; exit 1; }
command -v clang >/dev/null 2>&1 || { echo "ERROR: Xcode CLT not found"; exit 1; }

# Check JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "WARNING: JAVA_HOME not set, trying to detect..."
    if [ -x "/usr/libexec/java_home" ]; then
        JAVA_HOME=$(/usr/libexec/java_home)
    fi
    export JAVA_HOME
fi

echo "Using JAVA_HOME: $JAVA_HOME"

# Create build directory
mkdir -p build-macos
cd build-macos

# Configure (Universal binary: x64 + arm64)
echo ""
echo "Configuring..."
cmake -DCMAKE_BUILD_TYPE=Release \
      -DCMAKE_OSX_ARCHITECTURES="x86_64;arm64" \
      ..

# Build
echo ""
echo "Building..."
cmake --build . --config Release

# Copy to resources (x64 and arm64 combined in universal binary)
echo ""
echo "Copying to resources..."

# x64
OUTPUT_DIR_X64="../../src/main/resources/natives/macos-x64"
mkdir -p "$OUTPUT_DIR_X64"
cp -f lib/libjustcore.dylib "$OUTPUT_DIR_X64/"

# arm64 (same universal binary)
OUTPUT_DIR_ARM64="../../src/main/resources/natives/macos-arm64"
mkdir -p "$OUTPUT_DIR_ARM64"
cp -f lib/libjustcore.dylib "$OUTPUT_DIR_ARM64/"

cd ..

echo ""
echo "============================================"
echo "Build complete!"
echo "Output: src/main/resources/natives/macos-x64/libjustcore.dylib"
echo "Output: src/main/resources/natives/macos-arm64/libjustcore.dylib"
echo "============================================"
