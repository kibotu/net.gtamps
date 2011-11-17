package net.gtamps.game.universe;

import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.event.EventManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.*;

import java.util.LinkedList;

public class Universe implements IGameEventListener, IGameEventDispatcher {

    /**
     * generated value
     */
    private static final long serialVersionUID = 1821222727619509975L;

    private transient GameEventDispatcher eventDispatcher = new GameEventDispatcher();

    private final String name;
    private final int width;
    private final int height;

    //private LinkedList<Entity> entityList = new LinkedList<Entity>();
    private final LinkedList<Entity> spawnPoints = new LinkedList<Entity>();
    public final Box2DEngine physics;
    public final EventManager eventManager;
    public final PlayerManager playerManager;
    public final EntityManager entityManager;

    private long revision;


    public Universe(final String name, final int width, final int height, final Box2DEngine physics) {
        Logger.i().log(LogType.GAMEWORLD, "GameWorld was created, size: " + width + "x" + height);
        this.name = name;
        this.physics = physics;
        this.width = width;
        this.height = height;
        eventManager = new EventManager(this);
        entityManager = new EntityManager(this);
        playerManager = new PlayerManager(this, entityManager);

        eventManager.addEventListener(EventType.ACTION_EVENT, playerManager);
        eventManager.addEventListener(EventType.SESSION_EVENT, entityManager);
        eventManager.addEventListener(EventType.SESSION_EVENT, playerManager);
        playerManager.addEventListener(EventType.PLAYER_EVENT, eventManager);
        entityManager.addEventListener(EventType.ENTITY_EVENT, eventManager);

    }

    public String getName() {
        return name;
    }

    public long getRevision() {
        return revision;
    }

    public void updateRevision(final long newRevision) {
        revision = newRevision;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return height;
    }

    public Box2DEngine getPhysics() {
        return physics;
    }

    public void addSpawnPoint(final Entity sp) {
        if (sp == null) {
            throw new IllegalArgumentException("'sp' must not be null");
        }
        spawnPoints.add(sp);
    }

    public Entity getRandomSpawnPoint() {
        return spawnPoints.get((int) (spawnPoints.size() * Math.random()));
    }

    @Override
    public void addEventListener(final EventType type, final IGameEventListener listener) {
        eventDispatcher.addEventListener(type, listener);
    }

    @Override
    public void removeEventListener(final EventType type, final IGameEventListener listener) {
        eventDispatcher.removeEventListener(type, listener);
    }

    @Override
    public void dispatchEvent(final GameEvent event) {
        eventDispatcher.dispatchEvent(event);

    }

    @Override
    public void receiveEvent(final GameEvent event) {
        dispatchEvent(event);

    }

}
