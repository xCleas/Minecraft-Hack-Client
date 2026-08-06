#!/bin/bash
#
# JustClient Native Protection Library Build Script
#
# Prerequisites:
#   - GCC or Clang compiler
#   - JAVA_HOME environment variable set
#
# Usage: ./build.sh

echo ""
echo "======================================"
echo "  JustClient Native Library Builder"
echo "======================================"
echo ""

# Check JAVA_HOME
if [ -z "$JAVA_HOME" ]; then
    echo "ERROR: JAVA_HOME environment variable is not set"
    echo "Please set JAVA_HOME to your JDK installation directory"
    exit 1
fi

echo "JAVA_HOME: $JAVA_HOME"

# Detect OS
OS=$(uname -s)
ARCH=$(uname -m)

echo "OS: $OS"
echo "Architecture: $ARCH"

# Create output directory
mkdir -p ../build/native

# Set compiler flags based on OS
case "$OS" in
    "Linux")
        INCLUDES="-I$JAVA_HOME/include -I$JAVA_HOME/include/linux"
        OUTPUT="../build/native/libjustclient.so"
        LDFLAGS="-shared -fPIC"
        ;;
    "Darwin")
        INCLUDES="-I$JAVA_HOME/include -I$JAVA_HOME/include/darwin"
        OUTPUT="../build/native/libjustclient.dylib"
        LDFLAGS="-shared -fPIC -dynamiclib"
        ;;
    *)
        echo "ERROR: Unsupported OS: $OS"
        exit 1
        ;;
esac

echo ""
echo "Compiling..."
gcc -O2 $LDFLAGS $INCLUDES src/native_protect.c -o $OUTPUT

if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi

echo ""
echo "======================================"
echo "  Build Complete!"
echo "======================================"
echo ""
echo "Output files:"
echo "  - $OUTPUT"
echo ""
echo "To use:"
echo "  1. Copy to your application directory, or"
echo "  2. Add to java.library.path, or"
echo "  3. Place in src/main/resources/native/<os>/<arch>/"
echo ""
