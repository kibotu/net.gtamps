package net.gtamps.game.handler.blueprints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

public class HealthBlueprint extends HandlerBlueprint {

	private final String handlerName = "net.gtamps.game.handler.HealthHandler";

	private final int maxHealth;
	private final int dmgThreshold;
	private final float dmgMultiplier;

	public HealthBlueprint(final Universe universe, final int maxHealth) {
		this(universe, maxHealth, 0f, 0);
	}

	public HealthBlueprint(final Universe universe, final int maxHealth, final float dmgMultiplier, final int dmgThreshold) {
		super(universe, Handler.Type.HEALTH);
		this.maxHealth = maxHealth;
		this.dmgMultiplier = dmgMultiplier;
		this.dmgThreshold = dmgThreshold;
	}

	public HealthBlueprint(final HealthBlueprint other) {
		this(other.universe, other.maxHealth, other.dmgMultiplier, other.dmgThreshold);
	}

	@Override
	public ServersideHandler createHandler(final Entity parent, final Integer pixX, final Integer pixY,
			final Integer deg) {
		return instantiateHandler(parent);
	}

	private ServersideHandler instantiateHandler(final Entity parent) {
		ServersideHandler instance = null;
		try {
			final Class<?> handlerClass = Class.forName(handlerName);
			final Constructor<?> constructor = handlerClass.getConstructor(Universe.class, Entity.class, int.class, float.class, int.class);
			final Object hobject = constructor.newInstance(universe, parent, maxHealth, dmgMultiplier, dmgThreshold);
			instance = (ServersideHandler) hobject;
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
		return instance;
	}

	@Override
	public HandlerBlueprint copy() {
		return new HealthBlueprint(this);
	}

}
