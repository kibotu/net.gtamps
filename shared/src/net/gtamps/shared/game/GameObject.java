package net.gtamps.shared.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import net.gtamps.shared.Utils.UIDGenerator;


/**
 * A superclass for all objects the game needs to handle; the following features
 * are provided:
 * 
 * <ul>
 * <li>unique identification,</li>
 * <li>version control,</li>
 * <li>to-XML serialization.</li>
 * </ul>
 * 
 * @author til, tom, jan
 * 
 */
public abstract class GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7826642603562424002L;
	private static final long START_REVISION = 1;
	private static final String DEFAULT_NAME = "";

	protected final int uid;
	protected String name;
	protected long revision = START_REVISION;
	protected boolean hasChanged = true;
	private boolean silent = false;
	private HashMap<String, Propertay<?>> properties = null;

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

	public void setChanged() {
		this.hasChanged = true;
	}

	/**
	 * If set to true this game object (entity) will not send any xml updates to
	 * the client.
	 * 
	 * @param silence
	 *            if set to true, this game object is silent.
	 */
	public void setSilent(boolean silence) {
		this.silent = silence;
	}

	/**
	 * <code>true</code> if this gameObject is silent, which means that its
	 * method will under all circumstances return
	 * <code>null</code>.
	 * 
	 * @return <code>true</code> if this gameObject is silent.
	 */
	public boolean isSilent() {
		return this.silent;
	}

	public void addProperty(Propertay<?> p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		if (this.properties == null) {
			this.properties = new HashMap<String, Propertay<?>>();
		}
		if(this.properties.containsKey(p.name)) {
			throw new IllegalArgumentException("Property exists already: " + p);
		}
		this.properties.put(p.name, p);
	}

	public void removeProperty(String name) {
		Propertay<?> p = this.properties.remove(name);
		if (p == null) {
			p = this.properties.remove(name.toLowerCase());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> Propertay<T> getProperty(String name) {
		Propertay<T> p = (Propertay<T>) this.properties.get(name);
		if (p == null) {
			p = (Propertay<T>) this.properties.get(name.toLowerCase());
		}
		try {
			@SuppressWarnings("unused")
			T check = (T) p.value();
		} catch (RuntimeException e) {
			return null;
		}
		return p;
	}
	
	public Iterable<Propertay<?>> getAllProperties() {
		if (this.properties == null) {
			return new Iterable<Propertay<?>>() {
				@Override
				public Iterator<Propertay<?>> iterator() {
					return new Iterator<Propertay<?>>(){
						@Override
						public boolean hasNext() {
							return false;
						}
						@Override
						public Propertay<?> next() {
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
	

}
