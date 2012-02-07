package net.gtamps.shared.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

/**
 * <p>
 * A GameActor is a GameObject that can dispatch and receive GameEvents.
 * Received events are automatically re-dispatched (override
 * {@link IGameEventListener#receiveEvent(GameEvent) receiveEvent(GameEvent)}
 * to change this behavior). This happens so that
 * gameActors can be connected to form hierarchical message chains or
 * nets, hierarchical meaning that there is a sense of direction:
 * up and down.
 * </p><p>
 * By declaring which types of events a gameActor intends
 * to send upwards, and which types it will handle when passed down, these
 * connections can be joined automatically, by using
 * {@link #connectUpwardsActor(IGameActor)}.
 * </p><p>
 * Owing to the indiscriminate nature of {@link IGameEventDispatcher}-style
 * event handling (events themselves have no sense of the direction they're
 * travelling in), it is important that a gameActor does not send upwards
 * the same type of event it receives from there. Otherwise, an infinite
 * loop would result. It is, however, possible to receive events from the
 * opposite direction that they are sent. What this boils down to is that,
 * between the same actors, gameEvents can be sent in only one direction.
 * Nothing stops you from constructing an indirect loop, though.
 * </p>
 *
 * @author jan, tom, til
 */
public abstract class SharedGameActor extends GameObject implements IGameActor {

	private static final long serialVersionUID = 4996110383027919751L;

	private transient GameEventDispatcher eventDispatcher = new GameEventDispatcher();

	private boolean enabled = true;

	public SharedGameActor(final String name) {
		super(name);
	}

	public SharedGameActor(final String name, final int uid) {
		super(name, uid);
	}

	public SharedGameActor() {
		super();
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#enable()
	 */
	@Override
	public void enable() {
		enabled = true;
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#disable()
	 */
	@Override
	public void disable() {
		enabled = false;
	}

	/**
	 * Connect another gameActor to this one so that events will be passed
	 * between them; links will be set up for the eventTypes declared in
	 * {@link #sendsUp} and {@link #receivesDown}. The other actor is
	 * implicitly considered to be "upwards" in the hierarchy, which is
	 * really just a semantic convention that has no meaning beyond who
	 * will be added as {@link IGameEventListener listener} to whom
	 * for the respective eventType.
	 *
	 * @param other the other, "upwards", actor that events will be sent
	 *              to and received from; not <code>null</code>
	 * @throws IllegalArgumentException if an attempt is made to connect
	 *                                  an actor to itself
	 * @throws IllegalStateException    if an EventType is declared to be sent
	 *                                  both ways between this actor and the other one
	 * @see EventType
	 */
	@Override
	public void connectUpwardsActor(final IGameActor other) {
		{
			if (other == this) {
				final String msg = "Trying to connect a gameActor to itself. " +
						"This is not allowed.";
				throw new IllegalArgumentException(msg);
			}
		}
		final Set<EventType> receivesDown = getReceivedEventTypes();
		if (receivesDown != null) {
			other.registerListeningActor(this, receivesDown);
		}
	}

	@Override
	public void disconnectUpwardsActor(final IGameActor other) {
		removeEventListener(EventType.GAME_EVENT, other);
		other.removeEventListener(EventType.GAME_EVENT, this);
	}

	@Override
	public void registerListeningActor(final IGameActor listener, final Set<EventType> types) {
		for (final EventType type : types) {
			addEventListener(type, listener);
		}
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#receiveEvent(net.gtamps.shared.game.event.GameEvent)
	 */
	@Override
	public void receiveEvent(final GameEvent event) {
		//Logger.i().log(LogType.GAMEWORLD, this+" "+event);
		dispatchEvent(event);
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#addEventListener(net.gtamps.shared.game.event.EventType, net.gtamps.shared.game.event.IGameEventListener)
	 */
	@Override
	public void addEventListener(final EventType type, final IGameEventListener listener) {
		eventDispatcher.addEventListener(type, listener);
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#removeEventListener(net.gtamps.shared.game.event.EventType, net.gtamps.shared.game.event.IGameEventListener)
	 */
	@Override
	public void removeEventListener(final EventType type, final IGameEventListener listener) {
		eventDispatcher.removeEventListener(type, listener);
	}

	/* (non-Javadoc)
	 * @see net.gtamps.shared.game.IGameActor#dispatchEvent(net.gtamps.shared.game.event.GameEvent)
	 */
	@Override
	public void dispatchEvent(final GameEvent event) {
		if (isEnabled()) {
			eventDispatcher.dispatchEvent(event);
		}
	}

	@Override
	public boolean isRegisteredListener(final IGameEventListener listener) {
		return eventDispatcher.isRegisteredListener(listener);
	}

	/**
	 * utility function to convert an {@link EventType} array to a set.
	 * @param types
	 * @return
	 */
	protected static Set<EventType> toImmutableSet(final EventType... types) {
		assert Validate.noNullElements(types);
		return Collections.unmodifiableSet(new HashSet<EventType>(Arrays.asList(types)));
	}
}
