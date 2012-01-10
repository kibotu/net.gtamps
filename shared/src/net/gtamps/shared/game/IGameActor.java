package net.gtamps.shared.game;

import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

import java.util.Set;

public interface IGameActor extends IGameEventListener, IGameEventDispatcher {

    public abstract String getName();

    public abstract boolean isEnabled();

    public abstract void enable();

    public abstract void disable();

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
    public void connectUpwardsActor(final IGameActor other);

    public void disconnectUpwardsActor(final IGameActor other);

    /**
     * Declare the types of gameEvents this actor is interested in receiving
     * from upwards. Must have no types in common with {@link #setSendsUp(EventType[]) sendsUp}.
     * Use before {@link #connectUpwardsActor(SharedGameActor)} or
     * {@link #connectDownwardsActor(SharedGameActor)}.
     */
    public void setReceives(final EventType[] receivesDown);

    public void registerListeningActor(final IGameActor listener, final Set<EventType> types);

}