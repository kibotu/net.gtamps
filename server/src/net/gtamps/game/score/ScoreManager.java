package net.gtamps.game.score;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.game.score.Score;

public class ScoreManager implements IGameEventListener {

	Universe universe;

	private final Map<Integer, Score> fragScores = new HashMap<Integer, Score>();

	public ScoreManager(final Universe universe) {
		Validate.notNull(universe);
		this.universe = universe;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.PLAYER_EVENT)) {
			dispatchToPlayerScoreBoards(event);
		}
	}

	public Iterable<Score> getScores() {
		return Collections.unmodifiableCollection(fragScores.values());
	}

	private void dispatchToPlayerScoreBoards(final GameEvent event) {
		final int sourceUid = event.getSourceUid();
		final int targetUid = event.getTargetUid();
		if (universe.isPlayer(sourceUid)) {
			getPlayerFragScore(sourceUid).receiveEvent(event);
		}
		if (universe.isPlayer(sourceUid)) {
			getPlayerFragScore(targetUid).receiveEvent(event);
		}
	}

	private Score getPlayerFragScore(final int uid) {
		Score board = fragScores.get(uid);
		if (board == null) {
			board = createPlayerFragScore(uid);
		}
		return board;
	}

	private Score createPlayerFragScore(final int uid) throws IllegalArgumentException {
		if (GameObject.isValidUid(uid)) {
			throw new IllegalArgumentException("invalid uid: " + uid);
		}
		if (fragScores.containsKey(uid)) {
			throw new IllegalArgumentException("already created for player " + uid);
		}
		final Score score = new Score();
		score.setType(Score.ScoreType.FRAGS);
		score.setFilter(Score.getSourceUidFilter(uid));
		universe.getPlayer(uid).setFragScoreUid(score.getUid());
		return score;
	}

}
