package net.gtamps.shared.game.handler;


import net.gtamps.shared.game.IGameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;

/**
 * Handlers handle stuff for {@link Entity entities}, mainly events of a certain
 * type. They make entities modular in that way, behavior-wise and whatnot.
 * Handlers can make use of of an entity's {@link Propertay properties}.
 *
 * @author jan, tom, til
 */
public class Handler implements IGameActor {

	public enum Type {
		DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING, HEALTH
	}

	protected final Type type;
	protected final Entity parent;
	protected final IGameActor actor;


	public Handler(final Type type, final Entity parent) {
		if (parent == null) {
			throw new IllegalArgumentException("'parent' must not be null");
		}
		this.actor = new SharedGameActor(type.name().toLowerCase());
		this.parent = parent;
		this.type = type;
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
	public void receiveEvent(final GameEvent event) {
		actor.receiveEvent(event);
	}


	@Override
	public boolean isEnabled() {
		return actor.isEnabled();
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
	public void removeEventListener(final EventType type, final IGameEventListener listener) {
		actor.removeEventListener(type, listener);
	}


	@Override
	public void connectUpwardsActor(final SharedGameActor other) {
		actor.connectUpwardsActor(other);
	}


	@Override
	public void dispatchEvent(final GameEvent event) {
		actor.dispatchEvent(event);
	}

	@Override
	public void disconnectUpwardsActor(final SharedGameActor other) {
		actor.disconnectUpwardsActor(other);
	}

	@Override
	public void setReceives(final EventType[] receivesDown) {
		actor.setReceives(receivesDown);
	}

	public Entity getParent() {
		return this.parent;
	}

	@Override
	public String toString() {
		String s = "";
		s = String.format("%s (%s)", this.getName(), this.isEnabled() ? "on" : "off");
		return s;
	}

}
