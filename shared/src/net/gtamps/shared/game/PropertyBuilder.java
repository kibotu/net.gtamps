package net.gtamps.shared.game;

import net.gtamps.shared.Utils.validate.Validate;

public class PropertyBuilder<T> {

	Class<T> type;
	String name;
	boolean lazy = false;
	T value = null;
	T defaultValue;
	private GameObject parent;

	public PropertyBuilder(final Class<T> type) {
		Validate.notNull(type);
		this.type = type;
	}

	public PropertyBuilder<T> name(final String name) {
		Validate.notEmpty(name);
		this.name = name;
		return this;
	}

	public PropertyBuilder<T> lazy(final boolean isLazy) {
		this.lazy = isLazy;
		return this;
	}

	public PropertyBuilder<T> value(final T value) {
		this.value = value;
		return this;
	}

	public PropertyBuilder<T> defaultValue(final T defaultValue) {
		Validate.notNull(defaultValue);
		this.defaultValue = defaultValue;
		return this;
	}

	public PropertyBuilder<T> parent(final GameObject parent) {
		Validate.notNull(parent);
		this.parent = parent;
		return this;
	}

	public Propertay<T> build() {
		validate();
		final T setValue = value != null ? value : defaultValue;
		return new Propertay<T>(parent, name, setValue);
	}

	private void validate() {
		Validate.notNull(type);
		Validate.notEmpty(name);
		Validate.notNull(parent);
		Validate.isTrue(value != null || defaultValue != null);
	}

}
