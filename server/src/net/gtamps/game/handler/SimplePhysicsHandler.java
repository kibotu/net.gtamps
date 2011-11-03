package net.gtamps.game.handler;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class SimplePhysicsHandler extends Handler {
	@SuppressWarnings("unused")
	private static final LogType TAG = LogType.PHYSICS;
	private static final EventType[] up = { EventType.ENTITY_COLLIDE, EventType.ENTITY_SENSE, EventType.ENTITY_BULLET_HIT };
	private static final EventType[] down = { EventType.SESSION_UPDATE,
			EventType.ENTITY_DESTROYED };

	protected Entity parent;
	protected Body body;
	protected World world;

	protected PhysicalProperties physicalProperties;
	protected float velocityForce;
	protected float steeringForce;
	protected float steeringRadius;
	protected float slidyness;
	
	protected final IProperty<Integer> speedxProperty;
	protected final IProperty<Integer> speedyProperty;

	public SimplePhysicsHandler(final Entity parent, final Body physicalRepresentation, final PhysicalProperties physicalProperties) {
		super(Handler.Type.PHYSICS, parent);
		this.parent = parent;
		body = physicalRepresentation;
		world = body.getWorld();
//		this.physicalProperties = physicalProperties;
		setSendsUp(up);
		setReceivesDown(down);
		connectUpwardsActor(parent);

		speedxProperty = parent.useProperty("speedx", 0);
		speedyProperty = parent.useProperty("speedy", 0);
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
		if(body!=null){
			body.wakeUp();
			body.applyImpulse(impulse, body.getWorldCenter());
		}
	}

	public void update() {
		
		if (!isEnabled()) {
			if (body != null) {
				world.destroyBody(body);
				body = null;
			}
			return;
		}
		
		parent.x.set(PhysicsFactory.lengthToWorld(body.getWorldCenter().x));
		parent.y.set(PhysicsFactory.lengthToWorld(body.getWorldCenter().y));
		parent.rota.set(PhysicsFactory.angleToWorld((body.getAngle())));
		speedxProperty.set(PhysicsFactory.lengthToWorld(body.getLinearVelocity().x));
		speedyProperty.set(PhysicsFactory.lengthToWorld(body.getLinearVelocity().y));
	}
	
	Body getBody() {
		return body;
	}
	
	World getWorld() {
		return world;
	}

}
