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
		String id = m.getSessionId();
		Session s = getSessionForMessage(m);
		
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
