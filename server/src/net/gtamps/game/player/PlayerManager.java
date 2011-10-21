package net.gtamps.game.player;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.game.entity.EntityManager;
import net.gtamps.game.world.World;
import net.gtamps.server.User;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.game.player.Player;

/**
 * Manages player. Players once created cannot be removed from the game,
 * but possibly deactivated. We'll see.
 *
 * @author jan, tom, til
 *
 */
public class PlayerManager extends GameEventDispatcher implements IGameEventListener {

	public static final Player WORLD_PSEUDOPLAYER = new Player("world");
	
	private final World world;
	private final EntityManager entityManager;
	private final Map<Integer, Player> players = new HashMap<Integer, Player>();
	private final Map<Integer, Player> inactivePlayers = new HashMap<Integer, Player>();
	
	public PlayerManager(final World world, final EntityManager entityManager) {
		this.world = world;
		this.entityManager = entityManager;
		players.put(WORLD_PSEUDOPLAYER.getUid(), WORLD_PSEUDOPLAYER);
	}
	
	
	public Player createPlayer(final User user) {
		if (user == null) {
			throw new IllegalArgumentException("'user' must not be null");
		}
		final String name = user.name;
		final Player player = PlayerFactory.createPlayer(name);
		assert player != null : "PlayerFactory.createPlayer() returned null";
		final int uid = player.getUid();
		players.put(uid, player);
		coupleEventLinks(player);
		dispatchEvent(new GameEvent(EventType.PLAYER_JOINS, player));
		return player;
	}
	
	public boolean hasPlayer(final int uid) {
		return players.containsKey(uid);
	}
	
	public Player getPlayer(final int uid) {
		Player player = null;
		player = players.get(uid);
		if (player == null && inactivePlayers.containsKey(uid)) {
			player = inactivePlayers.get(uid);
			activatePlayer(uid);
		}
		return player;
	}
	
	public void deactivatePlayer(final int uid) {
		final Player player = players.get(uid);
		if (player == null) {
			return;
		}
		players.remove(uid);
		inactivePlayers.put(uid, player);
		decoupleEventLinks(player);
		dispatchEvent(new GameEvent(EventType.PLAYER_LEAVES, player));
	}
	
	public void activatePlayer(final int uid) {
		final Player player = inactivePlayers.get(uid);
		if (player == null) {
			return;
		}
		inactivePlayers.remove(uid);
		players.put(uid, player);
		coupleEventLinks(player);
		dispatchEvent(new GameEvent(EventType.PLAYER_JOINS, player));
	}
	
	
	/**
	 * @param uid
	 * @return	<code>true</code> if an entity was spawned for player
	 */
	public boolean spawnPlayer(final int uid) {
		final Player p = getPlayer(uid);
		assert p != null : "invalid player uid: " + uid;
		//if the player has an entity and his entity is still alive...
		//FIXME
		if (p.getEntity() != null /*&& ((HealthProperty) p.getEntity().getProperty(Type.HEALTH)).isAlive()*/) {
			return false;
		}
		//TODO create proper entity
		// create default entity at default location and add
		//PositionProperty pp = (PositionProperty) world.getRandomSpawnPoint().getProperty(Type.POSITION);
		final Entity spawn = world.getRandomSpawnPoint();
		final Entity e = entityManager.createEntityHuman(spawn.x.value(), spawn.y.value(), spawn.rota.value());
		p.setEntity(e);
		dispatchEvent(new GameEvent(EventType.PLAYER_SPAWNS, p));
		return true;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		if (event == null) {
			// TODO log warning
			// TODO log system :)
			return;
		}
		final EventType type = event.getType();
		if (type.isType(EventType.ACTION_EVENT)) {
			final Player player = getPlayer(event.getSourceUid());
			assert player != null : "event source uid does not match any active player; #players: " + players.size();
			player.receiveEvent(event);
		} else if (type.isType(EventType.PLAYER_EVENT)) {
			dispatchEvent(event);
		}

		// SWITCH WON'T WORK WITH HIERARCHICAL EVENT TYPES
//		switch (event.getType()) {
//		case ACTION_EVENT:
//			Player player = getPlayer(event.getSourceUid());
//			assert player != null : "event source uid does not match any active player; #players: " + players.size();
//			player.receiveEvent(event);
//			break;
//		case PLAYER_EVENT:
//			dispatchEvent(event);
//			break;
//		}
	}

	private void coupleEventLinks(final Player player) {
		player.addEventListener(EventType.PLAYER_EVENT, this);
	}
	
	private void decoupleEventLinks(final Player player) {
		player.removeEventListener(EventType.PLAYER_EVENT, this);
	}

	
}
