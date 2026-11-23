# Architecture

This document describes the initial architecture of Emerald and, just as importantly, what is still undecided. Emerald is at the scaffolding stage: only the pieces described under "Implemented" exist.

## Components

### Launcher (`launcher/`)

An Android application module written in Java. Today it contains:

- `MainActivity`: loads the native core and displays its version, or the load error.
- `NativeBridge`: the only class that declares native methods and loads `libemerald.so`.
- `mods/`: an empty package reserved for the mod system.

### Native core (`native/`)

A C++17 shared library, `libemerald.so`, built with CMake and packaged by the Android Gradle Plugin for `arm64-v8a` and `armeabi-v7a`.

- `src/jni/emerald_jni.cpp`: `JNI_OnLoad` and the native methods that match `NativeBridge`.
- `src/log.h`: logging macros (logcat on Android, stderr elsewhere).
- `src/mods/` and `third_party/`: reserved, see their READMEs.

### JNI boundary

The Java and C++ sides meet only in `NativeBridge` and `emerald_jni.cpp`. Native method names use the standard JNI naming convention (`Java_dev_emerald_launcher_NativeBridge_...`). If the number of native methods grows, switching to explicit `RegisterNatives` in `JNI_OnLoad` is a reasonable change.

## Implemented

- Gradle build (AGP 9.4.0) that builds the launcher and the native core together.
- Native core exposing its version through JNI.
- Launcher that loads the native core and reports the result.

## Not implemented

- Anything that touches the Geometry Dash process.
- The mod system (discovery, validation, loading, API).

## Reserved for the mod system

| Location | Purpose |
|---|---|
| `native/src/mods/` | Native mod discovery, validation and loading |
| `launcher/src/main/java/dev/emerald/launcher/mods/` | Launcher-side mod management |
| `native/third_party/` | Vendored native dependencies |

These locations exist only to fix the layout. They contain documentation, not code.

## Open design questions

These must be answered, and recorded here, before the corresponding code is written:

1. **Entry into the game process.** How Emerald's native code is loaded alongside Geometry Dash on a non-rooted Android device (for example, by modifying and re-signing the game's APK) is not decided.
2. **Game versions and architectures.** How Emerald identifies the installed Geometry Dash build and how it copes with updates is not decided.
3. **Mod package format.** What a mod is on disk, what metadata it carries and how it is validated is not decided.
4. **Mod API.** What mods can call into, and how that surface stays stable across game versions, is not decided.
5. **Storage.** Where mods and their data live, given Android's storage restrictions, is not decided.

## Build verification status

The native core has been compiled and exercised through JNI on a Linux host. The full Android build has not been run yet; see the README for details.
