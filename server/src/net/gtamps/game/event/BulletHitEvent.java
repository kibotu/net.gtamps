package net.gtamps.game.event;

import net.gtamps.XmlElements;
import net.gtamps.game.GameObject;
import net.gtamps.game.RevisionKeeper;

import org.jdom.Attribute;
import org.jdom.Element;

public class BulletHitEvent extends GameEvent {
	
	protected final float impulse;
	
	public BulletHitEvent(GameObject source, GameObject target, float impulse){
		super(EventType.ENTITY_BULLET_HIT, source, target);
		this.impulse = impulse;
	}
	
	@Override
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(baseRevision, keeper);
		if (e != null) {
			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(), impulse+""));
		}
		return e;
	}
	
	public float getImpulse() {
		return this.impulse;
	}
}
