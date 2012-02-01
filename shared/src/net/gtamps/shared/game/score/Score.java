package net.gtamps.shared.game.score;

import net.gtamps.shared.Utils.predicate.Predicate;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.serializer.communication.StringConstants;

/**
 * A score tracks how often a certain game event was triggered,
 * given additional constraints
 *
 * @author Tom Wallroth, Jan Rabe, Til Boerner
 *
 */
public class Score extends GameObject implements IGameEventListener, Cloneable {

	public static final Predicate<GameEvent> getSourceUidFilter(final int uid) {
		return new Predicate<GameEvent>() {

			@Override
			public boolean isTrueFor(final GameEvent x) {
				return x.getSourceUid() == uid;
			}

		};
	}

	public static final Predicate<GameEvent> getTargetUidFilter(final int uid) {
		return new Predicate<GameEvent>() {

			@Override
			public boolean isTrueFor(final GameEvent x) {
				return x.getTargetUid() == uid;
			}

		};
	}

	public static enum ScoreType {
		DUMMY(null, null),
		FRAGS(EventType.PLAYER_KILLED, null);

		private EventType triggerEventType;
		private Predicate<GameEvent> filter;

		private ScoreType(final EventType eventType, final Predicate<GameEvent> filter) {
			this.triggerEventType = eventType;
			this.filter = filter;
		}

		public EventType getTriggerEventType() {
			return triggerEventType;
		}

		public Predicate<GameEvent> getEventFilter() {
			return filter;
		}

		public boolean isTriggeredBy(final GameEvent event) {
			if (this == DUMMY) {
				return false;
			}
			if (event.getType() != this.triggerEventType) {
				return false;
			}
			if (filter != null && !filter.isTrueFor(event)) {
				return false;
			}
			return true;
		}
	}

	private static final long serialVersionUID = -913792277539222522L;

	private final IProperty<Integer> count = useProperty(StringConstants.PROPERTY_SCORE_COUNT, 0);
	private final IProperty<Long> lastModTime = useProperty(StringConstants.PROPERTY_SCORE_MOD_TIME, 0L);

	/** NEVER use directly, always invoke getter and setter! */
	private transient ScoreType type;

	private Predicate<GameEvent> filter;

	public Score() {
		this(ScoreType.DUMMY);
	}

	Score(final ScoreType type) {
		super();
		setType(type);
	}

	private Score(final Score other) {
		super(other);
	}

	public int getCount() {
		return count.value();
	}

	public long getLastModificationTimestamp() {
		return lastModTime.value();
	}

	public boolean isMoreRecentThan(final Score otherScore) {
		return this.getLastModificationTimestamp() > otherScore.getLastModificationTimestamp();
	}

	public ScoreType getType() {
		if (type == null) {
			type = ScoreType.valueOf(this.getName());
		}
		return type;
	}

	public EventType getEventType() {
		final ScoreType type = this.getType();
		if (type == ScoreType.DUMMY) {
			throw new IllegalArgumentException("dummy score doesn't have an event type");
		}
		return type.getTriggerEventType();
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		Validate.notNull(event);
		if (getType().isTriggeredBy(event) && filter.isTrueFor(event)) {
			increaseCountBy(1);
		}
	}

	@Override
	public String toString() {
		return super.toString() + " " + getCount();
	}

	public Score setType(final ScoreType type) {
		setName(type.name());
		this.type = null;
		return this;
	}

	public void setFilter(final Predicate<GameEvent> filter) {
		this.filter = filter;
	}

	@Override
	public Score clone() {
		return new Score(this);
	}

	Score increaseCountBy(final int value) {
		if (value < 0) {
			throw new IllegalArgumentException("'value' must be >= 0");
		}
		if (value == 0) {
			return this;
		}
		setCount(count.value() + value);
		return this;
	}

	Score setCount(final int value) {
		validateCount(value);
		if (value != count.value()) { 
			count.set(value);
			updateModificationTimestamp();
		}
		return this;
	}

	Score setModificationTimeStamp(final long value) {
		lastModTime.set(value);
		return this;
	}

	/**
	 * Merge the values of another score into this one. Values concerning
	 * the last modification will be taken from the more recent object.
	 * 
	 * @param other	a score object with the same type as this, or {@code null}
	 * @return	this score
	 * @throws IllegalArgumentException	if the other score has a different type 
	 */
	Score mergeScore(final Score other) throws IllegalArgumentException {
		if (other == null || other == this) {
			return this;
		} else if (this.getType() != other.getType()) {
			throw new IllegalArgumentException("score argument has wrong type: " + other.getType().toString() + ", expected " + this.getType().toString());
		}
		final Score mostRecentScore = this.isMoreRecentThan(other) ? this : other;
		final long lastModTime = mostRecentScore.getLastModificationTimestamp();	// save modTime, since this.increaseCount() will change it  
		increaseCountBy(other.getCount());
		setModificationTimeStamp(lastModTime);
		return this;
	}


	private void updateModificationTimestamp() {
		final long now = System.currentTimeMillis();
		setModificationTimeStamp(now);
	}

	private void validateCount(final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("'count' must be >= 0");
		}
	}

}
