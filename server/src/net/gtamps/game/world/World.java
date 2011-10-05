package net.gtamps.game.world;

import net.gtamps.XmlElements;
import net.gtamps.game.GameActor;
import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.entity.Entity;

import java.util.LinkedList;

import org.jdom.Attribute;
import org.jdom.Element;

import net.gtamps.game.physics.Box2DEngine;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

public class World extends GameActor {

	private final int width;
	private final int height;
	
	//private LinkedList<Entity> entityList = new LinkedList<Entity>();
	private final LinkedList<Entity> spawnPoints = new LinkedList<Entity>();
	private final Box2DEngine physics;

	public World(String name, int width, int height, Box2DEngine physics) {
		super(name, XmlElements.MAPDATA.tagName());
		Logger.i().log(LogType.GAMEWORLD, "GameWorld was created, size: "+width+"x"+height);
		this.physics = physics;
		this.width = width;
		this.height = height;
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
	
	@Override
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(baseRevision, keeper);
		e.setAttribute(new Attribute("width",width+""));
		e.setAttribute(new Attribute("height",height+""));
		return e;
	}
	
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
