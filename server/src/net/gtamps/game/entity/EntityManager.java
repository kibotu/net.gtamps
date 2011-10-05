package net.gtamps.game.entity;

import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.event.EventType;
import net.gtamps.game.event.GameEvent;
import net.gtamps.game.event.GameEventDispatcher;
import net.gtamps.game.event.IGameEventListener;
import net.gtamps.game.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

/**
 * A class to manage the game entities. Obviously.
 *
 * @author jan, tom, til
 *
 */
public class EntityManager extends GameEventDispatcher implements IGameEventListener {
	
	
	private final World world;
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	
	public EntityManager(World world) {
		this.world = world;
	}
	
	public Entity createEntityCar(int pixX, int pixY, int rotation) {
		// TODO
		Entity e = EntityFactory.createEntityCar(world.getPhysics().getWorld(), pixX, pixY, rotation);
		this.addEventListener(EventType.SESSION_EVENT, e);
		e.addEventListener(EventType.ENTITY_EVENT, this);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}
	
	public Entity createEntityHuman(int pixX, int pixY, int rotation) {
		Entity e = EntityFactory.createEntityHuman(world.getPhysics().getWorld(), pixX, pixY, rotation, this);
		this.addEventListener(EventType.SESSION_EVENT, e);
		e.addEventListener(EventType.ENTITY_EVENT, this);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}
	
	public Entity createEntityHouse(int pixX, int pixY) {
		// TODO
		Entity e = EntityFactory.createEntityHouse(world.getPhysics().getWorld(), pixX, pixY);
		this.addEventListener(EventType.SESSION_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}
	
	
	public Entity createEntitySpawnPoint(World world2, int pixX, int pixY, Integer rotation) {
		Entity e = EntityFactory.createEntitySpawnPoint(world.getPhysics().getWorld(), pixX, pixY, rotation);
		this.addEventListener(EventType.SESSION_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}
	
	public Entity createEntityBullet(int pixX, int pixY, Integer rotation) {
		//FIXME launch Distance is just some value: should be determined by the entity.
		//it is, just as the position property set in pixels.
		int launchDistance = 20;
		Entity e = EntityFactory.createEntityBullet(world.getPhysics().getWorld(), pixX, pixY, rotation, launchDistance);
		this.addEventListener(EventType.SESSION_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}
	
	public Entity getEntity(int uid) {
		Entity e = null;
		if (entities.containsKey(uid)) {
			e = entities.get(uid);
		}
		return e;
	}
	
	public Iterable<Entity> getEntities() {
		return this.entities.values();
	}
	
	public void removeEntity(int uid) {
		if (entities.containsKey(uid)) {
			entities.remove(uid);
		}

	}
	
	public List<Element> getUpdate(long baseRevision, RevisionKeeper keeper) {
		List<Element> update = new LinkedList<Element>();
		for (Entity entity: entities.values()) {
			Element xml = entity.toXMLElement(baseRevision, keeper);
			if (xml != null) {
				update.add(xml);
			}
		}
		return update;
	}


	@Override
	public void receiveEvent(GameEvent event) {
		// TODO Auto-generated method stub
//		EventType type = event.getType();
//		if (type.isType(EventType.SESSION_UPDATE)) {
			dispatchEvent(event);
//		}
	}
	
}
