# native/src/mods

Reserved for the native side of the future Emerald mod system.

**Nothing is implemented here, and nothing in this directory is built yet.**

Intended scope, once the mod format and the way Emerald enters the game process are decided (see `docs/architecture.md`):

- discovering mods that are installed on the device;
- validating a mod before it is loaded;
- loading and unloading mods, and tracking their state.

When real code is added here, list its sources in `native/CMakeLists.txt`.
