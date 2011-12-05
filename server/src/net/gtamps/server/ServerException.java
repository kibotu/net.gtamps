package net.gtamps.server;

public class ServerException extends Exception {

	private static final long serialVersionUID = 247848107406315606L;

	public ServerException(final String message) {
		super(message);
	}

	public ServerException(final Throwable cause) {
		super(cause);
	}

	public ServerException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
