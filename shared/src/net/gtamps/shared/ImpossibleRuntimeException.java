package net.gtamps.shared;

/**
 * A runtime exception for when something goes wrong that cannot go wrong:
 * execution has taken an unexpected path and reached territory hitherto deemed
 * unreachable.
 * <p/>
 * Guess somebody overlooked something, eh?
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public class ImpossibleRuntimeException extends BaseRuntimeException {

	private static final long serialVersionUID = -4201382437800211610L;

	private static final String impossibleMsg = 
			"the following condition was assumed to be prevented from "
					+ "happening at this point, but still, here it is: ";

	/**
	 * @see ImpossibleRuntimeException
	 */
	public ImpossibleRuntimeException() {
	}

	/**
	 * @see ImpossibleRuntimeException
	 */
	public ImpossibleRuntimeException(final String message) {
		super(impossibleMsg + message);
	}

	/**
	 * @see ImpossibleRuntimeException
	 */
	public ImpossibleRuntimeException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @see ImpossibleRuntimeException
	 */
	public ImpossibleRuntimeException(final String message, final Throwable cause) {
		super(impossibleMsg + message, cause);
	}


}
