package net.gtamps.shared;

public class BaseException extends Exception {

	private static final long serialVersionUID = 4453888320675452315L;

	public BaseException() {
	}

	public BaseException(final String message) {
		super(message);
	}

	public BaseException(final Throwable cause) {
		super(cause);
	}

	public BaseException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BaseException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
