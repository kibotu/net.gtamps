package net.gtamps.shared.game;

import org.jetbrains.annotations.NotNull;


/**
 * A generic, serializable  property of a game object, which has a name and
 * holds a single value.
 *
 * @param <T> it is highly recommended that this type properly
 *            implement {@link Object#toString() toString()},
 *            {@link Object#hashCode() hashCode()} and {@link Object#equals() equals()}
 * @author jan, tom, til
 */
public class Propertay<T> extends GameObject implements IProperty<T> {

	private static final long serialVersionUID = 6612996255584968605L;
	public static transient final String[] generics = {"value"};

	private final GameObject parent;
	private T value;

	public Propertay(@NotNull final GameObject parent, @NotNull final String name, @NotNull final T value) {
		super(name);
		this.parent = parent;
		this.value = value;
		this.parent.hasChanged = true;
	}

	@Override
	public T value() {
		return this.value;
	}

	@Override
	public String getAsString() {
		return this.value.toString();
	}

	@Override
	public boolean set(final T value) {
		if (value == null) {
			throw new IllegalArgumentException("'value' must not be null");
		}
		if (this.value.equals(value)) {
			return false;
		}
		this.value = value;
		this.hasChanged = true;
		this.parent.hasChanged = true;
		return true;
	}

	@Override
	public String toString() {
		final String s = String.format("%s: %s", this.name, this.value.toString());
		return s;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Propertay<?> other = (Propertay<?>) obj;
		if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + value.hashCode();
		return result;
	}

}
