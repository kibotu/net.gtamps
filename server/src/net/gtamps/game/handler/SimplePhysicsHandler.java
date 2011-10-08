package net.gtamps.game.handler;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;
import net.gtamps.game.property.SpeedProperty;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class SimplePhysicsHandler extends Handler {
	@SuppressWarnings("unused")
	private static final LogType TAG = LogType.PHYSICS;
	private static final EventType[] up = { EventType.ENTITY_COLLIDE, EventType.ENTITY_SENSE, EventType.ENTITY_BULLET_HIT };
	private static final EventType[] down = { EventType.ACTION_EVENT, EventType.SESSION_UPDATE,
			EventType.ENTITY_DESTROYED };

	protected Entity parent;
	protected Body body;
	protected World world;
	protected LinkedList<GameEvent> actionQueue = new LinkedList<GameEvent>();
	protected PhysicalProperties physicalProperties;
	protected float velocityForce;
	protected float steeringForce;
	protected float steeringRadius;
	protected float slidyness;

	public SimplePhysicsHandler(Entity parent, Body physicalRepresentation, PhysicalProperties physicalProperties) {
		super(Handler.Type.PHYSICS, parent);
		this.parent = parent;
		this.body = physicalRepresentation;
		this.world = body.getWorld();
		this.physicalProperties = physicalProperties;
		this.setSendsUp(up);
		this.setReceivesDown(down);
		this.connectUpwardsActor(parent);
		this.velocityForce = physicalProperties.VELOCITY_FORCE;
		this.steeringForce = physicalProperties.STEERING_FORCE;
		this.steeringRadius = physicalProperties.STEERING_RADIUS;
		this.slidyness = physicalProperties.SLIDYNESS;

	}

	@Override
	public void receiveEvent(GameEvent event) {
		EventType type = event.getType();
		if (type.isType(EventType.ACTION_EVENT)) {
			// Logger.i().log(TAG, type.toString());
			actionQueue.add(event);
		} else if (type.isType(EventType.SESSION_UPDATE)) {
			update();
		} else if (type.isType(EventType.ENTITY_DESTROYED)) {
			// FIXME handle deactivation of action events differently
			// there's supposed to be a driver handler or something anyway
			parent.removeEventListener(EventType.ACTION_EVENT, this);
		}
	}

	public void addAction(GameEvent event) {
		if (event.getType().isType(EventType.ACTION_EVENT)) {
			actionQueue.add(event);
		}
	}

	public void applyImpulse(Vec2 impulse) {
		if(body!=null){
			body.wakeUp();
			body.applyImpulse(impulse, body.getWorldCenter());
		}
	}

	public void update() {
		
		if (!this.isEnabled()) {
			if (this.body != null) {
				world.destroyBody(this.body);
				this.body = null;
			}
			return;
		}
		
//		PositionProperty p = (PositionProperty) parent.getProperty(Property.Type.POSITION);
//		SpeedProperty s = (SpeedProperty) parent.getProperty(Property.Type.SPEED);

//		if (p != null) {
			parent.x.set(PhysicsFactory.lengthToWorld(this.body.getWorldCenter().x));
			parent.y.set(PhysicsFactory.lengthToWorld(this.body.getWorldCenter().y));
			parent.rota.set(PhysicsFactory.angleToWorld((this.body.getAngle())));
//		}
//		if (s != null) {
			parent.getProperty("speedx").set(PhysicsFactory.lengthToWorld(this.body.getLinearVelocity().x));
			parent.getProperty("speedy").set(PhysicsFactory.lengthToWorld(this.body.getLinearVelocity().y));
//			s.setSpeedX(PhysicsFactory.lengthToWorld(this.body.getLinearVelocity().x));
//			s.setSpeedY(PhysicsFactory.lengthToWorld(this.body.getLinearVelocity().y));
//		}

	}


}
