package net.gtamps.game.handler;


import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtamps.game.physics.MobilityProperties;
import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

public class MobilityHandler extends ServersideHandler<Entity> {

	private static final LogType TAG = LogType.PHYSICS;
	private static final EventType[] down = {EventType.ACTION_EVENT, EventType.SESSION_UPDATE,
		EventType.ENTITY_DESTROYED};


	private final MobilityProperties mobilityProperties;
	private final SimplePhysicsHandler physics;

	protected ConcurrentLinkedQueue<GameEvent> actionQueue = new ConcurrentLinkedQueue<GameEvent>();

	protected World world = null;


	//TODO getRidOfThese
	protected float velocityForce;
	protected float steeringForce;
	protected float steeringRadius;
	protected float slidyness;

	public MobilityHandler(final Universe universe, final Entity parent, final MobilityProperties mobilityProperties, final SimplePhysicsHandler physicsHandler) {
		super(universe, Handler.Type.MOBILITY, parent);
		this.mobilityProperties = mobilityProperties;
		velocityForce = mobilityProperties.VELOCITY_FORCE;
		steeringForce = mobilityProperties.STEERING_FORCE;
		steeringRadius= mobilityProperties.STEERING_RADIUS;
		slidyness = mobilityProperties.SLIDYNESS;
		physics = physicsHandler;
		world = physics.getWorld();
		setReceives(down);
		connectUpwardsActor(parent);

	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.ACTION_EVENT)) {
			actionQueue.add(event);
		} else if (type.isType(EventType.SESSION_UPDATE)) {
			update();
		} else if (type.isType(EventType.ENTITY_DESTROYED)) {
			// FIXME handle deactivation of action events differently
			// there's supposed to be a driver handler or something anyway
			parent.removeEventListener(EventType.ACTION_EVENT, this);
		}
	}

	public void addAction(final GameEvent event) {
		if (event.getType().isType(EventType.ACTION_EVENT)) {
			actionQueue.add(event);
		}
	}

	@Override
	public void enable() {
		super.enable();
	}

	public void update() {

		if (!isEnabled() || !physics.isEnabled()) {
			return;
		}

		final Body body = physics.getBody();

		// put all player inputs inside the physics engine
		final Vec2 front = new Vec2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle()));
		final Vec2 worldCenter = body.getWorldCenter();

		final float forward = Math.signum(Vec2.dot(front, body.getLinearVelocity()));

		while (!actionQueue.isEmpty()) {
			final EventType pa = actionQueue.poll().getType();

			//            if (pa == EventType.ACTION_SUICIDE) {
			//                eventRoot.dispatchEvent(new CollisionEvent(parent, parent, 100f));
			//            }

			if (mobilityProperties.TYPE == MobilityProperties.Type.CAR) {
				if (pa == EventType.ACTION_ACCELERATE) {
					final Vec2 force = new Vec2((float) Math.cos(body.getAngle()) * velocityForce, (float) Math.sin(body.getAngle()) * velocityForce);
					body.applyForce(force, body.getWorldCenter());
				}
				if (pa == EventType.ACTION_DECELERATE) {
					final Vec2 force = new Vec2((float) -Math.cos(body.getAngle()) * velocityForce, (float) -Math.sin(body.getAngle()) * velocityForce);
					body.applyForce(force, body.getWorldCenter());
				}
				if (pa == EventType.ACTION_TURNRIGHT) {
					final Vec2 force = new Vec2((float) Math.cos(body.getAngle() + Math.PI / 2f), (float) Math.sin(body.getAngle() + Math.PI / 2f));
					body.applyForce(force.mul(steeringForce * forward), worldCenter.add(front.mul(steeringRadius)));
				}
				if (pa == EventType.ACTION_TURNLEFT) {
					final Vec2 force = new Vec2((float) Math.cos(body.getAngle() - Math.PI / 2f), (float) Math.sin(body.getAngle() - Math.PI / 2f));
					body.applyForce(force.mul(steeringForce * forward), worldCenter.add(front.mul(steeringRadius)));
				}
			}
			if (mobilityProperties.TYPE == MobilityProperties.Type.HUMAN) {
				GUILogger.getInstance().log(LogType.PHYSICS, parent.toString());
				if (pa == EventType.ACTION_ACCELERATE) {
					final Vec2 force = new Vec2((float) Math.cos(body.getAngle()) * velocityForce, (float) Math.sin(body.getAngle()) * velocityForce);
					GUILogger.getInstance().log(LogType.PHYSICS, "Accelerate " + force);
					body.setLinearVelocity(force);
					body.wakeUp();
				}
				if (pa == EventType.ACTION_DECELERATE) {
					final Vec2 force = new Vec2((float) -Math.cos(body.getAngle()) * velocityForce, (float) -Math.sin(body.getAngle()) * velocityForce);
					body.setLinearVelocity(force);
					body.wakeUp();
				}
				if (pa == EventType.ACTION_TURNRIGHT) {
					body.wakeUp();
					body.setAngularVelocity(mobilityProperties.STEERING_FORCE);
				}
				if (pa == EventType.ACTION_TURNLEFT) {
					body.wakeUp();
					body.setAngularVelocity(-mobilityProperties.STEERING_FORCE);
				}
			}
		}


		if (mobilityProperties.TYPE == MobilityProperties.Type.CAR) {
			//			Apply orthogonal friction, so that all the cars appear to run on "tracks"
			//			deteremines whether the current velocity is directed to the front of the vehicle.
			final float speed = body.getLinearVelocity().length();
			//			front is normalized: set it to the length of the current speed:
			final Vec2 frontvectorvelocity = front.mul(speed * forward);
			//			mix the current velocity vector with the front vector, according to the slidyness
			body.setLinearVelocity(body.getLinearVelocity().mul(slidyness).add(frontvectorvelocity.mul(1f - slidyness)));
		}

		if (mobilityProperties.TYPE == MobilityProperties.Type.HUMAN) {
			body.setAngularVelocity(body.getAngularVelocity() * 0.9f);
			body.setLinearVelocity(body.getLinearVelocity().mul(0.9f));
		}
		if (mobilityProperties.TYPE == MobilityProperties.Type.CAR) {
			body.setAngularVelocity(body.getAngularVelocity() * 0.93f);
			body.setLinearVelocity(body.getLinearVelocity().mul(0.96f));
		}

	}
}
