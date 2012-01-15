package net.gtamps.shared.game.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * GameEvents form a type hierarchy: types can have subtypes, which are for all
 * intends and purposes also of their more general parent type, an "is-a"
 * relationship just like in object-oriented inheritance.
 * </p>
 * <p>
 * <tt>GAME_EVENT</tt> is the most general type and has all other types as its
 * direct or indirect children. Thus it happens that every game event is a
 * <tt>GAME_EVENT</tt>.
 * </p>
 * <p>
 * This hierarchical structure of eventTypes means that <b><code>switch</code>
 * does NOT work well with EventTypes</b>, not reliably anyway, unless you
 * know what you're doing - knowledge that does probably not extend to the future...
 * </p>
 *
 * @author jan, tom, til
 * @see GameEvent
 */
public enum EventType {
	GAME_EVENT(null),

	ACTION_EVENT(GAME_EVENT), //

	ACTION_ACCELERATE(ACTION_EVENT), //
	ACTION_DECELERATE(ACTION_EVENT), //
	ACTION_TURNRIGHT(ACTION_EVENT), //
	ACTION_TURNLEFT(ACTION_EVENT), //
	ACTION_HANDBRAKE(ACTION_EVENT), //
	ACTION_NOISE(ACTION_EVENT), //
	ACTION_SHOOT(ACTION_EVENT), //
	ACTION_SWITCH_GUN_NEXT(ACTION_EVENT), //
	ACTION_SWITCH_GUN_PREV(ACTION_EVENT), //
	ACTION_ENTEREXIT(ACTION_EVENT), //
	ACTION_SUICIDE(ACTION_EVENT),

	SESSION_EVENT(GAME_EVENT), //
	SESSION_STARTS(SESSION_EVENT), //
	SESSION_ENDS(SESSION_EVENT), //
	SESSION_UPDATE(SESSION_EVENT), //

	PLAYER_EVENT(GAME_EVENT), //
	PLAYER_SPAWNS(PLAYER_EVENT), //
	PLAYER_JOINS(PLAYER_EVENT), //
	PLAYER_LEAVES(PLAYER_EVENT), //
	PLAYER_WINS(PLAYER_EVENT), //
	PLAYER_KILLED(PLAYER_EVENT), //
	PLAYER_SCORES(PLAYER_EVENT),//
	PLAYER_POWERUP(PLAYER_EVENT),//
	PLAYER_ENTERSCAR(PLAYER_EVENT),//
	PLAYER_EXITSCAR(PLAYER_EVENT), //
	PLAYER_LOGIN(PLAYER_EVENT),//

	ENTITY_EVENT(GAME_EVENT), //
	ENTITY_CREATE(ENTITY_EVENT),//
	ENTITY_UPDATE(ENTITY_EVENT), //
	ENTITY_REMOVE(ENTITY_EVENT),//
	ENTITY_DAMAGE(ENTITY_EVENT), //
	ENTITY_DESTROYED(ENTITY_EVENT),//
	ENTITY_ACTIVATE(ENTITY_EVENT), //
	ENTITY_DEACTIVATE(ENTITY_EVENT),//
	ENTITY_COLLIDE(ENTITY_EVENT), //
	ENTITY_BULLET_HIT(ENTITY_EVENT),//
	ENTITY_NEW_PLAYER(ENTITY_EVENT),//

	ENTITY_SENSE(ENTITY_EVENT),//
	ENTITY_SENSE_SPAWN(ENTITY_SENSE),//
	ENTITY_SENSE_DOOR(ENTITY_SENSE),//
	ENTITY_SENSE_EXPLOSION(ENTITY_SENSE);

	private static final Map<Integer, EventType> idLookupTable = new HashMap<Integer, EventType>(100, 0.75f);

	private final int id;
	private Set<EventType> children = null;
	private EventType parent = null;

	private EventType(final EventType parent) {
		this.id = getOrCreateLookupId(this);
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public static EventType fromId(final int id) throws IllegalArgumentException {
		if (!hasLookupId(id)) {
			throw new IllegalArgumentException("there is no EventType with id " + id);
		}
		return lookup(id);
	}

	public int getId() {
		return id;
	}

	public boolean hasSubtypes() {
		return children != null && children.size() > 0;
	}

	public EventType[] getSubtypes() {
		final int arraysize = (children == null) ? 0 : children.size();
		return children.toArray(new EventType[arraysize]);
	}

	/**
	 * @param type
	 * @return <tt>true</tt> if this type is <tt>type</tt> itself or a subtype
	 *         of <tt>type</tt>
	 */
	public boolean isType(final EventType type) {
		return (type == this) || (parent != null && parent.isType(type));
	}

	private void addChild(final EventType child) {
		if (children == null) {
			children = new HashSet<EventType>();
		}
		children.add(child);
	}

	private static int getOrCreateLookupId(final EventType entry) {
		assert entry != null;
		if (idLookupTable.containsValue(entry)) {
			return entry.id;
		}
		int id = entry.name().hashCode();
		while (hasLookupId(id)) {
			id++;
		}
		idLookupTable.put(id, entry);
		return id;
	}

	private static EventType lookup(final int id) {
		return idLookupTable.get(id);
	}

	private static boolean hasLookupId(final int id) {
		return idLookupTable.get(id) != null;
	}
}
