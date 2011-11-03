package net.gtamps.shared.serializer.communication;

public class MessageDeserializationException extends RuntimeException {

    private static final long serialVersionUID = -5572331543260173889L;

    public MessageDeserializationException(String message) {
        this(message, null);
    }

    public MessageDeserializationException(Throwable cause) {
        this(null, cause);
    }

    public MessageDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
