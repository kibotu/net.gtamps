package net.gtamps.shared.game;

import net.gtamps.shared.game.event.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
 * {@link #connectUpwardsActor(GameActor)}.
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
public abstract class GameActor extends GameObject implements
        IGameEventDispatcher, IGameEventListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4996110383027919751L;
    /**
     * The types of gameEvents this actor wants to pass upwards. Must have
     * no types in common with {@link #receivesDown}.
     */
    private transient Set<EventType> sendsUp = null;
    /**
     * The types of gameEvents this actor is interested in receiving from
     * upwards. Must have
     * no types in common with {@link #sendsUp}.
     */
    private transient Set<EventType> receivesDown = null;
    private transient GameEventDispatcher eventDispatcher = new GameEventDispatcher();
    private boolean enabled = true;

    public GameActor(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        //Logger.i().log(LogType.GAMEWORLD, "Enabling Game Actor "+this);
        this.enabled = true;
        //Logger.i().log(LogType.GAMEWORLD, this+" was enabled "+this.enabled);
    }

    public void disable() {
        this.enabled = false;
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
    protected void connectUpwardsActor(GameActor other) {
        {
            if (other == this) {
                String msg = "Trying to connect a gameActor to itself. " +
                        "This is not allowed.";
                throw new IllegalArgumentException(msg);
            }
            Set<EventType> intersection = upAndDownIntersection();
            if (intersection.size() != 0) {
                String msg = "'sendsUp' and 'receivesDown' have common EventTypes: "
                        + intersection;
                throw new IllegalStateException(msg);
            }
        }
        if (sendsUp != null) {
            this.registerListenigActor(other, sendsUp);
        }
        if (receivesDown != null) {
            other.registerListenigActor(this, receivesDown);
        }
    }

    /**
     * @param actor
     * @see #connectUpwardsActor(GameActor)
     */
    protected void connectDownwardsActor(GameActor actor) {
        actor.connectUpwardsActor(this);
    }

    protected void disconnectUpwardsActor(GameActor other) {
        this.removeEventListener(EventType.GAME_EVENT, other);
        other.removeEventListener(EventType.GAME_EVENT, this);
    }

    protected void disconnectDownwardsActor(GameActor actor) {
        actor.disconnectUpwardsActor(this);
    }

    /**
     * Declare the types of gameEvents this actor wants to pass upwards.
     * Must have no types in common with {@link #setReceivesDown(EventType[]) receivesDown}.
     * Use before {@link #connectUpwardsActor(GameActor)} or
     * {@link #connectDownwardsActor(GameActor)}.
     */
    protected void setSendsUp(EventType[] sendsUp) {
        this.sendsUp = new HashSet<EventType>(Arrays.asList(sendsUp));
    }

    /**
     * Declare the types of gameEvents this actor is interested in receiving
     * from upwards. Must have no types in common with {@link #setSendsUp(EventType[]) sendsUp}.
     * Use before {@link #connectUpwardsActor(GameActor)} or
     * {@link #connectDownwardsActor(GameActor)}.
     */
    protected void setReceivesDown(EventType[] receivesDown) {
        this.receivesDown = new HashSet<EventType>(Arrays.asList(receivesDown));
    }


    private void registerListenigActor(GameActor listener, Set<EventType> types) {
        for (EventType type : types) {
            this.addEventListener(type, listener);
        }
    }

    /**
     * Find the intersection between the types of events this actor sends
     * upwards and receives from there. Less than trivial, since
     * EventTypes form a hierarchy and types can be transitive.
     *
     * @return the intersection between the types of events this actor sends
     * upwards and receives from there
     */
    private Set<EventType> upAndDownIntersection() {
        Set<EventType> intersection = new HashSet<EventType>();
        if (sendsUp != null && receivesDown != null) {
            for (EventType upType : sendsUp) {
                for (EventType downType : receivesDown) {
                    if (upType.isType(downType)) {
                        intersection.add(upType);
                    } else if (downType.isType(upType)) {
                        intersection.add(downType);
                    }
                }
            }
        }
        return intersection;
    }

    @Override
    public void receiveEvent(GameEvent event) {
        //Logger.i().log(LogType.GAMEWORLD, this+" "+event);
        dispatchEvent(event);
    }

    @Override
    public void addEventListener(EventType type, IGameEventListener listener) {
        eventDispatcher.addEventListener(type, listener);
    }

    @Override
    public void removeEventListener(EventType type, IGameEventListener listener) {
        eventDispatcher.removeEventListener(type, listener);
    }

    @Override
    public void dispatchEvent(GameEvent event) {
        eventDispatcher.dispatchEvent(event);
    }

}
