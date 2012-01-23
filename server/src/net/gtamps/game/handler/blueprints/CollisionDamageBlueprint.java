package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.CollisionDamageHandler;
import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler.Type;

public class CollisionDamageBlueprint extends HandlerBlueprint<Entity> {

	float impulseDmgMultiply;
	int baseDamage;

	public CollisionDamageBlueprint(final Universe universe, final int baseDamage, final float impulseDmgMultiply) {
		super(universe, Type.COLLISION);
		this.baseDamage = baseDamage;
		this.impulseDmgMultiply = impulseDmgMultiply;
	}

	private CollisionDamageBlueprint(final CollisionDamageBlueprint other) {
		this(other.universe, other.baseDamage, other.impulseDmgMultiply);
	}

	@Override
	public ServersideHandler<Entity> createHandler(final Entity parent) {
		return new CollisionDamageHandler(universe, parent, baseDamage, impulseDmgMultiply);
	}

	@Override
	public HandlerBlueprint<Entity> copy() {
		return new CollisionDamageBlueprint(this);
	}

}
