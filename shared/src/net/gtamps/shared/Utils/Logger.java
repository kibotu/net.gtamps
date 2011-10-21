package net.gtamps.shared.Utils;

import net.gtamps.shared.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

final public class Logger {

    @NotNull
    public static final ConcurrentHashMap<String,Boolean> blacklist = new ConcurrentHashMap<String, Boolean>();

    @Nullable
    private static volatile ILogger logger;

    private static volatile boolean enableSaveFile;

    private static final String LINE_BREAKS = "\n\n\n\n\n";

    public enum Level {
        DEBUG, VERBOSE, INFO, WARN, ERROR
    }

    /**
     * Utility class.
     */
    private Logger() {
    }

    /**
     * DEBUG *
     */
    public static void d(@NotNull String id, @NotNull String message) {
        if(allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.DEBUG) >= 0) {
            logger.d(id,message);
        }
    }

    public static void D(@NotNull String id, @NotNull String message) {
        d(id, LINE_BREAKS + message + LINE_BREAKS);
    }

    public static void d(@NotNull Object id, @NotNull String message) {
       d(id.getClass().getSimpleName(),message);
    }

    public static void D(@NotNull Object id, @NotNull String message) {
       d(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
    }

    /**
     * VERBOSE *
     */

    public static void v(@NotNull String id, @NotNull String message) {
        if(allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.VERBOSE) >= 0) {
            logger.d(id,message);
        }
    }

    public static void V(@NotNull String id, @NotNull String message) {
        v(id, LINE_BREAKS + message + LINE_BREAKS);
    }

    public static void v(@NotNull Object id, @NotNull String message) {
       v(id.getClass().getSimpleName(), message);
    }

    public static void V(@NotNull Object id, @NotNull String message) {
       v(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
    }

    /**
     * INFO *
     */

    public static void i(@NotNull String id, @NotNull String message) {
        if(allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.INFO) >= 0) {
            logger.i(id,message);
        }
    }

    public static void I(@NotNull String id, @NotNull String message) {
        i(id, LINE_BREAKS + message + LINE_BREAKS);
    }

    public static void i(@NotNull Object id, @NotNull String message) {
       i(id.getClass().getSimpleName(),message);
    }

    public static void I(@NotNull Object id, @NotNull String message) {
       i(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
    }

    /**
     * WARN *
     */

    public static void w(@NotNull String id, @NotNull String message) {
        if(allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.WARN) >= 0) {
            logger.w(id, message);
        }
    }

    public static void W(@NotNull String id, @NotNull String message) {
        w(id, LINE_BREAKS + message + LINE_BREAKS);
    }

    public static void w(@NotNull Object id, @NotNull String message) {
       w(id.getClass().getSimpleName(),message);
    }

    public static void W(@NotNull Object id, @NotNull String message) {
       w(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
    }

    /**
     * ERROR *
     */

    public static void e(@NotNull String id, @NotNull String message) {
        if(allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.ERROR) >= 0) {
            logger.e(id, message);
        }
    }

    public static void E(@NotNull String id, @NotNull String message) {
        e(id, LINE_BREAKS + message + LINE_BREAKS);
    }

    public static void e(@NotNull Object id, @NotNull String message) {
       e(id.getClass().getSimpleName(), message);
    }

    public static void E(@NotNull Object id, @NotNull String message) {
       e(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
    }

    /**
     * SAVE TO FILE *
     */
    public static void enableDumpFile(boolean enable) {
        enableSaveFile = enable;
    }

    public static void save(String filename) {
        if(logger != null && enableSaveFile) {
            logger.save(filename);
        }
    }

    /**
     * CHECK AGAINST BLACKLIST AND IF NULL LOG *
     */
    private static boolean allowLogging(@NotNull String id) {
        return logger != null && !blacklist.contains(id);
    }

    private static boolean allowLogging(@NotNull Object id) {
        return allowLogging(id.getClass().getSimpleName());
    }

    public static void setLogger(ILogger logger) {
        Logger.logger = logger;
    }
}
