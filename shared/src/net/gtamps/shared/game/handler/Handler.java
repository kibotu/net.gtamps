package net.gtamps.shared.game.handler;


import net.gtamps.shared.game.IGameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

import java.util.Set;

/**
 * Handlers handle stuff for {@link Entity entities}, mainly events of a certain
 * type. They make entities modular in that way, behavior-wise and whatnot.
 * Handlers can make use of of an entity's {@link Propertay properties}.
 *
 * @author jan, tom, til
 */
public abstract class Handler<T extends SharedGameActor> implements IGameActor {

	public enum Type {
		DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING, HEALTH, PLAYER_CONTROL, COLLISION, AUTODISPOSE
	}

	protected final Type type;
	protected final T parent;
	protected final IGameActor actor;
	protected final IGameEventDispatcher eventRoot;


	public Handler(final IGameEventDispatcher eventRoot, final Type type, final T parent) {
		if (parent == null) {
			throw new IllegalArgumentException("'parent' must not be null");
		}
		this.actor = new SharedGameActor(type.name().toLowerCase());
		this.parent = parent;
		this.type = type;
		this.eventRoot = eventRoot;
		actor.addEventListener(EventType.GAME_EVENT, this);
	}

	@Override
	public void enable() {
		actor.enable();
	}

	@Override
	public void disable() {
		actor.disable();
	}

	@Override
	public String getName() {
		return actor.getName();
	}

	@Override
	public void addEventListener(final EventType type, final IGameEventListener listener) {
		actor.addEventListener(type, listener);
	}

	@Override
	public boolean isEnabled() {
		return actor.isEnabled();
	}

	@Override
	public void removeEventListener(final EventType type, final IGameEventListener listener) {
		actor.removeEventListener(type, listener);
	}

	@Override
	public void connectUpwardsActor(final IGameActor other) {
		actor.connectUpwardsActor(other);
	}

	@Override
	public void dispatchEvent(final GameEvent event) {
		actor.dispatchEvent(event);
	}

	@Override
	public boolean isRegisteredListener(final IGameEventListener listener) {
		return actor.isRegisteredListener(listener);
	}

	@Override
	public void disconnectUpwardsActor(final IGameActor other) {
		actor.disconnectUpwardsActor(other);
	}

	@Override
	public void setReceives(final EventType[] receivesDown) {
		actor.setReceives(receivesDown);
	}

	public T getParent() {
		return this.parent;
	}

	@Override
	public String toString() {
		String s = "";
		s = String.format("%s (%s)", this.getName(), this.isEnabled() ? "on" : "off");
		return s;
	}

	@Override
	public void registerListeningActor(final IGameActor listener, final Set<EventType> types) {
		actor.registerListeningActor(listener, types);

	}

}
