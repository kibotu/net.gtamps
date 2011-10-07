package net.gtamps.game.handler;

import net.gtamps.game.GameActor;
import net.gtamps.game.Propertay;
import net.gtamps.game.entity.Entity;
import net.gtamps.game.event.GameEvent;

/**
 * Handlers handle stuff for {@link Entity entities}, mainly events of a certain
 * type. They make entities modular in that way, behavior-wise and whatnot.
 * Handlers can make use of of an entity's {@link Propertay properties}.
 *
 * @author jan, tom, til
 *
 */
public abstract class Handler extends GameActor {
	
	public enum Type {
		DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING
	}
	
	protected final Type type;
	private Entity parent = null;

	public Handler(Type type, Entity parent) {
		super(type.name().toLowerCase(), "Handler");
		if (parent == null) {
			throw new IllegalArgumentException("'parent' must not be null");
		}
		this.parent = parent;
		this.type = type;
		this.setSilent(true);
	}
	
	public Entity getParent() {
		return this.parent;
	}
	

	@Override
	public void dispatchEvent(GameEvent event) {
		if (isEnabled()) {
			super.dispatchEvent(event);
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		s = String.format("%s (%s)", this.name, this.isEnabled() ? "on" : "off");
		return s;
	}
	
}
