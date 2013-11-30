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
	private static final String typeNotAllowedMsg = "illegal value type; see Value.ALLOWED_TYPES for legal types. -> ";

	protected T value;

	public Value() {
		super();
	}

	public Value(final T value) {
		this();
		set(value);
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

