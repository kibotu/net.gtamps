package net.gtamps.shared.game.score;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.Utils.predicate.Predicate;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.GameEventDispatcher;
import net.gtamps.shared.game.event.IGameEventListener;

public class ScoreBoard implements IGameEventListener {

	private final Map<Score.ScoreType, Score> scores = new HashMap<Score.ScoreType, Score>(Score.ScoreType.values().length, 1f);
	private final GameEventDispatcher internalDispatcher = new GameEventDispatcher();
	private final Predicate<GameEvent> filter;

	public ScoreBoard() {
		this(null);
	}

	public ScoreBoard(final Predicate<GameEvent> filter) {
		this.filter = filter;
	}

	public void addScoresForAllTypes() {
		final Score.ScoreType[] types = Score.ScoreType.values();
		for (int i = 0; i < types.length; i++) {
			final Score score = new Score(types[i]);
			score.setFilter(filter);
			addScore(score);
		}
	}

	/**
	 * return {@code true} if this scoreBoard is currently keeping a score of
	 * the given <tt>type</tt>
	 * 
	 * @param type	not {@code null}
	 * 
	 * @return	{@code true} if this scoreBoard is currently keeping a score of
	 * 			the given <tt>type</tt> 
	 */
	public boolean isTracking(final Score.ScoreType type) {
		return scores.containsKey(type);
	}

	/**
	 * @param type
	 * @return
	 * @throws IllegalArgumentException	if <tt>type</tt> is {@code null} or
	 * 									there is no score to return
	 */
	public Score getScore(final Score.ScoreType type) throws IllegalArgumentException {
		Validate.notNull(type);
		Validate.isTrue(isTracking(type), "score type is currently not being tracked: " + type.toString());
		return scores.get(type);
	}

	/**
	 * Add a score to this board. If this board is already keeping a store of
	 * the same type, the score added will be {@link Score#mergeScore(Score) merged}
	 * into the existing one. ( = The existing score will be modified.)
	 * 
	 * @param score	not {@code null}
	 * @return	the resulting score, as kept by this board
	 */
	public Score addScore(final Score score) {
		Validate.notNull(score);
		final Score existingScore = scores.get(score.getType());
		final Score mergedScore = existingScore == null ? score : existingScore.mergeScore(score);
		registerScore(mergedScore);
		return mergedScore;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		internalDispatcher.dispatchEvent(event);
	}

	private void registerScore(final Score score) {
		final Score.ScoreType  type = score.getType();
		if (!isTracking(type)) {
			internalDispatcher.addEventListener(score.getEventType(), score);
		}
		scores.put(type, score);
	}

	public Collection<? extends Score> getAllScores() {
		return Collections.unmodifiableCollection(scores.values());
	}

}
