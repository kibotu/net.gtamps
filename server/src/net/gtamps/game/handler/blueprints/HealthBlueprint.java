package net.gtamps.game.handler.blueprints;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.handler.HandlerBlueprint;

public class HealthBlueprint extends HandlerBlueprint {
	
	private final String handlerName = "net.gtamps.game.handler.HealthHandler";
	
	private final int maxHealth;
	private final int dmgThreshold;
	private final float dmgMultiplier;

	public HealthBlueprint(final int maxHealth) {
		this(maxHealth, 0f, 0);
	}
	
	public HealthBlueprint(final int maxHealth, final float dmgMultiplier, final int dmgThreshold) {
		super(Handler.Type.HEALTH);
		this.maxHealth = maxHealth;
		this.dmgMultiplier = dmgMultiplier;
		this.dmgThreshold =  dmgThreshold;
	}
	
	public HealthBlueprint(final HealthBlueprint other) {
		this(other.maxHealth, other.dmgMultiplier, other.dmgThreshold);
	}

	@Override
	public Handler createHandler(final Entity parent, final Integer pixX, final Integer pixY,
			final Integer deg)  {
		return instantiateHandler(parent);
	}
	
	private Handler instantiateHandler(final Entity parent) {
		Handler instance = null;
		try {
			final Class<?> handlerClass = Class.forName(handlerName);
			final Constructor<?> constructor = handlerClass.getConstructor(Entity.class, int.class, float.class, int.class);
			final Object hobject =  constructor.newInstance(parent, maxHealth, dmgMultiplier, dmgThreshold);
			instance = (Handler) hobject;
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
