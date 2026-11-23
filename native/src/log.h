#pragma once

// Minimal logging used by the native core.
// On Android it forwards to logcat; on other hosts it writes to stderr so the
// sources can be compiled outside the NDK for a quick sanity check.

#include <cstdio>

#define EMERALD_LOG_TAG "Emerald"

#ifdef __ANDROID__
#include <android/log.h>

#define EMERALD_LOGI(...) \
    __android_log_print(ANDROID_LOG_INFO, EMERALD_LOG_TAG, __VA_ARGS__)
#define EMERALD_LOGE(...) \
    __android_log_print(ANDROID_LOG_ERROR, EMERALD_LOG_TAG, __VA_ARGS__)
#else
#define EMERALD_LOG_HOST(level, ...)                              \
    do {                                                          \
        std::fprintf(stderr, "[" EMERALD_LOG_TAG "/" level "] "); \
        std::fprintf(stderr, __VA_ARGS__);                        \
        std::fputc('\n', stderr);                                 \
    } while (0)

#define EMERALD_LOGI(...) EMERALD_LOG_HOST("I", __VA_ARGS__)
#define EMERALD_LOGE(...) EMERALD_LOG_HOST("E", __VA_ARGS__)
#endif
