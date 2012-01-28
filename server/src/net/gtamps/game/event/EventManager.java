package net.gtamps.game.event;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

import org.jetbrains.annotations.NotNull;

public class EventManager extends GameEventDispatcher implements IGameEventListener {

	public static final long EVENT_TIMEOUT_MILLIS = 5000;

	@NotNull
	private final Universe universe;
	private final ConcurrentMap<GameEvent, Object> archive = new ConcurrentHashMap<GameEvent, Object>();

	public EventManager(final Universe universe) {
		this.universe = universe;

		// all received events will be communicated via getUpdate()
		universe.addEventListener(EventType.GAME_EVENT, this);
		universe.removeEventListener(EventType.SESSION_UPDATE, this);
		universe.removeEventListener(EventType.ACTION_ACCELERATE, this);
		universe.removeEventListener(EventType.ACTION_DECELERATE, this);
		universe.removeEventListener(EventType.ACTION_TURNLEFT, this);
		universe.removeEventListener(EventType.ACTION_TURNRIGHT, this);
		universe.removeEventListener(EventType.ENTITY_SENSE, this);
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		add(event);
	}

	public ArrayList<GameObject> getUpdate(final long baseRevision) {
		final ArrayList<GameObject> updates = new ArrayList<GameObject>(archive.size());
		final long currentRevision = universe.getRevision();
		for (final GameEvent e : archive.keySet()) {
			final long eventRevision = e.getRevision();
			if (eventRevision + EVENT_TIMEOUT_MILLIS < currentRevision) {
				archive.remove(e);
				continue;
			}
			if (eventRevision > baseRevision || e.hasChanged()) {
				updates.add(e);
			}
		}
		return updates;
	}

	private void add(final GameEvent event) {
		event.updateRevision(universe.getRevision());
		archive.put(event, event);
		//		super.setChanged();
	}

}
