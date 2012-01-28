package net.gtamps.game.score;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.game.score.Score;
import net.gtamps.shared.game.score.ScoreBoard;

public class ScoreManager implements IGameEventListener {

	Universe universe;

	private final Map<Integer, ScoreBoard> scoreBoards = new HashMap<Integer, ScoreBoard>();

	public ScoreManager(final Universe universe) {
		Validate.notNull(universe);
		this.universe = universe;
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		dispatchToPlayerScoreBoards(event);
	}

	public Iterable<Score> getScores() {
		final Collection<Score> scores = new LinkedList<Score>();
		for (final ScoreBoard board : scoreBoards.values()) {
			scores.addAll(board.getAllScores());
		}
		return scores;
	}

	private void dispatchToPlayerScoreBoards(final GameEvent event) {
		final int sourceUid = event.getSourceUid();
		final int targetUid = event.getTargetUid();
		if (universe.isPlayer(sourceUid)) {
			getPlayerScoreBoard(sourceUid).receiveEvent(event);
		}
		if (targetUid != sourceUid && universe.isPlayer(sourceUid)) {
			getPlayerScoreBoard(targetUid).receiveEvent(event);
		}
	}

	private ScoreBoard getPlayerScoreBoard(final int uid) {
		ScoreBoard board = scoreBoards.get(uid);
		if (board == null) {
			board = createScoreBoardForPlayer(uid);
		}
		return board;
	}

	private ScoreBoard createScoreBoardForPlayer(final int uid) throws IllegalArgumentException {
		if (scoreBoards.containsKey(uid)) {
			throw new IllegalArgumentException("already created for player " + uid);
		}
		final ScoreBoard board = new ScoreBoard();
		board.addScore(createPlayerFragScore(uid));
		scoreBoards.put(uid, board);
		return board;
	}

	private Score createPlayerFragScore(final int uid) throws IllegalArgumentException {
		final Score score = new Score();
		score.setType(Score.ScoreType.FRAGS);
		score.setFilter(Score.getSourceUidFilter(uid));
		universe.getPlayer(uid).setFragScoreUid(score.getUid());
		return score;
	}

}
