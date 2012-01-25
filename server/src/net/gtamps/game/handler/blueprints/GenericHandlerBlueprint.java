package net.gtamps.game.handler.blueprints;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

/**
 * generic blueprint for handlers that do not need special initialization,
 * i.e. there's a standard constructor only requiring a parent Entity parameter.
 *
 * @param <H>
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 */
public class GenericHandlerBlueprint<H extends ServersideHandler<Entity>> extends HandlerBlueprint<Entity> {

	private final Class<H> classObject;

	public GenericHandlerBlueprint(final Universe universe, final Class<H> classObject, final Handler.Type type) {
		super(universe, type);
		this.classObject = classObject;
	}

	@Override
	public ServersideHandler<Entity> createHandler(final Entity parent) {
		Constructor<H> constructor;
		H handler;
		try {
			constructor = classObject.getConstructor(Universe.class, Entity.class);
			handler = constructor.newInstance(universe, parent);
		} catch (final SecurityException e) {
			handler = null;
			e.printStackTrace();
			throw new NoSuchElementException("cannot instantiate handler: \n" + e.toString());
		} catch (final Exception e) {
			handler = null;
			e.printStackTrace();
			throw new NoSuchElementException("cannot instantiate handler: \n" + e.toString());
		}
		return handler;
	}

	@Override
	public HandlerBlueprint<Entity> copy() {
		return new GenericHandlerBlueprint<H>(universe, classObject, getType());
	}

}
