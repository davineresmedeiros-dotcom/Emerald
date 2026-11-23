package dev.emerald.launcher;

/**
 * Java side of the JNI boundary to the Emerald native core (libemerald.so).
 *
 * <p>All native calls go through this class so that loading failures are handled in a
 * single place and the rest of the launcher never touches JNI directly.
 */
public final class NativeBridge {

    private static final String LIBRARY_NAME = "emerald";

    private static boolean loaded;
    private static Throwable loadError;

    private NativeBridge() {
    }

    /**
     * Loads the native core if it is not loaded yet.
     *
     * @return {@code true} if the library is loaded and native calls may be used
     */
    public static synchronized boolean load() {
        if (loaded) {
            return true;
        }
        try {
            System.loadLibrary(LIBRARY_NAME);
            loaded = true;
            loadError = null;
        } catch (UnsatisfiedLinkError e) {
            loadError = e;
        }
        return loaded;
    }

    /** Returns the error from the last failed {@link #load()}, or {@code null}. */
    public static synchronized Throwable getLoadError() {
        return loadError;
    }

    /**
     * Returns the version of the native core.
     *
     * @throws IllegalStateException if the native core is not loaded
     */
    public static String getVersion() {
        if (!load()) {
            throw new IllegalStateException("Emerald native core is not loaded", loadError);
        }
        return nativeGetVersion();
    }

    private static native String nativeGetVersion();
}
