package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

public class TimeToLiveHandler extends ServersideHandler<Entity> {

	private static final EventType[] receives = { EventType.SESSION_UPDATE };

	long startRevision;
	long lifespanRevisions;

	public TimeToLiveHandler(final Universe universe, final Entity parent, final long lifespan) {
		super(universe, Type.AUTODISPOSE, parent, receives);

		startRevision = universe.getRevision();
		lifespanRevisions = lifespan;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		assert event.getType().isType(EventType.SESSION_UPDATE);
		final long now = getUniverse().getRevision();
		if (now - startRevision > lifespanRevisions) {
			final GameEvent destroyEvent = new GameEvent(EventType.ENTITY_DESTROYED, parent);
			getUniverse().dispatchEvent(destroyEvent);
		}
	}

}
