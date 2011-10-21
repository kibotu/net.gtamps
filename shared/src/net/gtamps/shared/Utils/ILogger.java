package net.gtamps.shared.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ILogger {

    public void toast(String message);

    /** DEBUG **/
    public void d(@NotNull String id, @Nullable String message);

    /** VERBOSE **/
    public void v(@NotNull String id, @Nullable String message);

    /** INFO **/
    public void i(@NotNull String id, @Nullable String message);

    /** WARN **/
    public void w(@NotNull String id, @Nullable String message);

    /** ERROR **/
    public void e(@NotNull String id, @Nullable String message);

    /** SAVE TO FILE **/
    public void save(String filename);
}
