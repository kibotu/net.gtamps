package net.gtamps.shared.game;

import net.gtamps.shared.Utils.UIDGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * A superclass for all objects the game needs to handle; the following features
 * are provided:
 * 
 * <ul>
 * <li>unique identification,</li>
 * <li>version control,</li>
 * <li>generic, serializable properties.</li>
 * </ul>
 * 
 * @author til, tom, jan
 * 
 */
public abstract class GameObject implements Serializable {

	public static final int INVALID_UID = UIDGenerator.INVALID_UID;
	
	private static final long serialVersionUID = 7826642603562424002L;
	private static final long START_REVISION = 1;
	private static final String DEFAULT_NAME = "GameObject";

	protected final int uid;
	protected String name;
	protected long revision = START_REVISION;
	protected boolean hasChanged = true;
	private boolean silent = false;
	private Map<String, IProperty<?>> properties = null;

	/**
	 * 
	 * @param name
	 *            default is {@value #DEFAULT_NAME}
	 */
	public GameObject(String name) {
		if (name == null) {
			name = DEFAULT_NAME;
		}
		this.uid = UIDGenerator.getNewUID();
		this.name = name;
	}

	public int getUid() {
		return this.uid;
	}

	public String getName() {
		return this.name;
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
		return this.revision;
	}

	/**
	 * Mark the current state of this gameObject as part of the given revision;
	 * the {@link #hasChanged() hasChanged}-flag will be unset.
	 * 
	 * @param newRevision
	 */
	public void updateRevision(long newRevision) {
		// FIXME make protected (at least!)
		this.revision = newRevision;
		this.hasChanged = false;
	}

	/**
	 * <code>true</code> if this gameObject has been modified since the last
	 * change was made public in a {@link #getRevision() revision}.
	 * 
	 * @return
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	/**
	 * sets the {@link #hasChanged changed-flag} to <code>true</code>
	 */
	public void setChanged() {
		this.hasChanged = true;
	}

	/**
	 * If set to true this game object (entity) will not send any updates to
	 * the client.
	 * 
	 * @param silence
	 *            if set to true, this game object is silent.
	 */
	public void setSilent(boolean silence) {
		this.silent = silence;
	}

	/**
	 * <code>true</code> if this gameObject is silent, which means that it
	 * should not be communicated to connected, potentially interested
	 * parties.  
	 * 
	 * @return <code>true</code> if this gameObject is silent.
	 */
	public boolean isSilent() {
		return this.silent;
	}
	
	/**
	 * Returns a {@link Propertay property} of this gameObject named 
	 * <code>name</code>, and, if it didn't exist already, initialized 
	 * to <code>value</code>.
	 * 
	 * @param <T>	the type of the property
	 * @param name	the name of the property; not <code>null</code>
	 * @param value	the property's initial value; not <code>null</code>
	 * @return		a property of the specified type, linked to this gameObject
	 * @throws NoSuchElementException	if a property with a different type
	 * 									already exists under the same <code>name</code>,
	 * 									or if the property couldn't be initialized for
	 * 									any reason
	 */
	public final <T> IProperty<T> useProperty(@NotNull String name, @NotNull T value) throws NoSuchElementException {
		String properName = name.toLowerCase();
		Propertay<T> p = this.getProperty(properName);
		if (p == null) {
			p = this.instantiateProperty(name, value);
			if (p == null) {
				throw new NoSuchElementException("cannot instantiate property");
			}
			this.addProperty(p);
		} else if (!value.getClass().isAssignableFrom(p.value().getClass())) {
			throw new NoSuchElementException("property already in use for different type: " + p.value().getClass().getSimpleName());
		}
		return p;
	}
	
	//TODO: add private reserveProperty() to avoid deferred throwing of Expections
	// 		from the lazy initialization of ProxyProperties
	
	/**
	 * Like {@link #useProperty(String, Object)}, but the IProperty returned will not be
	 * initialized until it is first used.
	 * <p>
	 * <strong>Note:</strong> Due to this lazy initialization technique, currently
	 * {@link #useProperty(String, Object) useProperty(...)}'s Exceptions might 
	 * get thrown upon the first use of a ProxyProperty obtained through this
	 * method.
	 * 
	 * @param <T>	the type of the property
	 * @param name	the name of the property; not <code>null</code>
	 * @param value	the property's initial value; not <code>null</code>
	 * @return		a property of the specified type, linked to this gameObject
	 * 
	 * @see #useProperty(String, Object)
	 */
	public final <T> IProperty<T> useLazyProperty(@NotNull String name, @NotNull T value) throws NoSuchElementException {
		return new ProxyProperty<T>(this, name, value);
	}
	
	
	/**
	 * returns an iterator over all Properties currently in use for this
	 * gameObject
	 * 
	 * @return	an iterator over all Properties currently in use for this
	 * gameObject
	 */
	public Iterable<IProperty<?>> getAllProperties() {
		if (this.properties == null) {
			return new Iterable<IProperty<?>>() {
				@Override
				public Iterator<IProperty<?>> iterator() {
					return new Iterator<IProperty<?>>(){
						@Override
						public boolean hasNext() {
							return false;
						}
						@Override
						public IProperty<?> next() {
							throw new NoSuchElementException();
						}
						@Override
						public void remove() {
							throw new UnsupportedOperationException();
							
						}
					};
				}
			};
		}
		return this.properties.values();
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s.%s]", this.name, this.uid, this.revision);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GameObject other = (GameObject) obj;
		if (uid != other.uid) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> Propertay<T> instantiateProperty(String name, T value) {
		Constructor<Propertay> c;
		Propertay<T> p = null;
		try {
			c = Propertay.class.getConstructor(GameObject.class, String.class, Object.class);
			p = c.newInstance(this, name.toLowerCase(), value);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	

	private void addProperty(Propertay<?> p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		if (this.properties == null) {
			this.properties = new HashMap<String, IProperty<?>>();
		}
		if(this.properties.containsKey(p.name)) {
			throw new IllegalArgumentException("Property exists already: " + p);
		}
		this.properties.put(p.name, p);
	}

	private void removeProperty(String name) {
		this.properties.remove(name);
	}
	
	@SuppressWarnings("unchecked")
	private <T> Propertay<T> getProperty(String name) {
		if (this.properties == null) {
			return null;
		}
		Propertay<?> p = (Propertay<?>) this.properties.get(name);
		return (Propertay<T>) p;
	}

}
