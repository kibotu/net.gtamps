package net.gtamps.server;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.communication.Message;


public final class SessionManager  {
	
	public static final SessionManager instance = new SessionManager();
	
	private Map<String, Session> sessions = new HashMap<String, Session>();
	
	private SessionManager() {
	}
	
	public void handleIncomingMessage(Connection c, Message m) {
		Session s = getSessionForMessage(m);
		if (!s.getConnection().equals(c)) {
			s.setConnection(c);
		}
	}
	
	public Session getSessionForMessage(Message msg) {
		String id = msg.getSessionId();
		Session s = null;
		if (id == null || id == "" || !sessions.containsKey(id)) {
			s = createSession();
		} else {
			s = sessions.get(id); 
		}
		return s;
	}
	
	public Session createSession() {
		Session s = new Session();
		sessions.put(s.getId(), s);
		return s;
	}
	
}
