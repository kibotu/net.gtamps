package net.gtamps.game.instantiation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import net.gtamps.shared.ImpossibleRuntimeException;
import net.gtamps.shared.Utils.validate.Validate;

/**
 * Instantiates objects and initializes their fields by reflection.
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 * @param <T>	the class of object that will be built
 */
public class ReflectiveObjectBuilder<T> {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Collection<?>> DEFAULT_COLLECTION_CLASS = (Class<? extends Collection<?>>) ArrayList.class;

	private final Class<T> type;
	private boolean locked = false;
	private Object lockingKey = null;
	private String description = null;

	/** names of fields that will be set and their values */
	private final Map<String, Object> parameters = new HashMap<String, Object>(5);
	/** names of collection fields and lists of values they will be initialized to contain */
	private final Map<String, List<?>> listparameters = new HashMap<String, List<?>>(5);
	/** a similar builder whose parameters this builder may override */
	private ReflectiveObjectBuilder<? super T> superBuilder = null;

	/** a cache for fields already handled */
	private final Map<String, Field> fields = new HashMap<String, Field>(5);

	/**
	 * creates a new builder for objects of the provided class
	 * 
	 * @param classobject	the class the builder will create objects of;
	 * 						must have a default constructor; not {@code null}
	 * 
	 * @throws IllegalArgumentException	if <tt>classobject</tt> is {@code null}
	 * 									or does not have a default constructor
	 */
	public ReflectiveObjectBuilder(final Class<T> classobject) throws IllegalArgumentException {
		Validate.notNull(classobject);
		validateDefaultConstructor(classobject);
		this.type = classobject;
	}

	private void validateDefaultConstructor(final Class<T> type) throws IllegalArgumentException {
		final Constructor<T> c = getAccessibleDefaultConstructor(type);
		if (c == null) {
			throw new IllegalArgumentException(
					type.getCanonicalName()
					+ "has no default constructor or default constructor not accessible"
					);
		}
	}

	/**
	 * return the class this builder will create objects of
	 * 
	 * @return  the class this builder will create objects of
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * A builder may be locked to prevent changing its parameters. Returns
	 * {@code true} if this is the case.
	 * 
	 * @return	{@code true} if the parameters of this builder cannot be changed
	 * 
	 * @see #lock()
	 * @see #unlock()
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Try to lock this builder with the default {@link #lock(Object) key} to prevent
	 * the changing of its parameters.
	 * 
	 * @return	this builder
	 * @throws IllegalArgumentException	if this builder has already been locked with
	 * 									a different key
	 * 
	 * @see #unlock()
	 * @see #lock(Object)
	 */
	public ReflectiveObjectBuilder<T> lock() throws IllegalArgumentException {
		return lock(null);
	}

	/**
	 * Try to unlock this builder with the default {@link #unlock(Object) key} to allow
	 * the changing of its parameters.
	 * 
	 * @return	this builder
	 * @throws IllegalArgumentException	if this builder is locked with a different key
	 * 
	 * @see #lock()
	 * @see #unlock(Object)
	 */
	public ReflectiveObjectBuilder<T> unlock() throws IllegalArgumentException {
		return unlock(null);
	}

	/**
	 * Try to lock this builder with the given <tt>key</tt> to prevent
	 * the changing of its parameters.
	 * 
	 * @param key	the key that has to be provided to unlock this builder again
	 * 
	 * @return	this builder
	 * @throws IllegalArgumentException	if this builder has already been locked with
	 * 									a different key
	 * @see #lock()
	 * @see #unlock(Object)
	 */
	public ReflectiveObjectBuilder<T> lock(final Object key) throws IllegalArgumentException {
		if (locked && key != lockingKey) {
			throw new IllegalArgumentException("can't lock: already locked with a different key");
		}
		locked = true;
		lockingKey = key;
		return this;
	}

	/**
	 * Try to unlock this builder with the given <tt>key</tt> to allow
	 * the changing of its parameters.
	 * 
	 * @param key	the key that was provided to lock this builder
	 * 
	 * @return	this builder
	 * @throws IllegalArgumentException	if this builder has been locked with
	 * 									a different key
	 * 
	 * @see #lock(Object)
	 * @see #unlock()
	 */
	public ReflectiveObjectBuilder<T> unlock(final Object key) throws IllegalArgumentException {
		if (locked && key != lockingKey) {
			throw new IllegalArgumentException("can't unlock: was locked with a different key");
		}
		locked = false;
		lockingKey = null;
		return this;
	}

