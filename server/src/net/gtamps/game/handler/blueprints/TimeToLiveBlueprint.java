package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.handler.TimeToLiveHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler.Type;

public class TimeToLiveBlueprint extends HandlerBlueprint<Entity> {

	long lifespan;

	public TimeToLiveBlueprint(final Universe universe, final long lifespan) {
		super(universe, Type.AUTODISPOSE);
		this.lifespan = lifespan;
	}

	public TimeToLiveBlueprint(final TimeToLiveBlueprint other) {
		this(other.universe, other.lifespan);
	}

	@Override
	public ServersideHandler<Entity> createHandler(final Entity parent) {
		return new TimeToLiveHandler(universe, parent, lifespan);
	}

	@Override
	public HandlerBlueprint<Entity> copy() {
		return new TimeToLiveBlueprint(this);
	}

}
