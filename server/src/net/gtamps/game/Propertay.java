package net.gtamps.game;

import net.gtamps.XmlElements;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 * A new, better implementation of the Property concept, the way Tom
 * imagined it in the first place. :)
 * 
 * A property has a name and holds a single value.
 *
 * @author jan, tom, til
 *
 */
public class Propertay<T> extends GameObject {
	
	private final GameObject parent;
	private T value = null;

	public Propertay(GameObject parent, String name, T value) {
		super(name.toLowerCase(), XmlElements.PROPERTY.tagName());
		if (parent == null) {
			throw new IllegalArgumentException("'parent' must not be null");
		}
		this.parent = parent;
		if (value == null) {
			this.value = null;
			this.hasChanged = false;
		} else {
			this.value = value;
			this.parent.hasChanged = true;
		}
	}
	
	public Propertay(GameObject parent, String name) {
		this(parent, name, null);
	}
	
	public GameObject getParent() {
		return this.parent;
	}
	
	public T get() {
		return this.value;
	}
	
	public String getAsString() {
		return this.value.toString();
	}
	
	public void set(T value) {
		if (value == null) {
			throw new IllegalArgumentException("'value' must not be null");
		}
		this.value = value;
		this.hasChanged = true;
		this.parent.hasChanged = true;
	}
	
	@Override
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(baseRevision, keeper);
		if (this.value == null) {
			return null;
		}
		if (e != null) {
			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(), this.getAsString()));
		}
		return e;
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
