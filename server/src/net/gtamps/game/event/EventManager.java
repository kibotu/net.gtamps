package net.gtamps.game.event;

import net.gtamps.game.world.World;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventManager extends GameEventDispatcher implements IGameEventListener {

    public static final long EVENT_TIMEOUT_MILLIS = 30000000;

    @NotNull
    private final World world;
    private final ConcurrentMap<GameEvent, Object> archive = new ConcurrentHashMap<GameEvent, Object>();

    public EventManager(final World world) {

        this.world = world;

        // all received events will be communicated via getUpdate()
        addEventListener(EventType.GAME_EVENT, this);
        removeEventListener(EventType.SESSION_UPDATE, this);
        removeEventListener(EventType.ACTION_ACCELERATE, this);
        removeEventListener(EventType.ACTION_DECELERATE, this);
        removeEventListener(EventType.ACTION_TURNLEFT, this);
        removeEventListener(EventType.ACTION_TURNRIGHT, this);
    }

    @Override
    public void receiveEvent(final GameEvent event) {
        add(event);
    }

    public ArrayList<GameObject> getUpdate(final long baseRevision) {
        final ArrayList<GameObject> updates = new ArrayList<GameObject>(archive.size());
        final long currentRevision = world.getRevision();
        for (final GameEvent e : archive.keySet()) {
            final long eventRevision = e.getRevision();
            if (eventRevision + EVENT_TIMEOUT_MILLIS < currentRevision) {
                archive.remove(e);
                continue;
            }
            if (eventRevision > baseRevision || e.hasChanged()) {
                updates.add(e);
            }
        }
        return updates;
    }

    private void add(final GameEvent event) {
        event.updateRevision(world.getRevision());
        archive.put(event, event);
//		super.setChanged();
    }

}
