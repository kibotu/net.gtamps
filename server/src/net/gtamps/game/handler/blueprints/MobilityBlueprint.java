package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.MobilityHandler;
import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.MobilityProperties;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

public class MobilityBlueprint extends HandlerBlueprint<Entity> {

	private final MobilityProperties mobProp;

	public MobilityBlueprint(final Universe universe, final MobilityProperties mobProp) {
		super(universe, Handler.Type.MOBILITY);
		this.mobProp = mobProp;
	}

	public MobilityBlueprint(final MobilityBlueprint other) {
		this(other.universe, other.mobProp);
	}

	@Override
	public ServersideHandler<Entity> createHandler(final Entity parent) {
		final MobilityHandler mobh = new MobilityHandler(universe, parent, mobProp, (SimplePhysicsHandler) parent.getHandler(Handler.Type.PHYSICS));
		return mobh;
	}

	@Override
	public HandlerBlueprint<Entity> copy() {
		return new MobilityBlueprint(this);
	}


}
