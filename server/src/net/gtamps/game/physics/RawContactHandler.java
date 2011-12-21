package net.gtamps.game.physics;

import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.BulletHitEvent;
import net.gtamps.shared.game.event.CollisionEvent;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.handler.Handler;

import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

/**
 * Interprets collisions as reported by the physics-engine and sends underway
 * the appropriate events.
 *
 * @author jan, tom, til
 */
public final class RawContactHandler implements ContactListener {
	private static final LogType TAG = LogType.PHYSICS;

	private final IGameEventDispatcher eventRoot;

	public RawContactHandler(final IGameEventDispatcher eventRoot) {
		if (eventRoot == null) {
			throw new IllegalArgumentException("'eventRoot' must not be 'null'");
		}
		this.eventRoot = eventRoot;
	}

	@Override
	public void add(final ContactPoint point) {
		final Entity one = (Entity) point.shape1.getBody().getUserData();
		final Entity two = (Entity) point.shape2.getBody().getUserData();
		EventType sensorType;
		if (point.shape1.isSensor() && point.shape2.isSensor()) {
			GUILogger.i().log(TAG, "sensor/sensor contact: SHOULD BE FILTERED!");
			return;
		} else if (point.shape1.isSensor()) {
			sensorType = (EventType) point.shape1.getUserData();
			sensorContact(one, two, sensorType);
		} else if (point.shape2.isSensor()) {
			sensorType = (EventType) point.shape2.getUserData();
			sensorContact(two, one, sensorType);
		}
	}

	@Override
	public void persist(final ContactPoint point) {
		// Entity one = (Entity) point.shape1.getBody().getUserData();
		// Entity two = (Entity) point.shape2.getBody().getUserData();
		// Logger.i().log(TAG, String.format("persist contact: %s, %s", one,
		// two));
	}

	@Override
	public void remove(final ContactPoint point) {
		final Entity one = (Entity) point.shape1.getBody().getUserData();
		final Entity two = (Entity) point.shape2.getBody().getUserData();
		EventType sensorType;
		if (point.shape1.isSensor() && point.shape2.isSensor()) {
			return;
		} else if (point.shape1.isSensor()) {
			sensorType = (EventType) point.shape1.getUserData();
			sensorContact(one, two, sensorType, true);
		} else if (point.shape2.isSensor()) {
			sensorType = (EventType) point.shape2.getUserData();
			sensorContact(two, one, sensorType, true);
		}
	}

	@Override
	public void result(final ContactResult point) {
		final Object one = point.shape1.getBody().getUserData();
		final Object two = point.shape2.getBody().getUserData();
		if (point.shape1.isSensor() || point.shape2.isSensor()) {
			//			throw new RuntimeException("sensor events shouldn't pass through here!");
			GUILogger.i().log(TAG, "SENSOR events in contact result callback! sensors aren't supposed to do this.");
		} else {
			hardContact(one, two, point.normalImpulse);
		}
	}

	private void sensorContact(final Object sensor, final Object sensed, final EventType sensorType) {
		sensorContact(sensor, sensed, sensorType, false);
	}

	private void sensorContact(final Object sensor, final Object sensed, final EventType sensorType, final boolean remove) {
		//Logger.i().log(TAG,	String.format("sensor contact: %s, %s, %s", sensor, sensed, remove ? "END" : "BEGIN"));
		if (sensor == null || sensed == null || !(sensor instanceof Entity)
				|| !(sensed instanceof Entity)) {
			return;
		}
		final Entity sensorEntity = (Entity) sensor;
		final Entity sensedEntity = (Entity) sensed;
		final GameEvent event = new GameEvent(sensorType, sensorEntity, sensedEntity,
				remove ? GameEvent.END_VALUE : GameEvent.BEGIN_VALUE);
		GUILogger.i().log(TAG, event.toString());
		eventRoot.dispatchEvent(event);

		//		SimplePhysicsHandler handler1 = sensorEntity.getPhysicsHandler();
		//		SimplePhysicsHandler handler2 = sensedEntity.getPhysicsHandler();
		//		if (handler1 != null) {
		//			handler1.dispatchEvent(event);
		//		}
		//		if (handler2 != null) {
		//			handler2.dispatchEvent(event);
		//		}

	}

	private void hardContact(final Object one, final Object two, final float impulse) {
		if (one == null || two == null || !(one instanceof Entity)
				|| !(two instanceof Entity)) {
			return;
		}
		final Entity oneEntity = (Entity) one;
		final Entity twoEntity = (Entity) two;
		GUILogger.i().log(
				TAG,
				String.format("collision: %s, %s [%d]", oneEntity, twoEntity,
						(int) impulse));
		final SimplePhysicsHandler handler1 = (SimplePhysicsHandler) oneEntity.getHandler(Handler.Type.PHYSICS);
		final SimplePhysicsHandler handler2 = (SimplePhysicsHandler) twoEntity.getHandler(Handler.Type.PHYSICS);
		GameEvent event = null;
		if (oneEntity.getName().equals("bullet") || twoEntity.getName().equals("bullet")) {
			event = new BulletHitEvent(oneEntity, twoEntity, impulse);
		} else {
			event = new CollisionEvent(oneEntity, twoEntity, impulse);
		}
		eventRoot.dispatchEvent(event);
	}
}
