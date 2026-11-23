cmake_minimum_required(VERSION 3.22.1)

project(emerald VERSION 0.1.0 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Native core, loaded by the launcher through System.loadLibrary("emerald").
add_library(emerald SHARED
    src/jni/emerald_jni.cpp
)

target_include_directories(emerald PRIVATE src)

# Single source of truth for the native version: project(... VERSION ...) above.
target_compile_definitions(emerald PRIVATE EMERALD_VERSION="${PROJECT_VERSION}")

target_compile_options(emerald PRIVATE -Wall -Wextra)

if(ANDROID)
    # jni.h comes from the NDK sysroot; liblog provides __android_log_print.
    target_link_libraries(emerald PRIVATE log)
else()
    # Host configure/compile is only a sanity check of the sources (not a
    # supported runtime target). It needs the JDK's JNI headers.
    find_package(JNI REQUIRED)
    target_include_directories(emerald PRIVATE ${JNI_INCLUDE_DIRS})
endif()

# Reserved for the future mod system. Nothing is built from these directories yet.
#   src/mods/         -> mod discovery/loading (not implemented)
#   third_party/      -> vendored native dependencies (none yet)
