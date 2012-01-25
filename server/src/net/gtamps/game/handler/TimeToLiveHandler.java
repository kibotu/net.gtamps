package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

public class TimeToLiveHandler extends ServersideHandler<Entity> {

	static EventType[] receives = { EventType.SESSION_UPDATE };

	long startRevision;
	long lifespanRevisions;

	public TimeToLiveHandler(final Universe universe, final Entity parent, final long lifespan) {
		super(universe, Type.AUTODISPOSE, parent);

		setReceives(receives);
		connectUpwardsActor(parent);

		startRevision = universe.getRevision();
		lifespanRevisions = lifespan;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		assert event.getType().isType(EventType.SESSION_UPDATE);
		final Universe universe = getUniverse();
		final long now = universe.getRevision();
		final long revisionsLived = now - startRevision;
		if (revisionsLived > lifespanRevisions) {
			final GameEvent destroyEvent = new GameEvent(EventType.ENTITY_DESTROYED, parent);
			universe.dispatchEvent(destroyEvent);
		}
	}

}
