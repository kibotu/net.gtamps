package net.gtamps.shared.configuration;

final class ConfigKey {

    public static final String DELIMITER = ".";

    private final static String normalizeKey(final String key) {
        return key.toUpperCase();
    }

    private static final String[] splitKey(final String key) {
        int index = key.indexOf(DELIMITER);
        if (index < 0) {
            index = key.length();
        }
        final String[] pair = {"", ""};
        pair[0] = key.substring(0, index);
        if (index < key.length()) {
            pair[1] = key.substring(index + 1);
        }
        return pair;
    }

    final String head;
    final String tail;

    public ConfigKey(final String key) {
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be 'null'");
        }
        final String[] pair = splitKey(normalizeKey(key));
        this.head = pair[0];
        this.tail = "".equals(pair[1]) ? null : pair[1];
    }

    public boolean isIntermediate() {
        return this.tail != null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.head == null) ? 0 : this.head.hashCode());
        result = prime * result + ((this.tail == null) ? 0 : this.tail.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigKey other = (ConfigKey) obj;
        if (this.head == null) {
            if (other.head != null) {
                return false;
            }
        } else if (!this.head.equals(other.head)) {
            return false;
        }
        if (this.tail == null) {
            if (other.tail != null) {
                return false;
            }
        } else if (!this.tail.equals(other.tail)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.head + ((this.tail != null) ? DELIMITER + this.tail : "");
    }


}
