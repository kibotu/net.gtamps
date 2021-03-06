package net.gtamps.shared.game;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import net.gtamps.shared.Utils.UIDGenerator;
import net.gtamps.shared.Utils.validate.Validate;

import org.jetbrains.annotations.NotNull;


/**
 * A superclass for all objects the game needs to handle; the following features
 * are provided:
 * <p/>
 * <ul>
 * <li>unique identification,</li>
 * <li>version control,</li>
 * <li>generic, serializable properties.</li>
 * </ul>
 *
 * @author til, tom, jan
 */
public abstract class GameObject implements Serializable {

	public static final int INVALID_UID = UIDGenerator.INVALID_UID;

	private static final long serialVersionUID = 7826642603562424002L;
	private static final long START_REVISION = 1;
	private static final String DEFAULT_NAME = "GameObject";

	protected int uid;
	protected String name;
	protected long revision = START_REVISION;
	protected boolean hasChanged = true;
	private boolean silent = false;
	private boolean mutable = false;
	private Map<String, IProperty<?>> properties = null;
	private Map<String, IProperty<?>> inactiveProperties = null;


	protected GameObject() {
		this(null, INVALID_UID);
	}

	/**
	 * @param name 	default is specified in {@link #DEFAULT_NAME}
	 * 				(currently "{@value #DEFAULT_NAME}")
	 */
	public GameObject(final String name) {
		this(name, INVALID_UID);
	}

	public GameObject(String name, final int uid) {
		if (name == null) {
			name = DEFAULT_NAME;
		}
		this.uid = (uid == INVALID_UID) ? UIDGenerator.getNewUID() : uid;
		setName(name);
	}

	public GameObject(final GameObject other) {
		uid = UIDGenerator.getNewUID();
		name = other.name;
		silent = other.silent;
		hasChanged = other.hasChanged;
		revision = other.revision;
		properties = copyPropertyMap(inactiveProperties);
		deactivateAllProperties();
		properties = copyPropertyMap(properties);
	}

	private Map<String, IProperty<?>> copyPropertyMap(final Map<String, IProperty<?>> original) {
		if (original == null) {
			return null;
		}
		final Map<String, IProperty<?>> cloneMap = new HashMap<String, IProperty<?>>();
		for (final String key: original.keySet()) {
			final IProperty<?> p = original.get(key);
			final IProperty<?> newP = useProperty(key, p.value());
			cloneMap.put(key, newP);
		}
		return cloneMap;
	}

	public int getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	/**
	 * The revision number of the game state when the latest change to this
	 * gameObject was made public. (If this object {@link #hasChanged()}, it has
	 * been further modified since then.)
	 *
	 * @return revision number of the game state when the latest change to this
	 *         gameObject was made public
	 */
	public long getRevision() {
		return revision;
	}

	/**
	 * Mark the current state of this gameObject as part of the given revision;
	 * the {@link #hasChanged() hasChanged}-flag will be unset.
	 *
	 * @param newRevision
	 */
	public void updateRevision(final long newRevision) {
		// FIXME make protected (at least!)
		revision = newRevision;
		hasChanged = false;
	}

	/**
	 * <code>true</code> if this gameObject has been modified since the last
	 * {@link #setRevision(long)}.
	 *
	 * @return	<code>true</code> if this gameObject has been modified since the last
	 * {@link #setRevision(long)}.
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	/**
	 * sets the {@link #hasChanged changed-flag} to <code>true</code>
	 */
	public void setChanged() {
		hasChanged = true;
	}

	/**
	 * If set to true this game object (entity) will not send any updates to
	 * the serializer.
	 *
	 * @param silence if set to true, this game object is silent.
	 */
	public void setSilent(final boolean silence) {
		silent = silence;
	}

	/**
	 * <code>true</code> if this gameObject is silent, which means that it
	 * should not be communicated to connected, potentially interested
	 * parties.
	 *
	 * @return <code>true</code> if this gameObject is silent.
	 */
	public boolean isSilent() {
		return silent;
	}

