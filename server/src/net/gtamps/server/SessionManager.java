package net.gtamps.server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.gtamps.shared.communication.Message;

public final class SessionManager implements IMessageHandler {
	
	public static final SessionManager instance = new SessionManager();
	
	private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	private SessionManager() {
	}
	
	@Override
	public void receiveMessage(Connection<?>c, Message m) {
		Session s = getSessionForMessage(m);
		s.setConnection(c);
	}
	
	public Session getSessionForMessage(Message msg) {
		// this assumes the id fields to be immutable for concurrency
		final String id = getSessionIdForMessage(msg);
		//FIXME putIfAbsent returns 'null' if put
		final Session newSession = new Session(id);
		final Session existing = sessions.putIfAbsent(id, newSession);
		return (existing == null) ? newSession : existing;
	}
	
	public Session getSessionById(String id) {
		return sessions.get(id);
	}
	
	private String getSessionIdForMessage(Message msg) {
		final String presumptiveId = msg.getSessionId();
		return (isValidId(presumptiveId) ? presumptiveId : generateSessionId());
	}
	
	private boolean isValidId(String id) {
		return (id != null && id.length() > 0);
	}
	
	private String generateSessionId() {
		return Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
	}
}
