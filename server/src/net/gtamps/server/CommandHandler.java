package net.gtamps.server;

import net.gtamps.Command;
import net.gtamps.XmlElements;

import java.nio.channels.ClosedChannelException;
import java.util.List;

import org.jdom.Element;

public class CommandHandler implements IHandler {

	private ConnectionManager connectionManager;
	private MessageHandler messageHandler;
	
	
	public CommandHandler(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		connectionManager.setCommandHandler(this);
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void addConnection(String connectionId) {
		this.connectionManager.addConnection(connectionId);
	}

	@Override
	public void removeConnection(String connectionId) {
		this.connectionManager.removeConnection(connectionId);
	}
	
	@Override
	public void receive(String connectionId, Element message) {
		if (message == null) {
			return;
		}
		List<Element> requests = message.getChildren(XmlElements.ACTION.tagName());
		//List<Element> responses = new ArrayList<Element>(requests.size());
		for (Element element : requests) {	
			String value = element.getAttribute("type").getValue().toUpperCase();
			try {
				Command command = Command.valueOf(value.toUpperCase());
				connectionManager.command(connectionId, command);
				
			} catch (IllegalArgumentException e) {
				System.err.println("bad command: " + value);
				continue;
			}
			
		}
	}

	@Override
	public void send(String connectionId, Element message) throws ClosedChannelException {
		Element msgElement = new Element(XmlElements.COMMAND.tagName());
		msgElement.addContent(message);
		this.messageHandler.send(connectionId, msgElement);
	}

}
