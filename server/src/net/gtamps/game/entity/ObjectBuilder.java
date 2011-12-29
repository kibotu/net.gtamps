package net.gtamps.game.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.gtamps.shared.ImpossibleRuntimeException;
import net.gtamps.shared.Utils.validate.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ObjectBuilder<T> {

	private boolean locked = false;
	private Class<T> type = null;
	private String description = null;

	private final Map<String, Object> parameters = new HashMap<String, Object>(5);
	private final Map<String, List<?>> listparameters = new HashMap<String, List<?>>(5);
	private ObjectBuilder<? super T> superBuilder = null;

	private final Map<String, Field> fields = new HashMap<String, Field>(5);

	public ObjectBuilder() {
		// nix
	}

	private void validateDefaultConstructor(final Class<T> type) {
		final Constructor<T> c = getAccessibleDefaultConstructor(type);
		if (c == null) {
			throw new IllegalArgumentException(
					type.getCanonicalName()
					+ "has not default constructor or default constructor not accessible"
					);
		}
	}

	public boolean isLocked() {
		return locked;
	}

	public ObjectBuilder<T> lock() {
		locked = true;
		return this;
	}

	public ObjectBuilder<T> unlock() {
		locked = false;
		return this;
	}

	private void checkUnlocked() throws IllegalStateException {
		if (locked) {
			throw new IllegalStateException("builder object is locked. unlock() first.");
		}
	}

	private void checkTypeIsSet() throws IllegalStateException {
		if (type == null) {
			throw new IllegalStateException("no type set. call classobject(Class<T> type) first.");
		}
	}

	public String getDescription() {
		return description == null ? "" : description;
	}

	public final void classobject(final Class<T> type) {
		Validate.notNull(type);
		validateDefaultConstructor(type);
		checkUnlocked();
		this.type = type;
	}

	public final ObjectBuilder<T> extend(final ObjectBuilder<? super T> superBuilder) {
		checkUnlocked();
		this.superBuilder = superBuilder;
		return this;
	}

	public final ObjectBuilder<T> desciption(final String description) {
		checkUnlocked();
		this.description = description;
		return this;
	}

	public final ObjectBuilder<T> setParam(final String name, final Object value) {
		Validate.notEmpty(name);
		validateParam(name, value);
		checkUnlocked();
		checkTypeIsSet();
		parameters.put(name, value);
		return this;
	}

	public final ObjectBuilder<T> addListParam(final String listparamName, final Object element) {
		validateListParam(listparamName, element);
		checkUnlocked();
		checkTypeIsSet();
		recordListParam(listparamName, element);
		return this;
	}

	public T build() {
		validate();
		final T instance = instantiate();
		populate(instance);
		return instance;
	}

	private void validate() {
		checkTypeIsSet();
		validateDefaultConstructor(type);
	}

	private void populate(final T instance) {
		if (superBuilder != null) {
			superBuilder.populate(instance);
		}
		setParameters(instance);
		setListParameters(instance);
	}

	private void setParameters(final T instance) {
		for (final Entry<String, Object> param : parameters.entrySet()) {
			setField(instance, param.getKey(), param.getValue());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setListParameters(final T instance) {
		for (final Entry<String, List<?>> entry: listparameters.entrySet()) {
			final String name = entry.getKey();
			final List listparam = entry.getValue();
			Collection actual = (Collection) readField(instance, name);
			if (actual == null) {
				actual = initList(instance, entry.getKey(), listparam);
			}
			actual.addAll(listparam);
		}
	}

	@SuppressWarnings("rawtypes")
	private List initList(final T instance, final String key, final List<?> value) {
		final int initialCapacity = value.size();
		final List list = new ArrayList(initialCapacity);
		setField(instance, key, list);
		return list;
	}

	private T instantiate() throws ImpossibleRuntimeException {
		try {
			return getAccessibleDefaultConstructor(type).newInstance();
		} catch (final InstantiationException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalArgumentException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final InvocationTargetException e) {
			throw new ImpossibleRuntimeException(e);
		}
	}

	private void validateParam(final String name, final Object value) {
		validateHasAccessibleField(name);
		validateFieldCanContain(name, value);
	}

	private void validateListParam(final String listparamName, final Object element) {
		validateHasAccessibleField(listparamName);
		validateFieldCanAddElement(listparamName, element);
		Validate.notNull(element);
	}

	private void recordListParam(final String listparamName, final Object element) {
		List list = listparameters.get(listparamName);
		if (list == null) {
			list = new LinkedList();
			listparameters.put(listparamName, list);
		} else if (list.size() > 0) {
			final Class<?> expectedClass = list.get(0).getClass();
			final Class<?> newElementClass = element.getClass();
			if (newElementClass != expectedClass) {
				throw new IllegalArgumentException(
						newElementClass.getCanonicalName()
						+ ": class of new element is not expected class "
						+ expectedClass.getCanonicalName()
						);
			}
		}
		list.add(element);
	}

	private Constructor<T> getAccessibleDefaultConstructor(final Class<T> type) {
		Constructor<T> c = null;
		try {
			c = type.getDeclaredConstructor();
			c.setAccessible(true);
		} catch (final NoSuchMethodException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final SecurityException e) {
			throw new ImpossibleRuntimeException(e);
		}
		return c;
	}

	private Field getAccessibleField(final String name) throws ImpossibleRuntimeException {
		Field field = fields.get(name);
		if (!fields.containsKey(name)) {
			try {
				field = type.getField(name);
				field.setAccessible(true);
				fields.put(name, field);
			} catch (final NoSuchFieldException e) {
				throw new ImpossibleRuntimeException(e);
			} catch (final SecurityException e) {
				throw new ImpossibleRuntimeException(e);
			}
		}
		return field;
	}

	private Object readField(final T instance, final String name) {
		final Field field = getAccessibleField(name);
		try {
			return field.get(instance);
		} catch (final IllegalArgumentException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new ImpossibleRuntimeException(e);
		}
	}

	private void setField(final T instance, final String name, final Object value) throws ImpossibleRuntimeException {
		final Field field = getAccessibleField(name);
		try {
			field.set(instance, value);
		} catch (final IllegalArgumentException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new ImpossibleRuntimeException(e);
		}
	}

	private void validateHasAccessibleField(final String name) {
		if (getAccessibleField(name) == null) {
			throw new IllegalArgumentException("field not present or not writable: " + name);
		}
	}

	private void validateFieldCanContain(final String name, final Object value) {
		final Field field = getAccessibleField(name);
		if (value == null) {
			validateFieldMayContainNull(field);
		} else {
			validateFieldCanContainObject(field, value);
		}
	}

	private void validateFieldMayContainNull(final Field field) {
		if (field.getAnnotation(NotNull.class) != null
				|| field.getAnnotation(Nullable.class) == null) {
			throw new IllegalArgumentException(
					"trying to assign null to field, but is nullable: "
							+ field.getName()
					);
		}
	}

	private void validateFieldCanContainObject(final Field field, final Object value) {
		final Class<?> fieldType = field.getType();
		final Class<?> valueType = value.getClass();
		if (!fieldType.isAssignableFrom(valueType)) {
			throw new IllegalArgumentException(
					fieldType.getCanonicalName()
					+ " field can not contain value type "
					+ valueType.getCanonicalName()
					);
		}
	}

	private void validateFieldCanAddElement(final String name, final Object value) {
		final Field field = getAccessibleField(name);
		validateFieldIsCollection(field);
		validateGenericsAppropriateForValue(field, value);
	}

	private void validateFieldIsCollection(final Field field) {
		final Class<?> fieldType = field.getType();
		if (!java.util.Collection.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException("field does not contain a collection: " + field.getName());
		}
	}

	private void validateGenericsAppropriateForValue(final Field field, final Object value) {
		// FIXME check generics!
	}

}
