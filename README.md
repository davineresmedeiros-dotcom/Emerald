# Emerald

Independent Android mod loader for Geometry Dash.

> **Status: early scaffolding.** The repository currently contains only the project base. Emerald does **not** load mods yet and does **not** interact with Geometry Dash in any way.

Emerald is an independent project. It is not affiliated with, endorsed by, or sponsored by RobTop Games or Geometry Dash. It is not based on, and does not contain code from, any other mod loader.

## Goal

Give Geometry Dash on Android a mod loader that is open (MIT) and independent: something that can bring mod code into the game, give mods a stable API, and manage which mods are installed and enabled.

## Current state

What exists today:

- An Android launcher module (`launcher/`, Java) with a single activity that loads the native core and shows its version.
- A native C++ core (`native/`) built into `libemerald.so`, exposing the JNI entry points used by the launcher (`JNI_OnLoad` and `NativeBridge.nativeGetVersion`).
- Gradle build configuration with the Android Gradle Plugin driving the CMake build of the native core. Only 64-bit (`arm64-v8a`) is built for now.
- A GitHub Actions workflow (`.github/workflows/ci.yml`) that builds the native core on a host and the debug APK on Android.
- Reserved, empty locations for the future mod system (`native/src/mods/`, `launcher/.../mods/`) and for vendored native dependencies (`native/third_party/`).

What does **not** exist yet:

- Any integration with the Geometry Dash process (injection, hooking, version handling).
- Mod discovery, validation or loading.
- A mod API.
- Automated tests.
- A Gradle wrapper (see [Building](#building)).

### Verification status

- The native core has been compiled on a Linux host (GCC and Clang, `-Wall -Wextra`) against the JDK's JNI headers, and the JNI boundary was exercised from a JVM (`System.loadLibrary("emerald")` followed by `getVersion()`).
- The CI workflow has been added, but its result has **not** been confirmed yet. Until a run succeeds, the Gradle/AGP/NDK configuration is based on the official compatibility documentation and is unverified. Check the Actions tab for the current state.
- The launcher activity has not been run on a device or emulator.

## Architecture (initial)

```
+-----------------------------+        JNI        +-----------------------------+
| Launcher (Java, Android app)| <---------------> | Native core (C++, libemerald)|
| launcher/                   |   NativeBridge    | native/                      |
+-----------------------------+                   +-----------------------------+
```

- **Launcher**: the Android application. Today it only loads the native core and reports its version. Over time it is the place for user-facing flows.
- **Native core**: the C++ shared library. It is the part that will eventually deal with the game process and with mods.
- **JNI boundary**: all native calls go through `NativeBridge`, so library loading and error handling live in one place.

See [docs/architecture.md](docs/architecture.md) for details and for the open design questions.

## Repository layout

```
.
├── .github/workflows/ci.yml    CI: host build of the native core + Android debug APK
├── build.gradle.kts            Root Gradle build (plugin declarations)
├── settings.gradle.kts         Project name, modules, repositories
├── gradle.properties           Gradle settings
├── gradle/libs.versions.toml   Version catalog (AGP version)
├── launcher/                   Android application module
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/dev/emerald/launcher/
│       │   ├── MainActivity.java
│       │   ├── NativeBridge.java
│       │   └── mods/           Reserved for the mod system
│       └── res/values/strings.xml
├── native/                     C++ core
│   ├── CMakeLists.txt
│   ├── src/
│   │   ├── jni/emerald_jni.cpp JNI entry points
│   │   ├── log.h               Logging (logcat on Android, stderr on host)
│   │   └── mods/               Reserved for the mod system
│   └── third_party/            Reserved for vendored native dependencies
└── docs/architecture.md
```

## Building

Toolchain the configuration targets:

| Component | Version |
|---|---|
| Android Gradle Plugin | 9.4.0 |
| Gradle | 9.6.0 (minimum required by AGP 9.4.0) |
| JDK | 17 or newer |
| SDK Build-Tools | 36.0.0 |
| Android platform | API 36 |
| NDK | 28.2.13676358 (AGP 9.4.0 default) |
| CMake | 3.22.1 (SDK Manager) |

The usual way to build is the GitHub Actions workflow: every push to `main` builds the debug APK and uploads it as the `emerald-launcher-debug` artifact. It can also be started manually from the Actions tab.

To build locally, the Gradle wrapper is not committed yet. With Gradle installed, generate it once and then build:

```sh
gradle wrapper --gradle-version 9.6.0
./gradlew :launcher:assembleDebug
```

An Android SDK must be available (`ANDROID_HOME` or `local.properties` with `sdk.dir`).

The native core can also be configured and compiled on a regular host as a quick sanity check of the sources (this is not a supported runtime target):

```sh
cmake -S native -B build/host
cmake --build build/host
```

## Roadmap

Planned, in rough order and subject to change:

1. Decide and document how Emerald gets into the game process on Android.
2. Handle differences between Geometry Dash versions (64-bit / `arm64-v8a` only for now).
3. Define the mod package format and the mod API.
4. Implement mod discovery, validation and loading.
5. Add automated tests.

## License

MIT. See [LICENSE](LICENSE).
