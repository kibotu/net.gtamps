package net.gtamps.shared.serializer;

import net.gtamps.shared.serializer.communication.Message;
import org.jetbrains.annotations.NotNull;

public interface IMessageManager {
    public boolean add(@NotNull Message message);

    public Message poll();

    public boolean isEmpty();
}
