package net.gtamps.game.handler;

import java.util.HashSet;
import java.util.Set;

import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

/**
 * A bare-bones sensor handler than can be easily extended to provide
 * actual functionality.
 *
 * @author jan, tom, til
 */
@SuppressWarnings("serial")
public abstract class SensorHandler extends ServersideHandler<Entity> {
	private static final LogType TAG = LogType.GAMEWORLD;

	protected Set<Entity> sensed = new HashSet<Entity>();
	protected final EventType sensorType;
	protected final EventType triggerEvent;

	public SensorHandler(final Universe universe, final EventType sensorType, final EventType triggerEvent, final Entity parent) {
		super(universe, Handler.Type.SENSOR, parent, sensorType, triggerEvent);
		this.sensorType = sensorType;
		this.triggerEvent = triggerEvent;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(sensorType)) {
			sense(event);
		} else if (type.isType(triggerEvent)) {
			act(event);
		}
	}

	protected abstract void act(final GameEvent event);

	protected void sense(final GameEvent event) {
		assert event.getType().isType(sensorType);
		final Universe universe = getUniverse();
		final GameObject source = universe.getGameObject(event.getSourceUid());
		final GameObject target = universe.getGameObject(event.getTargetUid());
		final GameObject subject = (source == getParent()) ? target : source;
		if (event.isBegin()) {
			sensed.add((Entity) subject);
		} else if (event.isEnd()) {
			sensed.remove(subject);
		}
		if (sensorType.isType(EventType.ENTITY_SENSE_DOOR)) {
			GUILogger.i().log(TAG, target + " interacted with door sensor of " + source);
		}

	}

	protected void update() {
		// nothing
	}
}
