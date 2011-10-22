package net.gtamps.shared.game.handler;


import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;

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
		DRIVER, SENSOR, MOBILITY, PHYSICS, SHOOTING, HEALTH
	}
	
	protected final Type type;
	protected final Entity parent;

	public Handler(Type type, Entity parent) {
		super(type.name().toLowerCase());
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
