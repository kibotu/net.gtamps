package net.gtamps.shared.game;

import java.io.Serializable;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;


/**
 * A generic, serializable  property of a game object, which has a name and 
 * holds a single value.
 *
 * @author jan, tom, til
 * 
 * @param <T>	it is highly recommended that this type properly 
 * 				implement {@link Object#toString() toString()}, 
 * 				{@link Object#hashCode() hashCode()} and {@link Object#equals() equals()}
 *
 */
public class Propertay<T> extends AbstractPropertay<T> {
	
	private static final long serialVersionUID = 6612996255584968605L;

	private T value;

	public Propertay(@NotNull GameObject parent, @NotNull String name, @NotNull T value) {
		super(parent, name.toLowerCase());
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
	public boolean set(T value) {
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
	public <T> Propertay<T> useProperty(@NotNull String name, @NotNull T value) throws NoSuchElementException {
		throw new UnsupportedOperationException("this type of gameObject does not support properties");
	}
	
	@Override
	public String toString() {
		String s = String.format("%s: %s", this.name, this.value.toString());
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Propertay<?> other = (Propertay<?>) obj;
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
