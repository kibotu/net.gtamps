package net.gtamps.game.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.gtamps.game.world.World;
import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

import org.jetbrains.annotations.NotNull;

public class EventManager extends GameActor {
	
	public static final long EVENT_TIMEOUT_MILLIS = 3000;
	
	@NotNull
	private final World world;
	private final Set<GameEvent> archive = Collections.synchronizedSet(new HashSet<GameEvent>());
	
	public EventManager(final World world) {
		super("EventManager");
		
		this.world = world;
		
		// all received events will be communicated via getUpdate()
		addEventListener(EventType.GAME_EVENT, this);
		removeEventListener(EventType.SESSION_UPDATE, this);
		removeEventListener(EventType.ACTION_ACCELERATE, this);
		removeEventListener(EventType.ACTION_DECELERATE, this);
		removeEventListener(EventType.ACTION_TURNLEFT, this);
		removeEventListener(EventType.ACTION_TURNRIGHT, this);
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		synchronized (archive) {
			add(event);
		}
	}
	
	public List<GameObject> update(final long baseRevision) {
		final ArrayList<GameObject> updates = new ArrayList<GameObject>(archive.size());
		final long currentRevision = world.getRevision();
		for (final GameEvent e : archive) {
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
		archive.add(event);
		super.setChanged();
	}

}
