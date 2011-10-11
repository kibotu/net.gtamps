package net.gtamps.server;

import java.util.Map;

import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.ISendable;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public class MessageCenter implements IResponseHandler {
	
	private Map<String, Session> sessions;
	private Map<Integer, Connection> connectionsByRequestId;
	private final IRequestHandler requestHandler;
	private final ICommandHandler commandHandler;
	
	public MessageCenter(IRequestHandler reqHandler, ICommandHandler cmdHandler) {
		if (reqHandler == null) {
			throw new IllegalArgumentException("'reqHandler' must not be null");
		}
		if (cmdHandler == null) {
			throw new IllegalArgumentException("'cmdHandler' must not be null");
		}
		this.requestHandler = reqHandler;
		this.commandHandler = cmdHandler;
	}

	public void receiveMessage(Connection connection, Message message) {
		Session session = getSessionForMessage(message);
		for (ISendable part : message.sendables) {
			if (part == null) {
				continue;
			}
			if (part instanceof Request) {
				Request r = (Request) part;
				connectionsByRequestId.put(r.id, connection);
				requestHandler.handleRequest(session, r);
			} else if (part instanceof Command) {
				commandHandler.handleCommand(session, (Command) part);
			} else {
				// unknown part. ignore.
			}
		}
	}
	
	private Session getSessionForMessage(Message message) {
		String id = message.getSessionId();
		Session session = null;
		if (id == null || id.equals("") || !sessions.containsKey(id)) {
			session = new Session();
			sessions.put(session.getId(), session);
		} else {
			session = sessions.get(id);
		}
		return session;
	}

	public void handleResponse(Response response) {
	}
	
	public void sendInMessage(Connection connection, ISendable sendable, String sessionId) {
		Message msg = new Message();
		msg.setSessionId(sessionId);
		msg.addSendable(sendable);
		connection.send(msg);
	}

	@Override
	public void handleResponse(Session s, Response response) {
		Connection c = connectionsByRequestId.get(response.requestId);
		sendInMessage(c, response, s.getId());
	}
	
	
}
