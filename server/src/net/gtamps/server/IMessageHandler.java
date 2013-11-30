package net.gtamps.server;

import net.gtamps.shared.serializer.communication.NewMessage;

public interface IMessageHandler {

	public abstract void receiveMessage(Connection<?> c, NewMessage msg);

}