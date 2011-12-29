package net.gtamps.shared;

public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1454822345768636674L;

	public BaseRuntimeException() {
	}

	public BaseRuntimeException(final String message) {
		super(message);
	}

	public BaseRuntimeException(final Throwable cause) {
		super(cause);
	}

	public BaseRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BaseRuntimeException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