	private void checkUnlocked() throws IllegalStateException {
		if (locked) {
			throw new IllegalStateException("builder object is locked. unlock() first.");
		}
	}

	/**
	 * returns a description of objects this builder creates
	 * @return	a description of objects this builder creates
	 */
	public String getDescription() {
		return description == null ? "" : description;
	}

	/**
	 * Provide another builder whose parameters this builder will use as defaults;
	 * parameters set in this builder will override those.
	 * 
	 * @param superBuilder	another builder that can handle the same fields as this
	 * 						one, so its type must be equal to or a supertype of this
	 * 						builder's
	 * 
	 * @return	this builder
	 * 
	 * @throws IllegalStateException if this builder is {@link #lock(Object) locked}
	 */
	public final ReflectiveObjectBuilder<T> extend(final ReflectiveObjectBuilder<? super T> superBuilder) throws IllegalStateException {
		checkUnlocked();
		this.superBuilder = superBuilder;
		return this;
	}

	/**
	 * Provide a free-text description of the objects this builder creates.
	 * 
	 * @param description	any String object
	 * 
	 * @return	this builder
	 * 
	 * @throws IllegalStateException if this builder is {@link #lock(Object) locked}
	 */
	public final ReflectiveObjectBuilder<T> desciption(final String description) throws IllegalStateException {
		checkUnlocked();
		this.description = description;
		return this;
	}

	/**
	 * Sets the value of a field of the object that will be created.
	 * 
	 * @param name	the field's name
	 * @param value	the value this field will be set to
	 *
	 * @return	this builder
	 *
	 * @throws IllegalArgumentException	if there is no field with <tt>name</tt>
	 * 									or if the field does not accept the type of <tt>value</tt>
	 * @throws IllegalStateException if this builder is {@link #lock(Object) locked}
	 */
	public final ReflectiveObjectBuilder<T> setParam(final String name, final Object value) throws IllegalArgumentException, IllegalStateException {
		Validate.notEmpty(name);
		checkUnlocked();
		validateParam(name, value);
		parameters.put(name, value);
		return this;
	}

	/**
	 * Adds a value to a collection field of the object that will be created.
	 * <p/>
	 * If the field is not initialized to a collection after the call to the
	 * object's default constructor, it will be initialized to a collection
	 * object of undefined type.
	 * <p/>
	 * If the field is initialized after the call to the default constructor,
	 * the <tt>value</tt> will be added to the collection.
	 * 
	 * @param name	the field's name
	 * @param value	the value this field will be set to
	 *
	 * @return	this builder
	 *
	 * @throws IllegalArgumentException	if there is no field with <tt>name</tt>,
	 * 									or if the field is not a collection,
	 * 									or if the field does not accept the type of <tt>value</tt>
	 * @throws IllegalStateException if this builder is {@link #lock(Object) locked}
	 */
	public final ReflectiveObjectBuilder<T> addListParam(final String listparamName, final Object element) throws IllegalArgumentException, IllegalStateException {
		checkUnlocked();
		validateListParam(listparamName, element);
		recordListParam(listparamName, element);
		return this;
	}

	/**
	 * create the object with its fields set according to the
	 * {@link #setParam(String, Object) parameters}
	 * (and {@link #addListParam(String, Object) list parameters})
	 * that are set.
	 * 
	 * @return a new object of the {@link #ReflectiveObjectBuilder(Class) given} type
	 * 
	 * @see #ReflectiveObjectBuilder(Class)
	 * @see #getType()
	 * @see #setParam(String, Object)
	 * @see #addListParam(String, Object)
	 */
	public T build() {
		validate();
		final T instance = instantiate();
		populate(instance);
		return instance;
	}

	/** perform validation before building an object */
	private void validate() {
	}

	/** fill fields according to parameters */
	private void populate(final T instance) {
		if (superBuilder != null) {
			superBuilder.populate(instance);
		}
		setParameters(instance);
		setListParameters(instance);
	}

	/** set field values according to parameters */
	private void setParameters(final T instance) {
		for (final Entry<String, Object> param : parameters.entrySet()) {
			setField(instance, param.getKey(), param.getValue());
		}
	}

