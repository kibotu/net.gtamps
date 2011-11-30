package net.gtamps.game.handler.blueprints;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.handler.HandlerBlueprint;

/**
 * generic blueprint for handlers that do not need special initialization,
 * i.e. there's a standard constructor only requiring a parent Entity parameter.
 *
 * @param <H>
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 */
public class GenericHandlerBlueprint<H extends Handler> extends HandlerBlueprint {

    private final Class<H> classObject;

    public GenericHandlerBlueprint(final IGameEventDispatcher eventRoot, final Class<H> classObject, final Handler.Type type) {
        super(eventRoot, type);
        this.classObject = classObject;
    }

    @Override
    public Handler createHandler(final Entity parent, final Integer pixX, final Integer pixY,
                                 final Integer deg) {
        Constructor<H> constructor;
        H handler;
        try {
            constructor = classObject.getConstructor(Entity.class);
            handler = constructor.newInstance(parent);
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
    public HandlerBlueprint copy() {
        return new GenericHandlerBlueprint<H>(eventRoot, classObject, getType());
    }

}
