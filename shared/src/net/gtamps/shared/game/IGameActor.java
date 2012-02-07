package net.gtamps.shared.game;

import java.util.Set;

import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

public interface IGameActor extends IGameEventListener, IGameEventDispatcher {

	public abstract String getName();

	public abstract boolean isEnabled();

	public abstract void enable();

	public abstract void disable();

	/**
	 * Connect another gameActor to this one so that events will be passed
	 * between them; links will be set up for the eventTypes declared by
	 * {@link #getReceivedEventTypes(EventType[])}. The other actor is
	 * implicitly considered to be "upwards" in the hierarchy, which is
	 * really just a semantic convention that has no meaning beyond who
	 * will be added as {@link IGameEventListener listener} to whom
	 * for the respective eventType.
	 *
	 * @param other the other, "upwards", actor that events will be sent
	 *              to and received from; not <code>null</code>
	 * @throws IllegalArgumentException if an attempt is made to connect
	 *                                  an actor to itself
	 * @see EventType
	 */
	public void connectUpwardsActor(final IGameActor other);

	public void disconnectUpwardsActor(final IGameActor other);

	/**
	 * Returns the types of gameEvents this actor is interested in receiving
	 * from upwards. Use before {@link #connectUpwardsActor(IGameActor)}.
	 */
	public Set<EventType> getReceivedEventTypes();

	public void registerListeningActor(final IGameActor listener, final Set<EventType> types);

}