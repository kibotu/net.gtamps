package net.gtamps.shared.game.event;

public interface IGameEventDispatcher {
    /**
     * Subscribe an {@link IGameEventListener} as an interested party to all gameEvents
     * of type <tt>type</tt> and its subtypes.
     *
     * @param type
     * @param listener
     */
    public void addEventListener(EventType type, IGameEventListener listener);

    /**
     * Unsubscribe the given {@link IGameEventListener} from all notifications
     * about gameEvents of type <tt>type</tt> and its subtypes.
     *
     * @param type
     * @param listener
     */
    public void removeEventListener(EventType type, IGameEventListener listener);

    public boolean isRegisteredListener(IGameEventListener listener);

    /**
     * Notify all listeners subscribed to the proper type that <tt>event</tt>
     * has happened.
     *
     * @param event
     */
    public void dispatchEvent(GameEvent event);
}
