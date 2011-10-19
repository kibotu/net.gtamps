package net.gtamps.shared.communication;

import net.gtamps.shared.communication.data.ISendableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Sendable {

    @NotNull
    public final SendableType type;
    @Nullable
    public ISendableData data;

    public Sendable(@NotNull final SendableType type,@Nullable final ISendableData data) {
        this.type = type;
        this.data = data;
    }

    public Sendable(SendableType type) {
        this(type, null);
    }
}
