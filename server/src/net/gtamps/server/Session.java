package net.gtamps.server;

import java.util.concurrent.atomic.AtomicReference;

import net.gtamps.game.IGame;

import org.jetbrains.annotations.NotNull;

public class Session {
	
	@NotNull
	private final String id;
	private final AtomicReference<Connection<?>> connection;
	private volatile IGame game = null;
	private volatile User user = null;
	
	
	
	/**
	 * @param id	not null; not the empty string
	 */
	Session(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (id.length() == 0) {
			throw new IllegalArgumentException("'id' must not be empty");
		}
		this.id = id;
		assert id != null;
		connection = new AtomicReference<Connection<?>>();
	}

	public String getId() {
		return id;
	}
	
	public boolean isAuthenticated() {
		return user != null;
	}
	
	public boolean isPlaying() {
		return game != null;
	}
	
	public Connection<?> getConnection() {
		return connection.get();
	}
	
	
	public IGame getGame() {
		return game;
	}
	
	public void setGame(final IGame game) {
		this.game = game;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(final User user) {
		this.user = user;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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

	void setConnection(final Connection<?> connection) {
		if (connection == null) {
			throw new IllegalArgumentException("'connection' must not be null");
		}
		this.connection.set(connection);
	}
	

}
