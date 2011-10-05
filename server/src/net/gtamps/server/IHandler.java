package net.gtamps.server;

import java.nio.channels.ClosedChannelException;

import org.jdom.Element;

public interface IHandler {
	public void addConnection(String connectionId);
	public void removeConnection(String connectionId);
	public void receive(String connectionId, Element message);
	public void send(String connectionId, Element message) throws ClosedChannelException;

}
