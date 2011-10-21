package net.gtamps.game.world;

import net.gtamps.XmlElements;
import net.gtamps.game.RevisionKeeper;

import java.util.LinkedList;

import org.jdom.Attribute;
import org.jdom.Element;

import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.event.EventManager;
import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;

public class World extends GameActor {

	/**
	 * generated value
	 */
	private static final long serialVersionUID = 1821222727619509975L;
	private final int width;
	private final int height;
	
	//private LinkedList<Entity> entityList = new LinkedList<Entity>();
	private final LinkedList<Entity> spawnPoints = new LinkedList<Entity>();
	public final Box2DEngine physics;
	public final EventManager eventManager;
	public final PlayerManager playerManager;
	public final EntityManager entityManager;

	public World(String name, int width, int height, Box2DEngine physics) {
		super(name);
		Logger.i().log(LogType.GAMEWORLD, "GameWorld was created, size: "+width+"x"+height);
		this.physics = physics;
		this.width = width;
		this.height = height;
		this.eventManager = new EventManager();
		this.entityManager = new EntityManager(this);
		this.playerManager = new PlayerManager(this, entityManager);

		eventManager.addEventListener(EventType.ACTION_EVENT, playerManager);
		eventManager.addEventListener(EventType.SESSION_EVENT, entityManager);
		eventManager.addEventListener(EventType.SESSION_EVENT, playerManager);
		playerManager.addEventListener(EventType.PLAYER_EVENT, eventManager);
		entityManager.addEventListener(EventType.ENTITY_EVENT, eventManager);
		
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeigth() {
		return height;
	}
	
	public Box2DEngine getPhysics() {
		return this.physics;
	}

	//TODO
//	@Override
//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(baseRevision, keeper);
//		e.setAttribute(new Attribute("width",width+""));
//		e.setAttribute(new Attribute("height",height+""));
//		return e;
//	}
	
	public void addSpawnPoint(Entity sp) {
		if (sp == null) {
			throw new IllegalArgumentException("'sp' must not be null");
		}
		spawnPoints.add(sp);
	}
	
	public Entity getRandomSpawnPoint(){
		return spawnPoints.get((int) (spawnPoints.size()*Math.random()));
	}
	
//	public void addEntity(Entity e){
//		this.entityList.add(e);
//	}
	
}
