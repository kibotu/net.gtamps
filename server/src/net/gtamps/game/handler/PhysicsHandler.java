package net.gtamps.game.handler;


import java.util.concurrent.ConcurrentLinkedQueue;

import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.CollisionEvent;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class PhysicsHandler extends SimplePhysicsHandler {

	protected ConcurrentLinkedQueue<GameEvent> actionQueue = new ConcurrentLinkedQueue<GameEvent>();

	public PhysicsHandler(final Universe universe, final Entity parent, final Body physicalRepresentation, final PhysicalProperties physicalProperties) {
		super(universe, parent, physicalRepresentation, physicalProperties);
	}

	@Override
	public void enable() {
		final int pixX = parent.x.value();
		final int pixY = parent.y.value();
		final int rota = parent.rota.value();
		body = null;
		while (body == null) {
			body = PhysicsFactory.createHuman(world, physicalProperties, pixX, pixY, rota);
		}
		super.enable();
	}

	@Override
	public void update() {

		if (!isEnabled()) {
			if (body != null) {
				world.destroyBody(body);
				body = null;
			}
			return;
		}

		// put all player inputs inside the physics engine
		final Vec2 front = new Vec2((float) Math.cos(body.getAngle()), (float) Math.sin(body.getAngle()));
		final Vec2 worldCenter = body.getWorldCenter();

		final float forward = Math.signum(Vec2.dot(front, body.getLinearVelocity()));

		while (!actionQueue.isEmpty()) {
			final EventType pa = actionQueue.poll().getType();

			if (pa == EventType.ACTION_SUICIDE) {
				eventRoot.dispatchEvent(new CollisionEvent(parent, parent, 100f));
			}

			if (physicalProperties.TYPE == PhysicalProperties.Type.CAR) {
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
			if (physicalProperties.TYPE == PhysicalProperties.Type.HUMAN) {
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
					body.setAngularVelocity(physicalProperties.STEERING_FORCE);
				}
				if (pa == EventType.ACTION_TURNLEFT) {
					body.wakeUp();
					body.setAngularVelocity(-physicalProperties.STEERING_FORCE);
				}
			}
		}


		if (physicalProperties.TYPE == PhysicalProperties.Type.CAR) {
			//			Apply orthogonal friction, so that all the cars appear to run on "tracks"
			//			deteremines whether the current velocity is directed to the front of the vehicle.
			final float speed = body.getLinearVelocity().length();
			//			front is normalized: set it to the length of the current speed:
			final Vec2 frontvectorvelocity = front.mul(speed * forward);
			//			mix the current velocity vector with the front vector, according to the slidyness
			body.setLinearVelocity(body.getLinearVelocity().mul(slidyness).add(frontvectorvelocity.mul(1f - slidyness)));
		}

		if (physicalProperties.TYPE == PhysicalProperties.Type.HUMAN) {
			body.setAngularVelocity(body.getAngularVelocity() * 0.9f);
			body.setLinearVelocity(body.getLinearVelocity().mul(0.9f));
		}
		if (physicalProperties.TYPE == PhysicalProperties.Type.CAR) {
			body.setAngularVelocity(body.getAngularVelocity() * 0.93f);
			body.setLinearVelocity(body.getLinearVelocity().mul(0.96f));
		}

		super.update();

	}
}
