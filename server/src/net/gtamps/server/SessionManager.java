package net.gtamps.server;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.gtamps.game.IGame;
import net.gtamps.shared.serializer.communication.Message;

public final class SessionManager {

    public static final SessionManager instance = new SessionManager(0);

    private final Object lock = new Object();
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
    private final int maxSessions;

    private SessionManager(final int maxSessions) {
    	this.maxSessions = Math.max(0, maxSessions);
    }

    /**
     * Ensure a proper {@link Session} exists for the <tt>message</tt> and
     * return the corresponding {@link Session#getId() session id}.
     *
     * @param message
     * @param connection
     * 
     * @return the id of the session associated with the message
     * 
     * @throws ServerException	if the session limit has been reached,
     * 							and no new session can be created
     */
    public String getSessionForMessage(final Message message, final Connection<?> connection) throws ServerException {
        if (connection == null) {
            throw new IllegalArgumentException("'connection' must not be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("'msg' must not be null");
        }
        final String id = getSessionId(message.getSessionId());
        final Session newSession = new Session(id, connection);
        final Session existing = sessions.putIfAbsent(id, newSession);
        if (existing != null) {
            connectSession(existing, connection);
            return existing.getId();
        } else {
        	synchronized (lock) {
        		if (maxSessionsReached()) {
        			sessions.remove(id);
        			throw new ServerException("Server session limit reached. Please try again later.");
        		}
        	}
            return newSession.getId();
        }
    }

    public boolean isConnected(final String sessionId) throws NoSuchElementException {
        return retrieveSession(sessionId).isConnected();
    }

    public boolean isAuthenticated(final String sessionId) throws NoSuchElementException {
        return retrieveSession(sessionId).isAuthenticated();
    }

    public boolean isPlaying(final String sessionId) throws NoSuchElementException {
        return retrieveSession(sessionId).isPlaying();
    }

    public void sendMessage(final Message msg) throws NoSuchElementException {
        final Session session = retrieveSession(msg.getSessionId());
        session.getConnection().send(msg);
    }

    public User getUserForSession(final String sessionId) {
        return retrieveSession(sessionId).getUser();
    }

    public IGame getGameForSession(final String sessionId) {
        return retrieveSession(sessionId).getGame();
    }


    /**
     * @param id
     * @param user
     * @throws IllegalStateException if authentication is not possible because
     *                               <tt>user</tt> does not match the user
     *                               already associated with the session
     */
    public void authenticateSession(final String id, final User user) throws IllegalStateException {
        Session existing = retrieveSession(id);
        while (!sessions.replace(id, existing, existing.authenticate(user))) {
            existing = sessions.get(id);
        }
    }

    public void joinSession(final String id, final IGame game) {
        Session existing = retrieveSession(id);
        while (!sessions.replace(id, existing, existing.join(game))) {
            existing = sessions.get(id);
        }
    }

    public void leaveSession(final String id) {
        Session existing = retrieveSession(id);
        while (!sessions.replace(id, existing, existing.leave())) {
            existing = sessions.get(id);
        }
    }

    private void disconnectSession(final String id, final Connection<?> deadConnection) {
        Session existing = retrieveSession(id);
        while (deadConnection.equals(existing.getConnection())
                && !sessions.replace(id, existing, existing.disconnect())) {
            existing = sessions.get(id);
        }
    }

    private Session retrieveSession(final String id) {
        final Session s = sessions.get(id);
        if (s == null) {
            throw new NoSuchElementException("session not found: " + id);
        }
        return s;
    }

    private void connectSession(Session existing, final Connection<?> newConnection) {
        final Connection<?> existingConnection = existing.getConnection();
        if (existingConnection != null && !existingConnection.equals(newConnection)) {
            while (!sessions.replace(existing.getId(), existing, existing.reconnect(newConnection))) {
                existing = sessions.get(existing);
            }
        }
    }

    private String getSessionId(final String presumptiveId) {
        if (isValidId(presumptiveId)) {
            return presumptiveId;
        }
        return generateSessionId();
    }

    private boolean isValidId(final String id) {
        return (id != null && sessions.containsKey(id));
    }

    private String generateSessionId() {
        final UUID uid = UUID.randomUUID();
        final long hi = uid.getMostSignificantBits() & 0xFF00000000000000l;
        final long lo = uid.getLeastSignificantBits() & 0x00FFFFFFFFFFFFFFl;
        final String id = Long.toHexString(hi | lo);
        return id;
    }
    
    // TODO synchronize properly
    private boolean maxSessionsReached() {
    	if (maxSessions < 1) {
    		return false;
    	} else {
			return sessions.size() > maxSessions;
		}
    }
}
