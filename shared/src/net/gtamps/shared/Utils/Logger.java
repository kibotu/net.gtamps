package net.gtamps.shared.Utils;

import net.gtamps.shared.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentLinkedQueue;

final public class Logger {

    @NotNull
    public static final ConcurrentLinkedQueue<String> blacklist = new ConcurrentLinkedQueue<String>();

    @Nullable
    private static volatile ILogger logger;

    private static volatile boolean enableSaveFile;

    private static final String LINE_BREAKS = "\n\n\n\n\n";

    public static void checkGlError(Object id, String message) {
        logger.checkGlError(id.getClass().getSimpleName(), message);
    }

    public enum Level {
        DEBUG_LOG_GL_CALLS, DEBUG_CHECK_GL_ERROR, DEBUG, VERBOSE, INFO, WARN, ERROR, NO_LOGGING,
    }

    /**
     * Utility class.
     */
    private Logger() {
    }

    public static void toast(@NotNull final Object id, final String message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (logger != null && message != null) {
                logger.toast(id instanceof String ? id.toString() : id.getClass().getSimpleName(), message);
            }
        }
    }

    /**
     * DEBUG *
     */
    public static void d(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (message != null && allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.DEBUG) <= 0) {
                logger.d(id, "" + message);
            }
        }
    }

    public static void D(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            d(id, LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    public static void d(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            d(id.getClass().getSimpleName(), message);
        }
    }

    public static void D(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            d(id.getClass().getSimpleName(), LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    /**
     * VERBOSE *
     */

    public static void v(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (message != null && allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.VERBOSE) <= 0) {
                logger.v(id, "" + message);
            }
        }
    }

    public static void V(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            v(id, LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    public static void v(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            v(id.getClass().getSimpleName(), message);
        }
    }

    public static void V(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            v(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
        }
    }

    /**
     * INFO *
     */

    public static void i(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (message != null && allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.INFO) <= 0) {
                logger.i(id, "" + message);
            }
        }
    }

    public static void I(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            i(id, LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    public static void i(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            i(id.getClass().getSimpleName(), message);
        }
    }

    public static void I(@NotNull final Object id, final String message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            i(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
        }
    }

    /**
     * WARN *
     */

    public static void w(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (message != null && allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.WARN) <= 0) {
                logger.w(id, "" + message);
            }
        }
    }

    public static void W(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            w(id, LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    public static void w(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            w(id.getClass().getSimpleName(), message);
        }
    }

    public static void W(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            w(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
        }
    }

    /**
     * ERROR *
     */

    public static void e(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            if (message != null && allowLogging(id) && Config.LOG_LEVEL.compareTo(Level.ERROR) <= 0) {
                logger.e(id, "" + message);
            }
        }
    }

    public static void E(@NotNull final String id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            e(id, LINE_BREAKS + "" + message + LINE_BREAKS);
        }
    }

    public static void e(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            e(id.getClass().getSimpleName(), message);
        }
    }

    public static void E(@NotNull final Object id, final Object message) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            e(id.getClass().getSimpleName(), LINE_BREAKS + message + LINE_BREAKS);
        }
    }

    public static void printException(@NotNull final String id, @NotNull final Exception e) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
        	Logger.e(id,e.toString());
            printStackTrace(id, e.getStackTrace());

            Logger.e(id, e.getMessage());
            if (e.getCause() != null && e.getCause().getStackTrace() != null) {
                Logger.e(id, "-------------------------------------------------------------------------------");
                printStackTrace(id, e.getCause().getStackTrace());
                Logger.e(id, e.getCause().getMessage());
            }
        }
    }

    public static void printStackTrace(@NotNull final String id, @NotNull final StackTraceElement[] stackTrace) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            final StringBuilder error = new StringBuilder();
            
            for (final StackTraceElement stackTraceElement : stackTrace) {
                // error.append(stackTraceElement.toString().substring(Math.max(0,stackTraceElement.toString().length()-80),stackTraceElement.toString().length())
                // + "\n");
                error.append(stackTraceElement + "\n");
            }
            Logger.e(id, error.toString());
        }
    }

    public static void printException(@NotNull final Object id, final Exception e) {
        // remove string allocations
        if (Config.LOG_LEVEL != Logger.Level.NO_LOGGING) {
            printException(id.getClass().getSimpleName(), e);
        }
    }

    /**
     * SAVE TO FILE *
     */
    public static void enableDumpFile(final boolean enable) {
        enableSaveFile = enable;
    }

    public static void save(final String filename) {
        if (logger != null && enableSaveFile) {
            logger.save(filename);
        }
    }

    /**
     * CHECK AGAINST BLACKLIST AND IF NULL LOG *
     */
    private static boolean allowLogging(@NotNull final String id) {
        return logger != null && !blacklist.contains(id);
    }

    private static boolean allowLogging(@NotNull final Object id) {
        return allowLogging(id.getClass().getSimpleName());
    }

    public static void setLogger(@NotNull final ILogger logger) {
        assert logger != null;
        Logger.logger = logger;
    }
}