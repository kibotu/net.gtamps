package net.gtamps.server;

import net.gtamps.game.IGame;

public class Session {
	
	
	private final String id;
	private volatile Connection<?> connection;
	private volatile boolean isAuthenticated = false;
	private volatile boolean isPlaying = false;
	private volatile IGame game = null;
	
	
	/**
	 * @param id	not null; not the empty string
	 */
	Session(String id) {
		if (id == null) {
			throw new IllegalArgumentException("'id' must not be null");
		}
		if (id.length() == 0) {
			throw new IllegalArgumentException("'id' must not be empty");
		}
		this.id = id;
		assert id != null;
	}

	public String getId() {
		return this.id;
	}
	
	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}
	
	public boolean isPlaying() {
		return this.isPlaying;
	}
	
	public Connection<?> getConnection() {
		return this.connection;
	}
	
	
	public IGame getGame() {
		return this.game;
	}
	
	public void setGame(IGame game) {
		this.game = game;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Session)) {
			return false;
		}
		Session other = (Session) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	void setConnection(Connection<?> connection) {
		this.connection = connection;
	}
	

}
