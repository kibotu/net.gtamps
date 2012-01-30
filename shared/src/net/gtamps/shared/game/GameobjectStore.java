package net.gtamps.shared.game;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.game.score.Score;


/**
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public class GameobjectStore {

	Map<Class, GameObjectCache> caches = new HashMap<Class, GameObjectCache>();
	Map<Integer, GameObject> active = new HashMap<Integer, GameObject>();

	public GameobjectStore() {
		initCache(Entity.class);
		initCache(GameEvent.class);
		initCache(Player.class);
		initCache(Score.class);
	}

	private <T extends GameObject> void initCache(final Class<T> type) {
		caches.put(type, new GameObjectCache<T>(type));
	}

	public void reclaim(final int uid) {
		final GameObject target = active.get(uid);
		if (target != null) {
			reclaim(target, (Class<GameObject>) target.getClass());
		}
	}

	public void reclaim(final Entity e) {
		reclaim(e, Entity.class);
	}

	public void reclaim(final GameEvent e) {
		reclaim(e, GameEvent.class);
	}

	public void reclaim(final Player p) {
		reclaim(p, Player.class);
	}

	public Entity getEntity(final int uid) {
		return getActiveOrCached(uid, Entity.class);
	}

	public GameEvent getGameEvent(final int uid) {
		return getActiveOrCached(uid, GameEvent.class);
	}

	public Player getPlayer(final int uid) {
		return getActiveOrCached(uid, Player.class);
	}

	public Score getScore(final int uid) {
		return getActiveOrCached(uid, Score.class);
	}

	private <T extends GameObject> void reclaim(final T obj, final Class<T> type) {
		active.remove(obj.getUid());
		@SuppressWarnings("unchecked")
		final GameObjectCache<T> cache =  caches.get(type);
		cache.registerElement(obj);
	}

	private <T extends GameObject> T getActiveOrCached(final int uid, final Class<T> type) {
		T obj = type.cast(active.get(uid));
		if (obj == null) {
			@SuppressWarnings("unchecked")
			final GameObjectCache<T> cache = caches.get(type);
			obj = cache.getOrCreate(uid);
		}
		return obj;
	}

}
