package net.gtamps.game.score;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.game.universe.Universe;
import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventListener;
import net.gtamps.shared.game.score.Score;
import net.gtamps.shared.game.score.ScoreBoard;

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

	private void dispatchToPlayerScoreBoards(final GameEvent event) {
		final int sourceUid = event.getSourceUid();
		final int targetUid = event.getTargetUid();
		if (universe.isPlayer(sourceUid)) {
			getPlayerScoreBoard(sourceUid).receiveEvent(event);
		}
		if (universe.isPlayer(sourceUid)) {
			getPlayerScoreBoard(targetUid).receiveEvent(event);
		}
	}

	private ScoreBoard getPlayerScoreBoard(final int uid) {
		ScoreBoard board = fragScores.get(uid);
		if (board == null) {
			board = createPlayerScoreBoard(uid);
		}
		return board;
	}

	private ScoreBoard createPlayerScoreBoard(final int uid) throws IllegalArgumentException {
		if (GameObject.isValidUid(uid)) {
			throw new IllegalArgumentException("invalid uid: " + uid);
		}
		if (fragScores.containsKey(uid)) {
			throw new IllegalArgumentException("board already createt for player " + uid);
		}
		final ScoreBoard board = new ScoreBoard();
		board.addScoresForAllTypes();
		return board;
	}

}
