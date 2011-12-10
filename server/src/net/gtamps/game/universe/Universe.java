package net.gtamps.game.universe;

import java.util.LinkedList;

import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.event.EventManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

public class Universe implements IGameEventListener, IGameEventDispatcher {

    private static final long serialVersionUID = 1821222727619509975L;

    private transient GameEventDispatcher eventRoot = new GameEventDispatcher();

    private final String name;
    private final int width;
    private final int height;

    //private LinkedList<Entity> entityList = new LinkedList<Entity>();
    private final LinkedList<Entity> spawnPoints = new LinkedList<Entity>();
    public Box2DEngine physics;
    public final EventManager eventManager;
    public final PlayerManager playerManager;
    public final EntityManager entityManager;

    private long revision;

    // TODO universe builder!

    public Universe(final String name, final int width, final int height) {
        GUILogger.i().log(LogType.GAMEWORLD, "GameWorld was created, size: " + width + "x" + height);
        this.name = name;
        this.width = width;
        this.height = height;
        eventManager = new EventManager(this);
        entityManager = new EntityManager(this);
        playerManager = new PlayerManager(this);

        eventManager.addEventListener(EventType.ACTION_EVENT, playerManager);
        eventManager.addEventListener(EventType.SESSION_EVENT, entityManager);
        eventManager.addEventListener(EventType.SESSION_EVENT, playerManager);
        playerManager.addEventListener(EventType.PLAYER_EVENT, eventManager);
        entityManager.addEventListener(EventType.ENTITY_EVENT, eventManager);

    }
    public void setPhysics (final Box2DEngine physics)  {
    	if (physics == null) {
			throw new IllegalArgumentException("'physics' must not be 'null'");
		}
    	this.physics = physics;
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
    
    public IGameEventDispatcher getEventRoot() {
    	return eventRoot;
    }

    public void addSpawnPoint(final Entity sp) {
        if (sp == null) {
            throw new IllegalArgumentException("'sp' must not be null");
        }
        spawnPoints.add(sp);
    }

    public Entity getRandomSpawnPoint() {
    	if (spawnPoints.size() > 0 ) {
    		return spawnPoints.get((int) (spawnPoints.size() * Math.random()));
    	}
    	return null;
    }

    @Override
    public void addEventListener(final EventType type, final IGameEventListener listener) {
        eventRoot.addEventListener(type, listener);
    }

    @Override
    public void removeEventListener(final EventType type, final IGameEventListener listener) {
        eventRoot.removeEventListener(type, listener);
    }

    @Override
    public void dispatchEvent(final GameEvent event) {
        eventRoot.dispatchEvent(event);

    }

    @Override
    public boolean isRegisteredListener(final IGameEventListener listener) {
    	return eventRoot.isRegisteredListener(listener);
    }

    @Override
    public void receiveEvent(final GameEvent event) {
        dispatchEvent(event);

    }

}
