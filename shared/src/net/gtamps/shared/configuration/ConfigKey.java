package net.gtamps.shared.configuration;

final class ConfigKey {
	
	public static final String DELIMITER = ".";
	
	private final static String normalizeKey(String key) {
		return key.toUpperCase();
	}
	
	private static final String[] splitKey(String key) {
		int index = key.indexOf(DELIMITER);
		String[] pair = new String[2];
		pair[0] = key.substring(0, index);			
		pair[1] = key.substring(index+1);			
		return pair;
	}
	
	final String head;
	final String tail;
	
	public ConfigKey(String key) {
		String[] pair = splitKey(normalizeKey(key == null ? "" : key));
		head = pair[0];
		tail = "".equals(pair[1]) ? null : pair[1];
	}
	
	public boolean isIntermediate() {
		return tail != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + ((tail == null) ? 0 : tail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConfigKey other = (ConfigKey) obj;
		if (head == null) {
			if (other.head != null) {
				return false;
			}
		} else if (!head.equals(other.head)) {
			return false;
		}
		if (tail == null) {
			if (other.tail != null) {
				return false;
			}
		} else if (!tail.equals(other.tail)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return head + ((tail != null) ? DELIMITER + tail : ""); 
	}
	
	
	
	

}
