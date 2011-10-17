package net.gtamps.server;

import net.gtamps.shared.communication.Message;

public interface IMessageHandler {

	public abstract void receiveMessage(Connection<?,?> c, Message msg);

}