package net.gtamps.server;

import net.gtamps.game.IGame;
import net.gtamps.shared.serializer.communication.SendableType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable!
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
public final class Session {
	
	@NotNull
	private final String id;
	@NotNull
	private final State state;
	@Nullable
	private transient final Connection<?> connection;
	@Nullable
	private transient final IGame game;
	@Nullable
	private transient final User user;
	
	Session(final String id) {
		this(id, State.DISCONNECTED, null, null, null);
	}
	
	Session(final String id, final Connection<?> connection) {
		this(id, State.CONNECTED, connection, null, null);
	}
	
	// COPY CONSTRUCTOR
	
	private Session(final String id, final State state, final Connection<?> c, final IGame g, final User u) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (id.length() == 0) {
			throw new IllegalArgumentException("'id' must not be empty");
		}
		if (state == null) {
			throw new IllegalArgumentException("'state' must not be null");
		}
		this.id = id;
		this.state = state;
		connection = c;
		game = g;
		user = u;
		validate();
	}
	
	// PUBLIC

	public String getId() {
		return id;
	}
	
	public boolean isConnected() {
		return state.ordinal() >= State.CONNECTED.ordinal();
	}
	
	public boolean isAuthenticated() {
		return state.ordinal() >= State.AUTHENTICATED.ordinal();
	}
	
	public boolean isPlaying() {
		return state.ordinal() >= State.PLAYING.ordinal();
	}
	
	public Connection<?> getConnection() {
		return connection;
	}
	
	
	public IGame getGame() {
		return game;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Session)) {
			return false;
		}
		final Session other = (Session) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Session [id=" + id + ", game=" + game + ", user=" + user + "]";
	}

	// PACKAGE PROTECTED

	State getState() {
		return state;
	}
	
	Session reconnect(final Connection<?> c) {
		if (isConnected() && connection.equals(c)) {
			return this;
		}
		return changeState(State.CONNECTED, c, game, user);
	}
	
	Session disconnect() {
		return changeState(State.DISCONNECTED, connection, game, user);
	}
	
	Session authenticate(final User u) {
		if (user != null && !user.equals(u)) {
			throw new IllegalStateException("session can only re-authenticate: user already set");
		}
		if (isAuthenticated()) {
			return this;
		}
		return changeState(State.AUTHENTICATED, connection, game, u);
	}
	
	Session reauthenticate() {
		if (isAuthenticated()) {
			return this;
		}
		return changeState(State.AUTHENTICATED, connection, game, user);
	}
	
	Session join(final IGame game) {
		return changeState(State.PLAYING, connection, game, user);
	}
	
	Session leave() {
		return changeState(State.AUTHENTICATED, connection, null, user);
	}
 	
	// PRIVATE
	
	private Session changeState(final State state, final Connection<?> c, final IGame g, final User u) {
		return new Session(id, state, c, g, u);
	}
	
	private void validate() throws IllegalStateException {
		switch (state) {
			case PLAYING:
				if (game == null) {
					throw new IllegalStateException("'game' must not be 'null' in " + state + " state");
				}
				//fallthrough:
			case AUTHENTICATED:
				if (user == null) {
						throw new IllegalStateException("'user' must not be 'null' in " + state + " state");
				}
				//fallthrough:
			case CONNECTED:
				if (connection == null) {
						throw new IllegalStateException("'connection' must not be 'null' in " + state + " state");
				}
				break;
			case DISCONNECTED:
				break;
			default:
				// shouldn't get here
				throw new IllegalAccessError("check and treat all possible states!");
		}
	}
	
	enum State {
		DISCONNECTED, CONNECTED, AUTHENTICATED, PLAYING;
		
		public State transition(final SendableType responseType) {
			switch (responseType) {
			// general transitions:
				case BAD_MESSAGE:
				case BAD_SENDABLE:
				case SESSION_OK:
				case SESSION_ERROR:
				case GETMAPDATA_ERROR:
				case GETPLAYER_ERROR:
				case GETUPDATE_ERROR:
				case JOIN_ERROR:
				case LEAVE_ERROR:
					return this;
				case QUIT_OK:
				case QUIT_NEED:
				case QUIT_BAD:
				case QUIT_ERROR:
					return DISCONNECTED;
			}
			switch (this) {
			// specific transitions:
				case CONNECTED:
					switch (responseType) {
						case LOGIN_OK:
						case REGISTER_OK:
							return AUTHENTICATED;
						case LOGIN_NEED:
						case LOGIN_BAD:
						case REGISTER_NEED:
						case REGISTER_BAD:
							return CONNECTED;
					}
				case AUTHENTICATED:
					switch (responseType) {
					case JOIN_OK:
						return PLAYING;
					case JOIN_NEED:
						return CONNECTED;
					case JOIN_BAD:
						return AUTHENTICATED;
				}
				case PLAYING:
					switch (responseType) {
						case LEAVE_OK:
						case LEAVE_NEED:
						case LEAVE_BAD:
						case SPAWN_NEED:
						case GETMAPDATA_NEED:
						case GETPLAYER_NEED:
						case GETUPDATE_NEED:
							return AUTHENTICATED;
						case SPAWN_OK:
						case GETMAPDATA_OK:
						case GETPLAYER_OK:
						case GETUPDATE_OK:
							return PLAYING;
					}
				case DISCONNECTED:
					return CONNECTED;
			}
			// default transition:
			return this;
		}
	}

}
