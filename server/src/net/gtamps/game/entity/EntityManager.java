package net.gtamps.game.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

/**
 * A class to manage the game entities. Obviously.
 *
 * @author jan, tom, til
 */
public class EntityManager extends GameEventDispatcher implements IGameEventListener {

	private final Comparator<Entity> reverseRevisionComparator = new Comparator<Entity>() {

		@Override
		public int compare(final Entity o1, final Entity o2) {
			final long revisionDelta = o2.getRevision() - o1.getRevision();
			if (revisionDelta != 0) {
				return (revisionDelta < 0) ? -1 : 1;
			}
			return o2.getUid() - o1.getUid();
		}

	};

	private final Universe universe;
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final SortedSet<Entity> updated = new TreeSet<Entity>(reverseRevisionComparator);

	public EntityManager(final Universe universe) {
		this.universe = universe;
	}

	public EntityManager registerEntity(final Entity e) {
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		entities.put(e.getUid(), e);
		return this;
	}

	public Entity createEntity(final String name, final int pixX, final int pixY, final int rotation) {
		final Entity e = EntityFactory.createEntity(universe, name, pixX, pixY, rotation);
		registerEntity(e);
		return e;
	}

	/**
	 * @deprecated	use {@link #createEntity(String, int, int, int)}
	 */
	@Deprecated
	public Entity createEntityCar(final int pixX, final int pixY, final int rotation) {
		// TODO
		final Entity e = EntityFactory.createEntityCar(universe, pixX, pixY, rotation);
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}

	/**
	 * @deprecated	use {@link #createEntity(String, int, int, int)}
	 */
	@Deprecated
	public Entity createEntityHuman(final int pixX, final int pixY, final int rotation) {
		final Entity e = EntityFactory.createEntityHuman(universe, pixX, pixY, rotation, this);
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}

	/**
	 * @deprecated	workaround: {@link EntityFactory#createSpecialEntityHouse(Universe, net.gtamps.shared.game.level.PhysicalShape)}, then {@link #registerEntity(Entity)}
	 * @see EntityFactory#createSpecialEntityHouse(Universe, net.gtamps.shared.game.level.PhysicalShape)
	 * @see #registerEntity(Entity)
	 */
	@Deprecated
	public Entity createEntityHouse(final int pixX, final int pixY) {
		// TODO
		final Entity e = EntityFactory.createEntityHouse(universe, pixX, pixY);
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}


	public Entity createEntitySpawnPoint(final Universe world2, final int pixX, final int pixY, final Integer rotation) {
		final Entity e = EntityFactory.createEntitySpawnPoint(universe, pixX, pixY, rotation);
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}

	/**
	 * @deprecated	use {@link #createEntity(String, int, int, int)}
	 */
	@Deprecated
	public Entity createEntityBullet(final int pixX, final int pixY, final Integer rotation) {
		//FIXME launch Distance is just some value: should be determined by the entity.
		//it is, just as the position property set in pixels.
		final int launchDistance = 20;
		final Entity e = EntityFactory.createEntityBullet(universe, pixX, pixY, rotation, launchDistance);
		addEventListener(EventType.SESSION_EVENT, e);
		addEventListener(EventType.ENTITY_EVENT, e);
		assert e != null;
		entities.put(e.getUid(), e);
		return e;
	}

	public Entity getEntity(final int uid) {
		Entity e = null;
		if (entities.containsKey(uid)) {
			e = entities.get(uid);
		}
		return e;
	}

	public Iterable<Entity> getEntities() {
		return entities.values();
	}

	public void removeEntity(final int uid) {
		final Entity removed = entities.remove(uid);
		if (removed != null) {
			removeEventListener(EventType.GAME_EVENT, removed);
		}
	}

	public void removeEntity(final Entity e) {
		removeEntity(e.getUid());
	}

	//TODO
	public ArrayList<GameObject> getUpdate(final long baseRevision) {
		final ArrayList<GameObject> update = new ArrayList<GameObject>();
		for (final Entity e : entities.values()) {
			if (!e.isSilent() && (e.getRevision() > baseRevision || e.hasChanged())) {
				if (e.hasChanged()) {
					e.updateRevision(universe.getRevision());
				}
				update.add(e);
			}
		}
		return update;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.ENTITY_EVENT)) {
			final Entity source = entities.get(event.getSourceUid());
			final Entity target = entities.get(event.getTargetUid());
			if (source != null) {
				source.receiveEvent(event);
			}
			if (target != null && target != source) {
				target.receiveEvent(event);
			}
			if (type.isType(EventType.ENTITY_DESTROYED)) {
				removeEntity(target);
			}
		} else {
			dispatchEvent(event);
		}
	}

}