	/** add list parameters to collection fields, initializing them if necessary */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setListParameters(final T instance) {
		for (final Entry<String, List<?>> entry: listparameters.entrySet()) {
			final String name = entry.getKey();
			final List listparam = entry.getValue();
			Collection actual = (Collection) readField(instance, name);
			if (actual == null) {
				actual = initCollection(instance, entry.getKey(), listparam);
			}
			actual.addAll(listparam);
		}
	}

	@SuppressWarnings("rawtypes")
	private Collection initCollection(final T instance, final String key, final List<?> value) {
		final Class<? extends Collection> collectionClass = DEFAULT_COLLECTION_CLASS;
		Collection list;
		try {
			list = collectionClass.newInstance();
		} catch (final InstantiationException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new ImpossibleRuntimeException(e);
		}
		setField(instance, key, list);
		return list;
	}

	/** create a new instance of target type */
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

	/** does target type have a field with the given name whose type matches the intended value? */
	private void validateParam(final String name, final Object value) throws IllegalArgumentException {
		validateHasAccessibleField(name);
		validateFieldCanContain(name, value);
	}

	/** does target type have a collection field with the given name? */
	private void validateListParam(final String listparamName, final Object element) {
		validateHasAccessibleField(listparamName);
		validateFieldCanAddElement(listparamName, element);
		Validate.notNull(element);
	}

	/** remember a list parameter element to add to a collection field later */
	private <E> void recordListParam(final String listparamName, final E element) {
		List<E> list = (List<E>) listparameters.get(listparamName);
		if (list == null) {
			list = new LinkedList<E>();
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

	/** return a usable default constructor or {@code null} */
	private Constructor<T> getAccessibleDefaultConstructor(final Class<T> type) {
		Constructor<T> c = null;
		try {
			c = type.getDeclaredConstructor();
			c.setAccessible(true);
		} catch (final NoSuchMethodException e) {
			// return null
		} catch (final SecurityException e) {
			// return null
		}
		return c;
	}

	/** return a field declared in the target class or a superclass and make it accessible, or return {@code null} */
	private Field getAccessibleField(final String name) {
		Field field = fields.get(name);
		if (field == null) {
			final Queue<Class<?>> q = new LinkedList<Class<?>>();
			final Set<Class<?>> seen = new HashSet<Class<?>>();
			q.offer(type);
			while (field == null && !q.isEmpty()) {
				final Class<?> c = q.poll();
				seen.add(c);
				try {
					field = c.getDeclaredField(name);
					field.setAccessible(true);
				} catch (final NoSuchFieldException e) {
					final Class<?> superClass = c.getSuperclass();
					if (superClass != null && !seen.contains(superClass)) {
						q.offer(superClass);
					}
				} catch (final SecurityException e) {
					return null;
				}
			}
			fields.put(name, field);
		}
		return field;
	}

	/** returns the current value */
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

	/** sets the value */
	private void setField(final T instance, final String name, final Object value) {
		final Field field = getAccessibleField(name);
		try {
			field.set(instance, value);
		} catch (final IllegalArgumentException e) {
			throw new ImpossibleRuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new ImpossibleRuntimeException(e);
		}
	}

	/** exception if no such field */
	private void validateHasAccessibleField(final String name) throws IllegalArgumentException {
		if (getAccessibleField(name) == null) {
			throw new IllegalArgumentException("field not present or not writable: \"" + name + "\"");
		}
	}

	/** exception if field won't take value */
	private void validateFieldCanContain(final String name, final Object value) {
		final Field field = getAccessibleField(name);
		if (value == null) {
			validateFieldMayContainNull(field);
		} else {
			validateFieldCanContainObject(field, value);
		}
	}

	/** checks for primitive types; no generics check is done */
	private void validateFieldMayContainNull(final Field field) {
		if (field.getType().isPrimitive()) {
			throw new IllegalArgumentException(
					"trying to assign null to field, but type is primitive: "
							+ field.getName() + ": " + field.getType().getSimpleName()
					);
		}
	}

	/** checks raw reference types; no generics check is done */
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

	/** is there a collection in that field? no further checks. */
	private void validateFieldCanAddElement(final String name, final Object value) {
		final Field field = getAccessibleField(name);
		validateFieldIsCollection(field);
		//TODO validateGenericsAppropriateForValue(field, value);
	}

	private void validateFieldIsCollection(final Field field) {
		final Class<?> fieldType = field.getType();
		if (!java.util.Collection.class.isAssignableFrom(fieldType)) {
			throw new IllegalArgumentException("field does not contain a collection: " + field.getName());
		}
	}

}
