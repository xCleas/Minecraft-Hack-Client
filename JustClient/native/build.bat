@echo off
REM JustClient Native Protection Library Build Script
REM
REM Prerequisites:
REM   - Visual Studio with C++ build tools (for Windows)
REM   - JAVA_HOME environment variable set
REM
REM Usage: build.bat

echo.
echo ======================================
echo   JustClient Native Library Builder
echo ======================================
echo.

REM Check JAVA_HOME
if "%JAVA_HOME%"=="" (
    echo ERROR: JAVA_HOME environment variable is not set
    echo Please set JAVA_HOME to your JDK installation directory
    exit /b 1
)

echo JAVA_HOME: %JAVA_HOME%

REM Check for cl.exe (MSVC compiler)
where cl >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: cl.exe not found in PATH
    echo Please run this script from Visual Studio Developer Command Prompt
    echo Or run: "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat"
    exit /b 1
)

REM Create output directory
if not exist "..\build\native" mkdir "..\build\native"

REM Compile for x64
echo.
echo Compiling for x64...
cl /LD /O2 /DNDEBUG ^
   /I"%JAVA_HOME%\include" ^
   /I"%JAVA_HOME%\include\win32" ^
   src\native_protect.c ^
   /Fe:..\build\native\justclient.dll ^
   /link /MACHINE:X64

if %ERRORLEVEL% neq 0 (
    echo ERROR: x64 compilation failed
    exit /b 1
)

echo.
echo x64 build successful: build\native\justclient.dll

REM Clean up intermediate files
del *.obj *.exp *.lib 2>nul

echo.
echo ======================================
echo   Build Complete!
echo ======================================
echo.
echo Output files:
echo   - build\native\justclient.dll (Windows x64)
echo.
echo To use:
echo   1. Copy justclient.dll to your application directory, or
echo   2. Add it to java.library.path, or
echo   3. Place in src/main/resources/native/windows/x64/
echo.
