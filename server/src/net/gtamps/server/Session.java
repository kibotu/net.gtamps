package net.gtamps.server;

public class Session {
	
	
	private final String id;
	private Connection connection;
	private boolean isAuthenticated = false;
	private boolean isPlaying = false;
	
	
	public Session() {
		this.id = generateId();
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
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private String generateId() {
		//TODO
		return "something";
	}

}
