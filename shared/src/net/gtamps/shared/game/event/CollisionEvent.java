package net.gtamps.shared.game.event;

import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.serializer.communication.StringConstants;

public class CollisionEvent extends GameEvent {


	private static final long serialVersionUID = -2768243016816037656L;

	public CollisionEvent(final GameObject source, final GameObject target, final float impulse) {
		super(EventType.ENTITY_COLLIDE, source, target);
		useProperty(StringConstants.PROPERTY_VALUE, "").set(Float.toString(impulse));
	}

	public float getImpulse() {
		return Float.valueOf(useProperty(StringConstants.PROPERTY_VALUE, "0").value());
	}


}
