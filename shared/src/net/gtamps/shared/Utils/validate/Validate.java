package net.gtamps.shared.Utils.validate;


public class Validate {

	public static boolean notNull(final Object o) throws IllegalArgumentException {
		return notNull(o, null);
	}

	public static boolean notNull(final Object o, final String message) throws IllegalArgumentException {
		if (o == null) {
			throw new IllegalArgumentException(message != null ? message : "parameter must not be null");
		}
		return true;
	}

	public static boolean notEmpty(final String s, final String message) throws IllegalArgumentException {
		notNull(s, (message != null ? message : "string must not be null"));
		if ("".equals(s)) {
			throw new IllegalArgumentException(message != null ? message : "string must not be empty");
		}
		return true;
	}

	public static boolean notEmpty(final String s) throws IllegalArgumentException {
		return notEmpty(s, null);
	}

	public static boolean isTrue(final boolean b) {
		return isTrue(b, null);
	}

	public static boolean isTrue(final boolean b, final String message) {
		if (!b) {
			throw new IllegalArgumentException(message != null ? message : "true boolean expected");
		}
		return true;
	}

	public static <T> boolean noNullElements(final T[] elements) {
		return noNullElements(elements, "array must not contain null");
	}

	public static <T> boolean noNullElements(final T[] elements, final String message) {
		notNull(elements, message);
		for (final T e: elements) {
			notNull(e, message);
		}
		return true;
	}

}
