package net.gtamps.server;

import net.gtamps.XmlElements;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class RequestHandler implements IHandler {

	private ConnectionManager connectionManager;
	private MessageHandler messageHandler;

	public RequestHandler(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		connectionManager.setRequestHandler(this);
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
		List<Element> requests = message.getChildren(XmlElements.SINGLE_REQUEST.tagName());
		List<Element> responses = new ArrayList<Element>(requests.size());
		for (Element request : requests) {
			String value = request.getAttribute("type").getValue().toLowerCase();
			RequestTypes reqtype;
			try {
				reqtype = RequestTypes.findByTypeString(value);
			} catch (IllegalArgumentException e) {
				System.err.println("bad request: " + value);
				continue;
			}
			Element response = null;
			switch (reqtype) {
			case GETMAPDATA:
				response = connectionManager.getMapData(connectionId);
				break;
			case JOIN:
				response = connectionManager.joinGame(connectionId);
				break;
			case LEAVE:
				response = connectionManager.leaveGame(connectionId);
				break;
			case GETPLAYER:
				response = connectionManager.getPlayer(connectionId);
				break;
			case IDENTIFY:
				String name = request.getAttributeValue(XmlElements.ATTRIB_USER
						.tagName());
				String passw = request
						.getAttributeValue(XmlElements.ATTRIB_PASSW.tagName());
				if (name == null) {
					name = "";
				}
				response = connectionManager.identifyConnection(connectionId,
						name, passw);
				break;
			case GETUPDATE:
				String revStr = request
						.getAttributeValue(XmlElements.ATTRIB_VALUE.tagName());
				Integer rev = null;
				try {
					rev = Integer.valueOf(revStr);
				} catch (NumberFormatException e) {
					rev = -1; // send bad argument
				}
				response = connectionManager
						.updateToRevision(connectionId, rev);
				break;
			}
			if (response != null) {
				responses.add(response);
			}
			// TODO sendall
		}
		try {
			Element toSend = (responses.size() == 0) ? null
					: joinElements(responses);
			messageHandler.send(connectionId, toSend);
		} catch (ClosedChannelException e) {
			connectionManager.removeConnection(connectionId);
		}
	}

	@Override
	public void send(String connectionId, Element message)
			throws ClosedChannelException {
		Element msgElement = new Element(XmlElements.RESPONSES.tagName());
		msgElement.addContent(message);
		this.messageHandler.send(connectionId, msgElement);
	}

	public Element joinElements(List<Element> elements) {
		Element msgElement = new Element(XmlElements.RESPONSES.tagName());
		for (Element element : elements) {
			if (element != null) {
				msgElement.addContent(element);
			}
		}
		return msgElement;
	}

}
