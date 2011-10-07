package net.gtamps.game.property;

import net.gtamps.XmlElements;
import net.gtamps.game.GameActor;
import net.gtamps.game.GameObject;

public abstract class Property extends GameActor {

	public enum Type {
		POSITION, SPEED, HEALTH, OWNER, INCARNATION, DRIVER, ACTIVATION
	}

	protected final GameObject parent;
	protected final Property.Type type;

	public Property(Property.Type type, GameObject parent) {
		super(type.name().toLowerCase(), XmlElements.PROPERTY.tagName());
		this.parent = parent;
		this.type = type;
	}
	
	public Property.Type getType() {
		return this.type;
	}

	protected void setParentChanged() {
		this.parent.setChanged();
	}
	
	@Override
	public String toString() {
		return name + " [" + revision + "]";
	}
}
