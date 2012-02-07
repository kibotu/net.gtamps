package net.gtamps.game.handler;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

public class ShootingHandler extends ServersideHandler<Entity> {
	private static final EventType[] receivesDown = {EventType.ACTION_SHOOT};

	EntityManager entityManager;
	long lastShot = System.currentTimeMillis();

	public ShootingHandler(final Universe universe, final Entity parent) {
		super(universe, Handler.Type.SHOOTING, parent, receivesDown);
		entityManager = universe.entityManager;
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

}
