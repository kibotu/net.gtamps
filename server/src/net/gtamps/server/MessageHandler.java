package net.gtamps.server;

import java.nio.channels.ClosedChannelException;

import org.jdom.Element;

import net.gtamps.XmlElements;
import net.gtamps.server.xsocket.XSocketHandler;

public class MessageHandler implements IHandler {
	
	private CommandHandler commandHandler;
	private RequestHandler requestHandler;
	private XSocketHandler socketHandler;

	public MessageHandler(CommandHandler commandHandler,
			RequestHandler requestHandler) {
		this.commandHandler = commandHandler;
		this.requestHandler = requestHandler;
		commandHandler.setMessageHandler(this);
		requestHandler.setMessageHandler(this);
	}

	public void setSocketHandler(XSocketHandler socketHandler) {
		this.socketHandler = socketHandler;
	}
	
	@Override
	public void addConnection(String connectionId) {
		this.requestHandler.addConnection(connectionId);
	}

	@Override
	public void removeConnection(String connectionId) {
		this.requestHandler.removeConnection(connectionId);
	}
	
	@Override
	public void receive(String connectionId, Element message) {
		if (message == null) {
			return;
		}
		Element cmdElement = message.getChild(XmlElements.COMMAND.tagName());
		Element reqElement = message.getChild(XmlElements.REQUESTS.tagName());
		commandHandler.receive(connectionId, cmdElement);
		requestHandler.receive(connectionId, reqElement);
	}

	@Override
	public void send(String connectionId, Element message) throws ClosedChannelException {
		Element msgElement = new Element(XmlElements.MESSAGE.tagName());
		if (message != null) {
			msgElement.addContent(message);
		}
		this.socketHandler.send(connectionId, msgElement);
	}
	

}
