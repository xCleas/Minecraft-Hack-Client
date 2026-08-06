#!/bin/bash
# JustClient Native Build Script - Linux
# Requires: CMake, GCC, JDK

set -e

echo "============================================"
echo "JustClient Native Library Builder - Linux"
echo "============================================"

# Check dependencies
command -v cmake >/dev/null 2>&1 || { echo "ERROR: CMake not found"; exit 1; }
command -v gcc >/dev/null 2>&1 || { echo "ERROR: GCC not found"; exit 1; }

# Check JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "WARNING: JAVA_HOME not set, trying to detect..."
    JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
    export JAVA_HOME
fi

echo "Using JAVA_HOME: $JAVA_HOME"

# Create build directory
mkdir -p build-linux
cd build-linux

# Configure
echo ""
echo "Configuring..."
cmake -DCMAKE_BUILD_TYPE=Release ..

# Build
echo ""
echo "Building..."
cmake --build . --config Release

# Copy to resources
echo ""
echo "Copying to resources..."
OUTPUT_DIR="../../src/main/resources/natives/linux-x64"
mkdir -p "$OUTPUT_DIR"
cp -f lib/libjustcore.so "$OUTPUT_DIR/"

cd ..

echo ""
echo "============================================"
echo "Build complete!"
echo "Output: src/main/resources/natives/linux-x64/libjustcore.so"
echo "============================================"
