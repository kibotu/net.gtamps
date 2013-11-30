package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.serializer.communication.StringConstants;

public class CollisionDamageHandler extends ServersideHandler<Entity> {

	private static final EventType[] receives = { EventType.ENTITY_COLLIDE };

	float impulseDmgMultiply;
	int baseDamage;

	public CollisionDamageHandler(final Universe universe, final Entity parent, final int baseDamage, final float impulseDmgMultiply) {
		super(universe, Type.COLLISION, parent, receives);

		this.baseDamage = baseDamage;
		this.impulseDmgMultiply = impulseDmgMultiply;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		assert event.getType().isType(EventType.ENTITY_COLLIDE);
		assert parent.getUid() == event.getSourceUid() || parent.getUid() == event.getTargetUid();
		final float impulse = Float.valueOf(event.useProperty(StringConstants.PROPERTY_VALUE, "0").value());
		final int dmg = baseDamage + (int) (impulse * impulseDmgMultiply);
		final GameEvent dmgEvent = new GameEvent();
		dmgEvent.setType(EventType.ENTITY_DAMAGE);
		final int sourceUid = parent.getUid();
		final int targetUid = event.getSourceUid() == sourceUid ? event.getTargetUid() : event.getSourceUid();
		dmgEvent.setSourceUid(sourceUid);
		dmgEvent.setTargetUid(targetUid);
		dmgEvent.setCause(event);
		dmgEvent.useProperty(StringConstants.PROPERTY_VALUE, "").set(String.valueOf(dmg));
		getUniverse().dispatchEvent(dmgEvent);
	}

}
