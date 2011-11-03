package net.gtamps.shared.game;

import org.jetbrains.annotations.NotNull;

public class ProxyProperty<T> extends GameObject implements IProperty<T> {
	
	private static final long serialVersionUID = -12578145798197928L;

	@NotNull
	private final GameObject parent;
	@NotNull
	private final T defaultValue;
	private IProperty<T> actual = null;
	
	public ProxyProperty(GameObject parent, String name, @NotNull T defaultValue) {
		super(name);
		this.parent = parent;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public T value() {
		return getActual().value();
	}
	
	@Override
	public String getAsString() {
		return getActual().getAsString();
	}
	
	@Override
	public boolean set(T value) {
		if (value == null) {
			throw new IllegalArgumentException("'value' must not be null");
		}
		return getActual().set(value);
	}
	
	@Override
	public String toString() {
		if (actual == null) {
			return String.format("%s (proxy): not instantiated");
		}
		return actual.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return getActual().equals(obj);
	}
	
	@Override 
	public int hashCode() {
		return getActual().hashCode();
	}
	
	private IProperty<T> getActual() {
		if (actual == null) {
			actual = parent.useProperty(name, defaultValue);
		}
		return actual;
	}

}
