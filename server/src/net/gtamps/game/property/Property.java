package net.gtamps.game.property;

import net.gtamps.XmlElements;
import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.GameObject;

public abstract class Property extends GameActor {

	public enum Type {
		POSITION, SPEED, HEALTH, OWNER, INCARNATION, DRIVER, ACTIVATION
	}

	protected final GameObject parent;
	protected final Property.Type type;

	public Property(Property.Type type, GameObject parent) {
		super(type.name().toLowerCase());
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
