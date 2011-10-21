package net.gtamps.shared.game;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;


/**
 * A new, better implementation of the Property concept, the way Tom
 * imagined it in the first place. :)
 * 
 * A property has a name and holds a single value.
 *
 * @author jan, tom, til
 *
 */
public class Propertay<T> extends GameObject implements Serializable {
	
	private transient final GameObject parent;
	private T value;

	public Propertay(@NotNull GameObject parent, @NotNull String name, @NotNull T value) {
		super(name.toLowerCase());
		this.parent = parent;
		this.value = value;
		this.parent.hasChanged = true;
	}
	
	public GameObject getParent() {
		return this.parent;
	}
	
	public T value() {
		return this.value;
	}
	
	public String getAsString() {
		return this.value.toString();
	}
	
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
	public String toString() {
		String s = String.format("%s: %s", this.name, this.value);
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
