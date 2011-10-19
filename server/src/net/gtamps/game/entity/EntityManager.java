package net.gtamps.game.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.gtamps.game.world.World;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

/**
 * A class to manage the game entities. Obviously.
 *
 * @author jan, tom, til
 *
 */
public class EntityManager extends GameEventDispatcher implements IGameEventListener {
	
	private final Comparator<Entity> reverseRevisionComparator = new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			long revisionDelta = o2.getRevision() - o1.getRevision();
			if (revisionDelta != 0) {
				return (revisionDelta < 0) ? -1 : 1;
			}
			return o2.getUid() - o1.getUid(); 
		}
		
	};
	
	private final World world;
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final SortedSet<Entity> updated = new TreeSet<Entity>(reverseRevisionComparator);
	
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

	//TODO
	public ArrayList<Entity> getUpdate(long baseRevision) {
		ArrayList<Entity> update = new ArrayList<Entity>();
//		for (Entity entity: entities.values()) {
//			Element xml = entity.toXMLElement(baseRevision, keeper);
//			if (xml != null) {
//				update.add(xml);
//			}
//		}
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
