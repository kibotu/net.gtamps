package net.gtamps.shared.game.handler;

import java.lang.reflect.Constructor;

import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;

import org.jetbrains.annotations.NotNull;

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

	/**
	 * make sure a property of the parent entity the handler wants to use
	 * actually exists and is of the correct type
	 * 
	 * @param <T>
	 * @param name
	 * @param type
	 * @return
	 * 
	 * @throws RuntimeException	if anything goes wrong
	 */
	protected <T> Propertay<T> useParentProperty(@NotNull String name, @NotNull T value) {
		Propertay<T> p = parent.getProperty(name.toLowerCase());
		if (p == null) {
			try {
				Constructor<Propertay> c = Propertay.class.getConstructor(GameObject.class, String.class, Object.class); 
				p = (Propertay<T>) c.newInstance(this.parent, name.toLowerCase(), value);
			} catch (Exception e) {
				// TODO catchall for a multitude of possible exceptions; ok like that?
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(), e);
			}
			parent.addProperty(p);
//		} else if (!type.isInstance(p)) {
			//throw new IllegalArgumentException("Property type mismatch: parent entity already uses same property with different type");
		}
//		return type.cast(p);
		return p;
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