	/**
	 * Returns a {@link Propertay property} of this gameObject named
	 * <code>name</code>, and, if it didn't exist already, initialized
	 * to <code>value</code>.
	 *
	 * @param <T>   the type of the property
	 * @param name  the name of the property; not <code>null</code>
	 * @param value the property's initial value; not <code>null</code>
	 * @return a property of the specified type, linked to this gameObject
	 * @throws NoSuchElementException if a property with a different type
	 *                                already exists under the same <code>name</code>,
	 *                                or if the property couldn't be initialized for
	 *                                any reason
	 */
	public final <T> IProperty<T> useProperty(@NotNull final String name, @NotNull final T value) throws NoSuchElementException {
		final String properName = name;
		Propertay<T> p = this.getProperty(properName);
		if (p == null) {
			p = this.instantiateProperty(name, value);
			if (p == null) {
				throw new NoSuchElementException("cannot instantiate property");
			}
			addProperty(p);
		} else if (!value.getClass().isAssignableFrom(p.value().getClass())) {
			throw new NoSuchElementException("property ("+properName+") already in use for different type: " + p.value().getClass().getSimpleName()+ " value: "+value.getClass().getSimpleName()+" : "+value.toString());
		}
		return p;
	}

	//TODO: add private reserveProperty() to avoid deferred throwing of Expections
	// 		from the lazy initialization of ProxyProperties

	/**
	 * updates the value of a property, creating it first if necessary
	 * 
	 * @param name	the name of the property to be updated; not <code>null</code>
	 * @param value	the new value of the property; not <code>null</code>
	 */
	public final <T> void updateProperty(final String name, final T value) {
		Validate.notNull(name);
		Validate.notNull(value);
		final IProperty<T> p = useProperty(name, value);
		p.set(value);
	}


	/**
	 * returns an iterator over all Properties currently in use for this
	 * gameObject
	 *
	 * @return an iterator over all Properties currently in use for this
	 *         gameObject
	 */
	public Collection<IProperty<?>> getAllProperties() {
		if (properties == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(properties.values());
	}

	@Override
	public String toString() {
		return String.format("%s [%s.%s]", name, uid, revision);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
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
		final GameObject other = (GameObject) obj;
		if (uid != other.uid) {
			return false;
		}
		return true;
	}


	void setMutable(final boolean value) {
		mutable = value;
	}

	void setUid(final int uid) {
		ensureMutable();
		this.uid = uid;
	}

	public void setName(final String name) {
		Validate.notEmpty(name);
		this.name = name;
	}

	protected void ensureMutable() throws IllegalStateException {
		if (!mutable) {
			throw new IllegalStateException("trying to alter fixed attribute in immutable state");
		}
	}

	void setRevision(final long revision) {
		this.revision = revision;
	}

	void deactivateAllProperties() {
		if (properties == null) {
			return;
		}
		if (inactiveProperties == null) {
			inactiveProperties = new HashMap<String, IProperty<?>>();
		}
		inactiveProperties.putAll(properties);
		properties.clear();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private <T> Propertay<T> instantiateProperty(final String name, final T value) {
		Constructor<Propertay> c;
		Propertay<T> p = null;
		try {
			c = Propertay.class.getConstructor(GameObject.class, String.class, Object.class);
			p = c.newInstance(this, name, value);
		} catch (final SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.getCause().printStackTrace();
		}
		return p;
	}


	private void addProperty(final Propertay<?> p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		if (properties == null) {
			properties = new HashMap<String, IProperty<?>>();
		}
		if (properties.containsKey(p.getName())) {
			throw new IllegalArgumentException("Property exists already: " + p);
		}
		properties.put(p.getName(), p);
	}

	private void removeProperty(final String name) {
		properties.remove(name);
	}

	private <T> Propertay<T> getProperty(final String name) {
		Propertay<T> p = getActiveProperty(name);
		if (p == null) {
			p = getInactiveProperty(name);
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	private <T> Propertay<T> getActiveProperty(final String name) {
		if (properties == null) {
			return null;
		}
		final Propertay<?> p = (Propertay<?>) properties.get(name);
		return (Propertay<T>) p;
	}

	@SuppressWarnings("unchecked")
	private <T> Propertay<T> getInactiveProperty(final String name) {
		if (inactiveProperties == null) {
			return null;
		}
		final Propertay<?> p = (Propertay<?>) inactiveProperties.get(name);
		return (Propertay<T>) p;
	}

	public static boolean isValidUid(final int uid) {
		return UIDGenerator.isValid(uid);
	}



}
