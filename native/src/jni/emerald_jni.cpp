// JNI boundary of the Emerald native core.
//
// The Java counterpart is dev.emerald.launcher.NativeBridge. Native method
// names follow the standard JNI naming convention, so no explicit
// RegisterNatives call is needed at this stage.

#include <jni.h>

#include "log.h"

#ifndef EMERALD_VERSION
#error "EMERALD_VERSION must be defined by the build system (see native/CMakeLists.txt)"
#endif

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* /*reserved*/) {
    JNIEnv* env = nullptr;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        EMERALD_LOGE("JNI_OnLoad: could not obtain JNIEnv");
        return JNI_ERR;
    }

    EMERALD_LOGI("native core %s loaded", EMERALD_VERSION);
    return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT jstring JNICALL
Java_dev_emerald_launcher_NativeBridge_nativeGetVersion(JNIEnv* env, jclass /*clazz*/) {
    return env->NewStringUTF(EMERALD_VERSION);
}
