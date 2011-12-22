package net.gtamps.shared.game.event;

import net.gtamps.shared.game.GameObject;

/**
 * <p>
 * A gameEvent is something meaningful that happens to a gameObject,
 * or between two gameObjects. The meaning of a particular event and the type of the
 * object(s) involved can be inferred from the {@link EventType event type}.
 * </p><p>
 * <tt>source</tt> and <tt>target</tt> here are meant as a way to differentiate between
 * the roles of the gameObjects involved, *if* there is more than one
 * involved, and *if* a need for such differentiation exists. In a case like that,
 * <tt>source</tt> will denote the active role, while the <tt>target</tt>
 * is passive. In a truly symmetric relationship, both roles
 * should be interchangeable without altering the meaning or interpretation
 * of the event.
 * </p><p>
 * If only one object is involved in an event, <tt>source</tt> and
 * <tt>target</tt> should be the same, since there is
 * no convention which of the two roles will be considered
 * in this case. For convenience, a
 * {@link #GameEvent(EventType, GameObject) constructor} is in place
 * that copies the given source into the target slot.
 * </p>
 *
 * @author jan, tom, til
 */
public class GameEvent extends GameObject {

	/**
	 * signals the beginning of an ongoing event
	 */
	public static final String BEGIN_VALUE = "BEGIN";
	/**
	 * signals that an ongoing event has finished
	 */
	public static final String END_VALUE = "END";
	private static final long serialVersionUID = 4026988656700611249L;

	protected final EventType type;
	protected final GameObject source;
	protected final GameObject target;
	protected final String value;

	/**
	 * @param type   use an unambiguous type here, that is, one without
	 *               {@link EventType#hasSubtypes() subtypes}
	 * @param source
	 * @param target
	 * @param value
	 */
	public GameEvent(final EventType type, final GameObject source, final GameObject target, String value) {
		super(type.name());
		if (source == null) {
			throw new IllegalArgumentException("'source' must not be null");
		}
		if (target == null) {
			throw new IllegalArgumentException("'target' must not be null");
		}
		if (type.hasSubtypes()) {
			throw new IllegalArgumentException("'type' is ambiguous! use an EventType without subtypes.");
		}
		value = value == null ? "" : value;
		this.type = type;
		this.source = source;
		this.target = target;
		this.value = value;
	}

	public GameEvent(final EventType type, final GameObject source, final GameObject target) {
		this(type, source, target, null);
	}

	/**
	 * Creates a new game event originating from source. As per definition,
	 * since no target is being specified, the source will also act as the target.
	 *
	 * @param type
	 * @param source
	 */
	public GameEvent(final EventType type, final GameObject source) {
		this(type, source, source);
	}

	public EventType getType() {
		return this.type;
	}

	/**
	 * @return if this gameEvent has no source, this will return the
	 *         same uid as {@link #getTargetUid()}
	 */
	public int getSourceUid() {
		return this.source.getUid();
	}

	public int getTargetUid() {
		return this.target.getUid();
	}

	public GameObject getSource() {
		return this.source;
	}

	public GameObject getTarget() {
		return this.target;
	}

	public boolean isBegin() {
		return this.value.equals(BEGIN_VALUE);
	}

	public boolean isEnd() {
		return this.value.equals(END_VALUE);
	}

	@Override
	public boolean hasChanged() {
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + (value != null ? value.toString() : "");
	}

}
