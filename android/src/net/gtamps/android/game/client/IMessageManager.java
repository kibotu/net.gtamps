package net.gtamps.android.game.client;

import net.gtamps.shared.communication.Message;
import org.jetbrains.annotations.NotNull;

public interface IMessageManager {
    public boolean add(@NotNull Message message);
    public Message poll();
    public boolean isEmpty();
}
