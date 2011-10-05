package net.gtamps.game.event;

import net.gtamps.XmlElements;
import net.gtamps.game.GameActor;
import net.gtamps.game.RevisionKeeper;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jdom.Element;

public class EventManager extends GameActor {
	
	private final long eventTimeout;
	private final Set<GameEvent> archive = Collections.synchronizedSet(new HashSet<GameEvent>());
	
	public EventManager(long eventTimeout) {
		super("EventManager", XmlElements.EVENTS.tagName());
		if (eventTimeout < 0) {
			throw new IllegalArgumentException("'eventTimeout' must be > 0");
		}
		this.eventTimeout = eventTimeout;
		
		// all received events will be communicated via getUpdate()
		this.addEventListener(EventType.GAME_EVENT, this);
		this.removeEventListener(EventType.SESSION_UPDATE, this);
		this.removeEventListener(EventType.ACTION_ACCELERATE, this);
		this.removeEventListener(EventType.ACTION_DECELERATE, this);
		this.removeEventListener(EventType.ACTION_TURNLEFT, this);
		this.removeEventListener(EventType.ACTION_TURNRIGHT, this);
	}

	@Override
	public void receiveEvent(GameEvent event) {
		synchronized (archive) {
			this.add(event);
		}
	}
	
	@Override
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(baseRevision, keeper);
		if (e != null) {
			e.addContent(this.getUpdate(baseRevision, keeper));
		}
		return e;
	}
	
	public List<Element> getUpdate(long baseRevision, RevisionKeeper keeper) {
		List<Element> update = new LinkedList<Element>();
		List<GameEvent> toBeRemoved = new LinkedList<GameEvent>();
		synchronized (archive) {
			for (GameEvent event: archive) {
				if (keeper != null && keeper.getCurrentRevision() - event.getRevision() > eventTimeout) {
					toBeRemoved.add(event);
					continue;
				}
				Element xml = event.toXMLElement(baseRevision, keeper);
				if (xml != null) {
					update.add(xml);
				}
			}
			archive.removeAll(toBeRemoved);
		}
		return update;
	}

	private void add(GameEvent event) {
		archive.add(event);
		super.setChanged();
	}

}
