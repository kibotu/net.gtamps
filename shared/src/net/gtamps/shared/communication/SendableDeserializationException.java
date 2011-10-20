package net.gtamps.shared.communication;

public class SendableDeserializationException extends RuntimeException {
	
	private static final long serialVersionUID = -5572331543260173889L;

	public SendableDeserializationException(String message) {
		this(message, null);
	}

	public SendableDeserializationException(Throwable cause) {
		this(null, cause);
	}

	public SendableDeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
