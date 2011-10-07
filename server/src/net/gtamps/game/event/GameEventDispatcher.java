package net.gtamps.game.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameEventDispatcher implements IGameEventDispatcher {
	
	Map<EventType, Set<IGameEventListener>> listeners = new ConcurrentHashMap<EventType, Set<IGameEventListener>>();

	@Override
	public void addEventListener(EventType type, IGameEventListener listener) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("'listener' must not be null");
		}
		if (type.hasSubtypes()) {
			for (EventType subtype : type.getSubtypes()) {
				addEventListener(subtype, listener);
			}
		} else {
			addSingleListener(type, listener);
		}
	}
	
	@Override
	public void removeEventListener(EventType type, IGameEventListener listener) {
		if (type == null) {
			throw new IllegalArgumentException("'type' must not be null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("'listener' must not be null");
		}
		if (type.hasSubtypes()) {
			for (EventType subtype : type.getSubtypes()) {
				removeEventListener(subtype, listener);
			}
		} else {
			removeSingleListener(type, listener);
		}
	}

	@Override
	public void dispatchEvent(GameEvent event) {
		if (event == null) {
			throw new IllegalArgumentException("'event' must not be null");
		}
		Set<IGameEventListener> specificListeners = null;
		if(listeners.get(event.getType()) != null){
			specificListeners = Collections.synchronizedSet(listeners.get(event.getType()));
		}
		if (specificListeners != null) {
			for (IGameEventListener l : specificListeners) {
				//FIXME ugly as hell, but necessary to get to our goal.
				try{
					l.receiveEvent(event);
				} catch (java.util.ConcurrentModificationException e){
					continue;
				}
			}
		}
	}

	private void addSingleListener(EventType type, IGameEventListener listener) {
		assert type != null;
		assert listener != null;
		if (!listeners.containsKey(type)) {
			listeners.put(type, new HashSet<IGameEventListener>());
		}
		listeners.get(type).add(listener);
	}
	
	private void removeSingleListener(EventType type, IGameEventListener listener) {
		assert type != null;
		assert listener != null;
		if (!listeners.containsKey(type)) {
			return;
		}
		listeners.get(type).remove(listener);
	}
	

}
