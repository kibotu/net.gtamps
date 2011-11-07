package net.gtamps.shared.game;

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
    public void connectUpwardsActor(final SharedGameActor other);
    /**
     * @param actor
     * @see #connectUpwardsActor(SharedGameActor)
     */
    public void connectDownwardsActor(final SharedGameActor actor);

    public void disconnectUpwardsActor(final SharedGameActor other);

    public void disconnectDownwardsActor(final SharedGameActor actor);

    /**
     * Declare the types of gameEvents this actor wants to pass upwards.
     * Must have no types in common with {@link #setReceivesDown(EventType[]) receivesDown}.
     * Use before {@link #connectUpwardsActor(SharedGameActor)} or
     * {@link #connectDownwardsActor(SharedGameActor)}.
     */
    public void setSendsUp(final EventType[] sendsUp) ;

    /**
     * Declare the types of gameEvents this actor is interested in receiving
     * from upwards. Must have no types in common with {@link #setSendsUp(EventType[]) sendsUp}.
     * Use before {@link #connectUpwardsActor(SharedGameActor)} or
     * {@link #connectDownwardsActor(SharedGameActor)}.
     */
    public void setReceivesDown(final EventType[] receivesDown);
	

}