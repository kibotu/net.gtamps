package net.gtamps.shared.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.System.err;
import static java.lang.System.out;

public class JavaLogger implements ILogger {

    @Override
    public void toast(@NotNull String id, String message) {
//        new AlertBox(id, message);
    }

    @Override
    public void d(@NotNull String id, @Nullable String message) {
        out.printf("%-15s%s%s%n", id,"   D   ", message);
    }

    @Override
    public void v(@NotNull String id, @Nullable String message) {
        out.printf("%-15s%s%s%n", id,"   V   ", message);
    }

    @Override
    public void i(@NotNull String id, @Nullable String message) {
        out.printf("%-15s%s%s%n", id,"   I   ", message);
    }

    @Override
    public void w(@NotNull String id, @Nullable String message) {
        err.printf("%-15s%s%s%n", id,"   W   ", message);
    }

    @Override
    public void e(@NotNull String id, @Nullable String message) {
        err.printf("%-15s%s%s%n", id,"   E   ", message);
    }

    @Override
    public void save(String filename) {
    }

    @Override
    public void checkGlError(@NotNull String id, @Nullable String operation) {
    }
}
