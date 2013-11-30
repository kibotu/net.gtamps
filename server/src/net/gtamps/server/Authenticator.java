package net.gtamps.server;

import java.util.regex.Pattern;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.server.db.DBHandler;

public final class Authenticator {
	
	private final static Pattern usernameMatcher = Pattern.compile("^(\\w)+$");
	private final static Pattern passwordMatcher = Pattern.compile("^(\\w)+$");
	
	public static Authenticator instance = new Authenticator(GTAMultiplayerServer.getDBHandler());

	private final DBHandler dbHandler;

	private Authenticator(final DBHandler dbHandler) {
		if (dbHandler == null) {
			throw new IllegalArgumentException("'dbHandler' must not be 'null'");
		}
		this.dbHandler = dbHandler;
	}
	
	public synchronized User Login(final String username, final String password) throws ServerException {
		validateUsername(username);
		validatePassword(password);
		final User user = login(username, password);
		if (user == null) {
			throw new ServerException("wrong username or password");
		}
		return user;
	}
	
	public synchronized User Register(final String username, final String password) throws ServerException {
		validateUsername(username);
		validatePassword(password);
		final User user = register(username, password);
		if (user == null) {
			throw new ServerException("can't register: choose another username");
		}
		return user;
	}
	
	private void validateUsername(final String username) throws ServerException {
		if (!usernameMatcher.matcher(username).matches()) {
			throw new ServerException("malformed username");
		}
	}
	
	private void validatePassword(final String password) throws ServerException {
		if (!passwordMatcher.matcher(password).matches()) {
			throw new ServerException("malformed password");
		}
	}
	
	private User login(final String username, final String password) {
		User user = null;
		if (userExists(username)) {
			final int uid = dbHandler.authPlayer(username, password);
			if (uid >= 0) {
				user = new User(uid, username);
			}
		}
		return user;
	}
	
	private User register(final String username, final String password) {
		if (userExists(username)) {
			return null;
		}
		User user = null;
		final int uid = dbHandler.createPlayer(username, password);
		if (uid >= 0) {
			user = new User(uid, username);
		}
		return user;
	}
	
	private boolean userExists(final String username) {
		return dbHandler.hasPlayer(username);
	}
	
}
