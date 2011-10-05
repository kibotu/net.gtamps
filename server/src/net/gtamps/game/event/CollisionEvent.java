package net.gtamps.game.event;

import net.gtamps.game.GameObject;

public class CollisionEvent extends GameEvent {
	

	public CollisionEvent(GameObject source, GameObject target, float impulse) {
		super(EventType.ENTITY_COLLIDE, source, target, Float.toString(impulse));
	}
	
//	@Override
//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(baseRevision, keeper);
//		if (e != null) {
//			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(), impulse+""));
//		}
//		return e;
//	}
	
	public float getImpulse() {
		return Float.parseFloat(this.value);
	}


}
