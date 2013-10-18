package net.gtamps.shared.game.handler;


import java.util.Set;

import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.IGameEventDispatcher;

/**
 * Handlers handle stuff for {@link Entity entities}, mainly events of a certain
 * type. They make entities modular in that way, behavior-wise and whatnot.
 * Handlers can make use of of an entity's {@link Propertay properties}.
 *
 * @author jan, tom, til
 */
@SuppressWarnings("serial")
public abstract class Handler<T extends SharedGameActor> extends SharedGameActor {

	public enum Type {
		DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING, HEALTH, PLAYER_CONTROL, COLLISION, AUTODISPOSE
	}

	protected final Type type;
	protected final T parent;
	protected final IGameEventDispatcher eventRoot;
	private final Set<EventType> receivedEventTypes;

	public Handler(final IGameEventDispatcher eventRoot, final Type type, final T parent, final EventType...eventTypes) {
		super(type.name());
		if (parent == null) {
			throw new IllegalArgumentException("'parent' must not be null");
		}
		this.parent = parent;
		this.type = type;
		this.eventRoot = eventRoot;
		this.receivedEventTypes = toImmutableSet(eventTypes);
		connectUpwardsActor(parent);
	}

	public T getParent() {
		return this.parent;
	}

	@Override
	public Set<EventType> getReceivedEventTypes() {
		return receivedEventTypes;
	}

	@Override
	public String toString() {
		String s = "";
		s = String.format("%s (%s)", getName(), isEnabled() ? "on" : "off");
		return s;
	}

}
