package net.gtamps.server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import net.gtamps.shared.communication.Message;

public final class SessionManager implements IMessageHandler {
	
	public static final SessionManager instance = new SessionManager();
	public static final Pattern ID_PATTERN = Pattern.compile("[0-9A-Fa-f]{16}");
	
	private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	private SessionManager() {
	}
	
	@Override
	public void receiveMessage(final Connection<?>c, final Message m) {
		final Session s = getSessionForMessage(m);
		s.setConnection(c);
	}
	
	public Session getSessionForMessage(final Message msg) {
		// this assumes the id fields to be immutable for concurrency
		final String id = getSessionIdForMessage(msg);
		final Session newSession = new Session(id);
		final Session existing = sessions.putIfAbsent(id, newSession);
		return (existing == null) ? newSession : existing;
	}
	
	public Session getSessionById(final String id) {
		return sessions.get(id);
	}
	
	private String getSessionIdForMessage(final Message msg) {
		final String presumptiveId = msg.getSessionId();
		if (isValidId(presumptiveId)) {
			return presumptiveId;
		}
		final String newId = generateSessionId();
		msg.setSessionId(newId);
		return newId;
	}
	
	private boolean isValidId(final String id) {
		return (id != null && ID_PATTERN.matcher(id).matches());
	}
	
	private String generateSessionId() {
		final String id = Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
		assert isValidId(id);
		return id;
	}
}
