package net.gtamps.game.handler;

import net.gtamps.game.handler.blueprints.PhysicsBlueprint;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.serializer.communication.StringConstants;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class SimplePhysicsHandler extends ServersideHandler<Entity> {
	@SuppressWarnings("unused")
	private static final LogType TAG = LogType.PHYSICS;
	private static final EventType[] down = {
		EventType.SESSION_UPDATE,
		EventType.ENTITY_DESTROYED
	};

	protected Body body;

	protected final IProperty<Integer> speedxProperty;
	protected final IProperty<Integer> speedyProperty;

	private final PhysicsBlueprint blueprint;
	private float initialImpulse = 0f;

	public SimplePhysicsHandler(final Universe universe, final Entity parent, final PhysicsBlueprint blueprint) {
		super(universe, Handler.Type.PHYSICS, parent);
		setReceives(down);
		connectUpwardsActor(parent);

		this.blueprint = blueprint;

		speedxProperty = parent.useProperty(StringConstants.PROPERTY_SPEEDX, 0);
		speedyProperty = parent.useProperty(StringConstants.PROPERTY_SPEEDY, 0);
	}

	public SimplePhysicsHandler(final Universe universe, final Entity parent, final PhysicsBlueprint blueprint, final int initialImpulseMagnitude) {
		this(universe, parent, blueprint);
		initialImpulse = initialImpulseMagnitude;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.ACTION_EVENT)) {
			// nothing
		} else if (type.isType(EventType.SESSION_UPDATE)) {
			update();
		} else if (type.isType(EventType.ENTITY_DESTROYED)) {
			// FIXME handle deactivation of action events differently
			// there's supposed to be a driver handler or something anyway
			parent.removeEventListener(EventType.ACTION_EVENT, this);
		}
	}

	public void applyImpulse(final Vec2 impulse) {
		if (body != null) {
			body.wakeUp();
			body.applyImpulse(impulse, body.getWorldCenter());
		}
	}

	public void update() {
		final Vec2 pos = body.getWorldCenter();
		final Vec2 vel = body.getLinearVelocity();
		parent.x.set(PhysicsFactory.lengthToWorld(pos.x));
		parent.y.set(PhysicsFactory.lengthToWorld(pos.y));
		parent.rota.set(PhysicsFactory.angleToWorld((body.getAngle())));
		speedxProperty.set(PhysicsFactory.lengthToWorld(vel.x));
		speedyProperty.set(PhysicsFactory.lengthToWorld(vel.y));
	}

	Body getBody() {
		return body;
	}

	World getWorld() {
		return blueprint.getWorld();
	}

	@Override
	public void enable() {
		super.enable();
		createBody();
	}

	@Override
	public void disable() {
		super.disable();
		destroyBody();
	}

	private void destroyBody() {
		if (body != null) {
			blueprint.getWorld().destroyBody(body);
			body = null;
		}
	}

	private void createBody() {
		final Entity parent = getParent();
		body = blueprint.createBody(parent, parent.x.value(), parent.y.value(), parent.rota.value());
		applyInitialImpulse(body);
	}

	private void applyInitialImpulse(final Body body) {
		if (initialImpulse == 0f) {
			return;
		}
		final float rota = body.getAngle();
		final Vec2 impulse = new Vec2((float)Math.cos(rota), (float) Math.sin(rota));
		impulse.mulLocal(initialImpulse);
		applyImpulse(impulse);
	}

}
