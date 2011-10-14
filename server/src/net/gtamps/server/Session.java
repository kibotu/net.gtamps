package net.gtamps.server;

public class Session {
	
	private final String id;
	private Connection connection;
	private boolean isAuthenticated;
	
	public Session() {
		this.id = generateId();
		assert id != null;
		this.isAuthenticated = false;
	}

	public String getId() {
		return this.id;
	}
	
	public boolean isAuthenticated() {
		return isAuthenticated;
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
