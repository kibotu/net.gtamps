package net.gtamps.game.handler;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.handler.Handler;

public class ShootingHandler extends Handler {
	private final EventType[] sendsUp = {};
	private final EventType[] receivesDown = {EventType.ACTION_SHOOT};

	EntityManager entityManager;
	long lastShot = System.currentTimeMillis();

	// FIXME find better way to set entityManager
	public ShootingHandler (final IGameEventDispatcher eventRoot, final Entity parent) {
		this(eventRoot, parent, null);
	}

	public ShootingHandler(final IGameEventDispatcher eventRoot, final Entity parent, final EntityManager em) {
		super(eventRoot, Handler.Type.SHOOTING, parent);
		entityManager = em;
		setReceives(receivesDown);
		connectUpwardsActor(parent);
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.ACTION_SHOOT)) {
			// FIXME: entity manager must be set
			if (entityManager != null) {
				if (System.currentTimeMillis() - lastShot > WorldConstants.GUN_SHOT_MILLIS) {
					entityManager.createEntityBullet(parent.x.value(), parent.y.value(), parent.rota.value());
					lastShot = System.currentTimeMillis();
				}
			} else {
				System.err.println("shooting handler: no entity manager set");
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
