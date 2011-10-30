package net.gtamps.server;

/**
 * immutable!
 */
public final class User {
	
	public final int uid;
	public final String name;

	public User(int id, String name) {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		if (!isValidId(id)) {
			throw new IllegalArgumentException("'id' is invalid");
		}
		this.uid = id;
		this.name = name;
	}
	
	private boolean isValidId(int id) {
		return id > 0;
	}
	
}
