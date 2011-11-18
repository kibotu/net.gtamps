package net.gtamps.shared.game.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameEventDispatcher implements IGameEventDispatcher {

	private static final String LOGTAG = "GAMEWORLD";

	Map<EventType, Set<IGameEventListener>> listeners = new ConcurrentHashMap<EventType, Set<IGameEventListener>>();

	@Override
	public void addEventListener(final EventType type, final IGameEventListener listener) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("'listener' must not be null");
		}
		if (type.hasSubtypes()) {
			for (final EventType subtype : type.getSubtypes()) {
				addEventListener(subtype, listener);
			}
		} else {
			addSingleListener(type, listener);
		}
	}

	@Override
	public void removeEventListener(final EventType type, final IGameEventListener listener) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("'listener' must not be null");
		}
		if (type.hasSubtypes()) {
			for (final EventType subtype : type.getSubtypes()) {
				removeEventListener(subtype, listener);
			}
		} else {
			removeSingleListener(type, listener);
		}
	}

	@Override
	public void dispatchEvent(final GameEvent event) {
		if (event == null) {
			throw new IllegalArgumentException("'event' must not be null");
		}
		Set<IGameEventListener> specificListeners = null;
		if (listeners.get(event.getType()) != null) {
			specificListeners = Collections.synchronizedSet(listeners.get(event.getType()));
		}
		if (specificListeners != null) {
			for (final IGameEventListener l : specificListeners) {
				//FIXME ugly as hell, but necessary to get to our goal.
				try {
					l.receiveEvent(event);
				} catch (final java.util.ConcurrentModificationException e) {
					continue;
				}
			}
		}
	}

	private void addSingleListener(final EventType type, final IGameEventListener listener) {
		assert type != null;
		assert listener != null;
		if (!listeners.containsKey(type)) {
			listeners.put(type, new HashSet<IGameEventListener>());
		}
		listeners.get(type).add(listener);
	}

	private void removeSingleListener(final EventType type, final IGameEventListener listener) {
		assert type != null;
		assert listener != null;
		if (!listeners.containsKey(type)) {
			return;
		}
		listeners.get(type).remove(listener);
	}

	@Override
	public boolean isRegisteredListener(final IGameEventListener listener) {
		for (final Entry<EventType, Set<IGameEventListener>> entry : listeners.entrySet()) {
			if (entry.getValue().contains(listener)) {
				return true;
			}
		}
		return false;
	}


}
