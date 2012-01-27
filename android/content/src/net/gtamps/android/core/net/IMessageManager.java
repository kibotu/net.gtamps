package net.gtamps.android.core.net;

import net.gtamps.shared.serializer.communication.NewMessage;

import org.jetbrains.annotations.NotNull;

public interface IMessageManager {
	public boolean add(@NotNull NewMessage message);

	public NewMessage poll();

	public boolean isEmpty();
}
