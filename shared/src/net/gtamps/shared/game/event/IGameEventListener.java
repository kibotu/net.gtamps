package net.gtamps.shared.game.event;

import java.util.EventListener;

public interface IGameEventListener extends EventListener {
    public void receiveEvent(GameEvent event);
}
