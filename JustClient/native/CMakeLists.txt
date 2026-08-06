cmake_minimum_required(VERSION 3.16)
project(justcore C)

# C Standard
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# Find JNI
find_package(JNI REQUIRED)

# Include directories
include_directories(${JNI_INCLUDE_DIRS})

# Source files
set(SOURCES
    src/justcore.c
)

# Create shared library
add_library(justcore SHARED ${SOURCES})

# Platform-specific settings
if(WIN32)
    # Windows
    target_compile_definitions(justcore PRIVATE _WIN32)
    set_target_properties(justcore PROPERTIES
        PREFIX ""
        OUTPUT_NAME "justcore"
        SUFFIX ".dll"
    )
    # Strip symbols in release
    if(CMAKE_BUILD_TYPE STREQUAL "Release")
        target_link_options(justcore PRIVATE /OPT:REF /OPT:ICF)
    endif()

elseif(APPLE)
    # macOS
    set_target_properties(justcore PROPERTIES
        PREFIX "lib"
        OUTPUT_NAME "justcore"
        SUFFIX ".dylib"
    )
    # Universal binary (x64 + arm64)
    set(CMAKE_OSX_ARCHITECTURES "x86_64;arm64")
    # Strip in release
    if(CMAKE_BUILD_TYPE STREQUAL "Release")
        add_custom_command(TARGET justcore POST_BUILD
            COMMAND strip -x $<TARGET_FILE:justcore>
        )
    endif()

else()
    # Linux
    set_target_properties(justcore PROPERTIES
        PREFIX "lib"
        OUTPUT_NAME "justcore"
        SUFFIX ".so"
    )
    # Strip in release
    if(CMAKE_BUILD_TYPE STREQUAL "Release")
        add_custom_command(TARGET justcore POST_BUILD
            COMMAND strip $<TARGET_FILE:justcore>
        )
    endif()
endif()

# Optimization flags for release
if(CMAKE_BUILD_TYPE STREQUAL "Release")
    if(MSVC)
        target_compile_options(justcore PRIVATE /O2 /GL)
    else()
        target_compile_options(justcore PRIVATE -O3 -fvisibility=hidden)
    endif()
endif()

# Link libraries
target_link_libraries(justcore ${JNI_LIBRARIES})

# Output directory
set_target_properties(justcore PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/lib"
    RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}/lib"
)

# Installation
install(TARGETS justcore
    LIBRARY DESTINATION lib
    RUNTIME DESTINATION lib
)
