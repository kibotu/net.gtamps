package net.gtamps.server;

public class Session {
	
	private final String id;
	private Connection connection;
	
	public Session() {
		this.id = generateId();
		assert id != null;
	}

	public String getId() {
		return this.id;
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
