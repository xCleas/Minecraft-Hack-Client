@echo off
REM JustClient Native Build Script - Windows
REM Requires: CMake, Visual Studio or MinGW

setlocal enabledelayedexpansion

echo ============================================
echo JustClient Native Library Builder - Windows
echo ============================================

REM Check CMake
where cmake >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: CMake not found. Please install CMake.
    exit /b 1
)

REM Create build directory
if not exist "build-windows" mkdir build-windows
cd build-windows

REM Configure with CMake
echo.
echo Configuring...
cmake -G "Visual Studio 17 2022" -A x64 -DCMAKE_BUILD_TYPE=Release ..

if %ERRORLEVEL% neq 0 (
    echo ERROR: CMake configuration failed
    cd ..
    exit /b 1
)

REM Build
echo.
echo Building...
cmake --build . --config Release

if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed
    cd ..
    exit /b 1
)

REM Copy to resources
echo.
echo Copying to resources...
set OUTPUT_DIR=..\..\..\src\main\resources\natives\windows-x64
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"
copy /Y "lib\Release\justcore.dll" "%OUTPUT_DIR%\justcore.dll"

if %ERRORLEVEL% neq 0 (
    copy /Y "lib\justcore.dll" "%OUTPUT_DIR%\justcore.dll"
)

cd ..

echo.
echo ============================================
echo Build complete!
echo Output: src\main\resources\natives\windows-x64\justcore.dll
echo ============================================
