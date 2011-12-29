package net.gtamps.shared.serializer.communication;

public class SendableSerializationException extends RuntimeException {

    private static final long serialVersionUID = -5572331543260173889L;

    public SendableSerializationException(String message) {
        this(message, null);
    }

    public SendableSerializationException(Throwable cause) {
        this(null, cause);
    }

    public SendableSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
