package net.gtamps.server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import net.gtamps.shared.communication.Message;

public final class SessionManager implements IMessageHandler {
	
	public static final SessionManager instance = new SessionManager();
	public static final Pattern ID_PATTERN = Pattern.compile("[0-9A-Fa-f]{16}");
	
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
		if (isValidId(presumptiveId)) {
			return presumptiveId;
		} 
		String newId = generateSessionId();
		msg.setSessionId(newId);
		return newId;
	}
	
	private boolean isValidId(String id) {
		return (id != null && id.length() > 0 && ID_PATTERN.matcher(id).matches());
	}
	
	private String generateSessionId() {
		String id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		assert isValidId(id);
		return id;
	}
}
