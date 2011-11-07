package net.gtamps.game.property;

import net.gtamps.game.LocalGameActor;
import net.gtamps.shared.game.GameObject;

/**
 * @deprecated	replaced by net.gtamps.shared.game.IProperty
 *
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
@Deprecated
public abstract class Property extends LocalGameActor {

	public enum Type {
		POSITION, SPEED, HEALTH, OWNER, INCARNATION, DRIVER, ACTIVATION
	}

	protected final GameObject parent;
	protected final Property.Type type;

	public Property(final Property.Type type, final GameObject parent) {
		super(type.name().toLowerCase());
		this.parent = parent;
		this.type = type;
	}
	
	public Property.Type getType() {
		return type;
	}

	protected void setParentChanged() {
		parent.setChanged();
	}
	
//	@Override
//	public String toString() {
//		return name + " [" + revision + "]";
//	}
}
