package net.gtamps.shared.serializer.communication.data;

public final class Value<T> extends AbstractSendableData<Value<T>> {

	public static final Class<?>[] ALLOWED_TYPES = {
		String.class,
		Boolean.class,
		Byte.class, Integer.class, Long.class, 
		Float.class, Double.class,
		//		Entity.class, GameEvent.class, Player.class,
	};

	private static final long serialVersionUID = -9073044759676467812L;
	private static final String typeNotAllowedMsg = "illegal value type; see SendableDataValue.ALLOWED_TYPES for legal types. -> ";

	protected T value;

	/**
	 * @param type
	 * @param value
	 * @throws IllegalArgumentException	if T (= value.getClass()is not
	 */
	public Value() {
		//		validate(valueType);
	}

	private final void validate(final Class<T> valueType) {
		boolean found = false;
		for (int i = 0; i < ALLOWED_TYPES.length; i++) {	// old-school for-loop allocates no heap memory
			if (valueType == ALLOWED_TYPES[i]) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException(typeNotAllowedMsg + valueType.getCanonicalName());
		}
	}

	public void set(final T value) {
		this.value = value;
	}

	public T get() {
		return value;
	}

	@Override
	public String toString() {
		return "value=" + (value == null ? "null" : value.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		final Value<?> other = (Value<?>) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	protected void initHook() {
		value = null;
	}

	@Override
	protected void recycleHook() {
		value = null;
	}
}

