package net.gtamps.game.handler;

import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

import org.jdom.Element;

public class ShootingHandler extends Handler{
	private EventType[] sendsUp = {};
	private EventType[] receivesDown = {EventType.ACTION_SHOOT};

	EntityManager entityManager;
	long lastShot = System.currentTimeMillis();
	
	public ShootingHandler(Entity parent, EntityManager em) {
		super(Handler.Type.SHOOTING, parent);
		this.entityManager = em;
		this.setSendsUp(sendsUp);
		this.setReceivesDown(receivesDown);
		this.connectUpwardsActor(parent);
	}
	@Override
	public void receiveEvent(GameEvent event) {
		EventType type = event.getType();
		if (type.isType(EventType.ACTION_SHOOT)) {
			if(System.currentTimeMillis()-lastShot>WorldConstants.GUN_SHOT_MILLIS){
				Propertay<Float> x = this.getParent().getProperty("posx");
				Propertay<Float> y = this.getParent().getProperty("posy");
				Propertay<Float> rot = this.getParent().getProperty("rotation");
				entityManager.createEntityBullet(x.value().intValue(), y.value().intValue(), rot.value().intValue());
				//PositionProperty pp = (PositionProperty) this.getParent().getProperty(Property.Type.POSITION);
				//entityManager.createEntityBullet((int)pp.getX(), (int)pp.getY(), (int)pp.getRotation());
				lastShot = System.currentTimeMillis();
			}
		} 
	}
	
//	@Override
//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(baseRevision, keeper);
//		if (e != null) {
//			//Attribute value = new Attribute(XmlElements.ATTRIB_VALUE.tagName(), driverStr);
//			//e.setAttribute(value);
//		}
//		return e;
//	}
}
