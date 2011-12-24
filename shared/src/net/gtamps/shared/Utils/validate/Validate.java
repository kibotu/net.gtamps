package net.gtamps.shared.Utils.validate;

public class Validate {

    public static void notNull(final Object o) throws IllegalArgumentException {
        notNull(o, null);
    }

    public static void notNull(final Object o, final String message) throws IllegalArgumentException {
        if (o == null) {
            throw new IllegalArgumentException(message != null ? message : "parameter must not be null");
        }
    }

    public static void notEmpty(final String s, final String message) throws IllegalArgumentException {
        notNull(s, (message != null ? message : "string must not be null"));
        if ("".equals(s)) {
            throw new IllegalArgumentException(message != null ? message : "string must not be empty");
        }
    }

    public static void notEmpty(final String s) throws IllegalArgumentException {
        notEmpty(s, null);
    }

    public static void isTrue(final boolean b) {
        isTrue(b, null);
    }

    public static void isTrue(final boolean b, final String message) {
        if (!b) {
            throw new IllegalArgumentException(message != null ? message : "true boolean expected");
        }
    }

}
