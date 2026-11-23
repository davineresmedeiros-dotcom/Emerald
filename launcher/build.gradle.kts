plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.emerald.launcher"
    compileSdk = 36

    // Default NDK of AGP 9.4.0.
    ndkVersion = "28.2.13676358"

    defaultConfig {
        applicationId = "dev.emerald.launcher"
        // Launcher-side choice, not derived from Geometry Dash requirements.
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        ndk {
            // 64-bit only for now.
            abiFilters += listOf("arm64-v8a")
        }
    }

    externalNativeBuild {
        cmake {
            path = file("../native/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
