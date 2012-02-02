package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

public class DeathExplosionHandler extends ServersideHandler<Entity> {

	private static EventType[] triggers = {EventType.ENTITY_DESTROYED};

	public DeathExplosionHandler(final Universe universe, final Entity parent) {
		super(universe, Type.AUTODISPOSE, parent);
		setReceives(triggers);
		connectUpwardsActor(parent);
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		getUniverse().dispatchEvent(new GameEvent(EventType.ENTITY_EXPLOSION, parent));
	}

}
