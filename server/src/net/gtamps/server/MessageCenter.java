package net.gtamps.server;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.communication.Command;
import net.gtamps.shared.communication.ISendable;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Request;
import net.gtamps.shared.communication.Response;

public class MessageCenter implements IResponseHandler {
	
	private Map<String, Session> sessions = new HashMap<String, Session>();
	private Map<Integer, Connection> connectionsByRequestId = new HashMap<Integer, Connection>();
	private IRequestHandler requestHandler = null;
	private ICommandHandler commandHandler = null;;
	
	public MessageCenter() {
		super();
	}

	public void receiveMessage(Connection connection, Message message) {
		Session session = getSessionForMessage(message);
		for (ISendable part : message.sendables) {
			if (part == null) {
				continue;
			}
			if (part instanceof Request && requestHandler != null ) {
				Request r = (Request) part;
				connectionsByRequestId.put(r.id, connection);
				requestHandler.handleRequest(session, r);
			} else if (part instanceof Command && commandHandler != null) {
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

	public void setRequestHandler(IRequestHandler requestHandler) {
		this.requestHandler = requestHandler;
	}

	public void setCommandHandler(ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	
}
