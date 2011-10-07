package net.gtamps.game;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.XmlElements;

import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.jdom.Element;

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
public abstract class GameObject {
	private static final long START_REVISION = RevisionKeeper.START_REVISION;
	private static final String DEFAULT_NAME = "";
	private static final String PRODUCTION_TAG = "obj";
	private static final String DEBUG_TAG = "object";
	private static final String DEFAULT_TAG = GTAMultiplayerServer.DEBUG ? DEBUG_TAG
			: PRODUCTION_TAG;

	protected final String xmlTag;
	protected final int uid;
	protected String name;
	protected long revision = START_REVISION;
	protected boolean hasChanged = true;
	private boolean silent = false;
	private Map<String, Propertay<?>> properties = null;

	/**
	 * 
	 * 
	 * @param name
	 *            default is {@value #DEFAULT_NAME}
	 * @param xmlTag
	 *            the name of this type of object as an XML element; like this:
	 *            <code>&lt;xmlTag...&gt;</code>.<br/>
	 *            default is {@value #DEFAULT_TAG}
	 */
	public GameObject(String name, String xmlTag) {
		if (name == null) {
			name = DEFAULT_NAME;
		}
		if (xmlTag == null) {
			xmlTag = DEFAULT_TAG;
		}
		this.uid = GTAMultiplayerServer.getNextUID();
		this.name = name;
		this.xmlTag = xmlTag;
	}

	/**
	 * Return an XML element containing the data of this game object as a diff
	 * to the given revision. Only data elements that were changed since this
	 * revision will be included. If there was no change, <code>null</code> will
	 * be returned.
	 * 
	 * If a <code>revisionKeeper</code> is provided and this gameObject has the
	 * {@link #hasChanged() changed flag} set, it will update its revision to
	 * the {@link RevisionKeeper#getNextRevision() upcoming} revision number.
	 * 
	 * @param baseRevision
	 * @param keeper
	 * @return an XML element containing the data of this game object that has
	 *         been changed since <code>baseRevision</code>, or
	 *         <code>null</code>.
	 */
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		if (this.silent || !(this.revision > baseRevision || this.hasChanged)) {
			return null;
		}
		if (this.hasChanged && keeper != null) {
			this.updateRevision(keeper.getNextRevision());
		}
		Element e = new Element(this.xmlTag);
		e.setAttribute(XmlElements.ATTRIB_UID.tagName(), this.uid + "");
		e.setAttribute(XmlElements.ATTRIB_NAME.tagName(), this.name);
		if (this.properties != null) {
			for (Propertay<?> p : properties.values()) {
				Element f = p.toXMLElement(baseRevision, keeper);
				if (f != null) {
					e.addContent(f);
				}
			}
		}
		return e;
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
	 * {@link #toXMLElement(long)} method will under all circumstances return
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
			T check = (T) p.get();
		} catch (RuntimeErrorException e) {
			return null;
		}
		return p;
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
