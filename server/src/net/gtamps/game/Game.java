package net.gtamps.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.GTAMultiplayerServer;
import net.gtamps.game.player.PlayerManagerFacade;
import net.gtamps.game.universe.Universe;
import net.gtamps.game.universe.UniverseFactory;
import net.gtamps.server.SessionManager;
import net.gtamps.server.User;
import net.gtamps.server.gui.DebugGameBridge;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.Config;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.NullGameObject;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.level.Tile;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.game.score.Score;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.StringConstants;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.ListNode;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.SendableDataConverter;
import net.gtamps.shared.serializer.communication.data.Value;


/**
 * the game, as it happens on the server.
 *
 * @author jan, tom, til
 */
public class Game implements IGame, Runnable {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final long THREAD_UPDATE_SLEEP_TIME = 20;
	private static final int PHYSICS_ITERATIONS = 20;

	private static volatile int instanceCounter = 0;

	private final int id;
	private final Thread thread;
	private final BlockingQueue<NewSendable> requestQueue = new LinkedBlockingQueue<NewSendable>();
	private final BlockingQueue<NewSendable> commandQueue = new LinkedBlockingQueue<NewSendable>();
	private final BlockingQueue<NewSendable> responseQueue = new LinkedBlockingQueue<NewSendable>();

	private volatile boolean run;
	private volatile boolean isActive;

	private final Universe universe;
	private final PlayerManagerFacade playerStorage;
	private final TimeKeeper gameTime;
	private final SendableProvider sendableProvider = new SendableProvider(new SendableCacheFactory());

	public Game(final String mapPath) {
		id = ++Game.instanceCounter;
		final String name = "Game " + id;
		thread = new Thread(this, name);
		//        universe = UniverseFactory.loadMap(mapPath);
		universe = UniverseFactory.loadWorldFromLevel(mapPath);
		if (universe != null) {
			GUILogger.i().log(LogType.GAMEWORLD, "Starting new Game: " + universe.getName());
			run = true;
			playerStorage = new PlayerManagerFacade(universe.playerManager);
			gameTime = new TimeKeeper();

			start();
		} else {
			GUILogger.i().log(LogType.GAMEWORLD, "Game not loaded");
			run = false;
			playerStorage = null;
			gameTime = null;
		}
	}

	public Game() {
		this(Config.TEST_LEVEL_PATH);
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return universe.getName();
	}

	@Override
	public void start() {
		thread.start();
	}

	@Override
	public void hardstop() {
		run = false;
		thread.interrupt();
	}

	@Override
	public void run() {
		isActive = true;

		if (GTAMultiplayerServer.DEBUG) {
			DebugGameBridge.instance.setWorld(universe.physics.getWorld());
		}


		universe.dispatchEvent(new GameEvent(EventType.SESSION_STARTS, NullGameObject.DUMMY));
		while (run) {
			gameTime.startCycle();
			doCycle();
			gameTime.endCycle();
			sleepIfCycleTimeRemaining();
		}
		universe.dispatchEvent(new GameEvent(EventType.SESSION_ENDS, NullGameObject.DUMMY));

		if (GTAMultiplayerServer.DEBUG) {
			DebugGameBridge.instance.setWorld(null);
		}

		isActive = false;
	}

	private void doCycle() {
		universe.updateRevision(gameTime.getTotalDurationMillis());
		universe.physics.step(gameTime.getLastCycleDurationSeconds(), PHYSICS_ITERATIONS);
		//TODO
		universe.dispatchEvent(new GameEvent(EventType.SESSION_UPDATE, NullGameObject.DUMMY));

		//for fps debugging
		//		lastUpdate += timeElapsedInSeceonds;
		//		updates++;
		//		if(lastUpdate>5f){
		//			Logger.i().log(LogType.PHYSICS, "Physics fps: "+((updates/lastUpdate)));
		//			lastUpdate = 0f;
		//			updates = 0;
		//		}

		universe.processEvents();
		processCommandQueue();
		processRequestQueue();
	}

	private void sleepIfCycleTimeRemaining() {
		final long millisRemaining = THREAD_UPDATE_SLEEP_TIME - gameTime.getLastActiveDurationMillis();
		try {
			if (millisRemaining > 0) {
				Thread.sleep(millisRemaining);
			}
		} catch (final InterruptedException e) {
			// reset interrupted status?
			//Thread.currentThread().interrupt();
		}
	}


	@Override
	public boolean isActive() {
		return isActive;
	}


	@Override
	public void handleSendable(final NewSendable sendable) {
		if (sendable == null) {
			throw new IllegalArgumentException("'r' must not be null");
		}
		switch (sendable.type) {
		case ACTION_ACCELERATE:
		case ACTION_DECELERATE:
		case ACTION_ENTEREXIT:
		case ACTION_LEFT:
		case ACTION_RIGHT:
		case ACTION_SHOOT:
		case ACTION_SUICIDE:
			commandQueue.add(sendable);
			break;
		case GETMAPDATA:
		case GETPLAYER:
		case GETUPDATE:
		case GETTILEMAP:
		case JOIN:
		case LEAVE:
			requestQueue.add(sendable);
			break;
		default:
			break;
		}
	}

	@Override
	public void drainResponseQueue(final Collection<NewSendable> target) {
		responseQueue.drainTo(target);
	}

	private void processCommandQueue() {
		final List<NewSendable> commandPairs = new LinkedList<NewSendable>();
		commandQueue.drainTo(commandPairs);
		for (final NewSendable sendable : commandPairs) {
			command(sendable);
		}
		commandPairs.clear();
	}

	private void processRequestQueue() {
		final List<NewSendable> requestPairs = new LinkedList<NewSendable>();
		requestQueue.drainTo(requestPairs);
		for (final NewSendable sendable : requestPairs) {
			final NewSendable response = processRequest(sendable);
			handleResponse(response);
		}
		requestPairs.clear();
	}

	private NewSendable processRequest(final NewSendable request) {
		NewSendable response = null;
		if (!(request.type.equals(SendableType.JOIN) || SessionManager.instance.isPlaying(request.sessionId))) {
			return request.createResponse(request.type.getNeedResponse());
		}
		switch (request.type) {
		case JOIN:
			response = join(request);
			break;
		case GETMAPDATA:
			//					response = getMapData(session, request);
			break;
		case GETPLAYER:
			response = getPlayer(request);
			break;
		case GETUPDATE:
			response = getUpdate(request);
			break;
		case GETTILEMAP:
			response = getTileMap(request);
			break;
		case LEAVE:
			response = leave(request);
			break;
		default:
			break;

		}
		return response;
	}

	private NewSendable getTileMap(final NewSendable request) {
		NewSendable response;
		final List<Tile> tileList = universe.getTiles();
		if (tileList == null) {
			final String errorMsg = "Game does not contain tile data";
			response = request.createResponse(SendableType.GETTILEMAP_ERROR);
			final DataMap data = sendableProvider.getDataMap();
			final MapEntry<Value<String>> errorEntry = new MapEntry<Value<String>>(StringConstants.ERROR_MESSAGE, new Value(errorMsg));
			data.add(errorEntry);
		} else {
			final ListNode<?> tileData = SendableDataConverter.tileMaptoSendableData(tileList, sendableProvider);
			response = request.createResponse(SendableType.GETTILEMAP_OK);
			response.data = tileData;
		}
		return response;
	}

	private void handleResponse(final NewSendable r) {
		assert r != null;
		responseQueue.add(r);
	}

	private void command(final NewSendable cmd) {
		final User user = SessionManager.instance.getUserForSession(cmd.sessionId);
		final Player player = playerStorage.getPlayerForUser(user);
		if (player == null) {
			return;
		}
		EventType type = null;
		switch (cmd.type) {
		case ACTION_ACCELERATE:
			type = EventType.ACTION_ACCELERATE;
			break;
		case ACTION_DECELERATE:
			type = EventType.ACTION_DECELERATE;
			break;
		case ACTION_LEFT:
			type = EventType.ACTION_TURNLEFT;
			break;
		case ACTION_RIGHT:
			type = EventType.ACTION_TURNRIGHT;
			break;
		case ACTION_ENTEREXIT:
			type = EventType.ACTION_ENTEREXIT;
			GUILogger.i().log(TAG, "ENTER/EXIT received");
			break;
		case ACTION_SHOOT:
			type = EventType.ACTION_SHOOT;
			break;
		case ACTION_HANDBRAKE:
			type = EventType.ACTION_HANDBRAKE;
			break;
		case ACTION_SUICIDE:
			type = EventType.ACTION_SUICIDE;
		}
		if (type != null) {
			universe.dispatchEvent(new GameEvent(type, player));
		}
	}


	private NewSendable join(final NewSendable sendable) {
		assert sendable.type.equals(SendableType.JOIN);
		final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
		final Player player = playerStorage.joinUser(user);
		if (player == null) {
			final NewSendable joinError =  sendable.createResponse(SendableType.JOIN_ERROR);
			final Value<String> errorMessage = new Value<String>("no spawnpoint found");
			final MapEntry<Value<String>> errorMessageEntry = new MapEntry<Value<String>>
			(StringConstants.ERROR_MESSAGE, errorMessage);
			final DataMap errorData = new DataMap();
			errorData.add(errorMessageEntry);
			joinError.data = errorData;

			new Value<String>("no spawnpoint found");
			return joinError;
		}
		SessionManager.instance.joinSession(sendable.sessionId, this);
		return sendable.createResponse(SendableType.JOIN_OK);
	}

	private NewSendable leave(final NewSendable sendable) {
		assert sendable.type.equals(SendableType.LEAVE);
		final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
		playerStorage.leaveUser(user);
		SessionManager.instance.leaveSession(sendable.sessionId);
		return sendable.createResponse(SendableType.LEAVE_OK);
	}

	private NewSendable getPlayer(final NewSendable request) {
		assert request.type.equals(SendableType.GETPLAYER);
		final User user = SessionManager.instance.getUserForSession(request.sessionId);
		final Player player = playerStorage.getPlayerForUser(user);
		if (player == null) {
			return request.createResponse(SendableType.GETPLAYER_NEED);
		}
		final NewSendable response = request.createResponse(SendableType.GETPLAYER_OK);

		final DataMap playerData = sendableProvider.getDataMap();
		final DataMap playerMap = SendableDataConverter.toSendableData(player, sendableProvider);
		final MapEntry<DataMap> playerEntry = sendableProvider.getMapEntry(StringConstants.PLAYER_DATA, playerMap);
		playerData.add(playerEntry);
		response.data = playerData;
		return response;
	}

	private NewSendable getUpdate(final NewSendable sendable) {
		assert sendable.type.equals(SendableType.GETUPDATE);
		final User user = SessionManager.instance.getUserForSession(sendable.sessionId);
		final Player player = playerStorage.getPlayerForUser(user);
		if (player == null) {
			return sendable.createResponse(SendableType.GETPLAYER_NEED);
		}
		return getUpdateByRev(sendable.data.asMap().getLong(StringConstants.UPDATE_REVISION),sendable);
	}
	private NewSendable getUpdateByRev(final long baseRevision, final NewSendable sendable){
		final ArrayList<GameObject> entities = universe.entityManager.getUpdate(baseRevision);
		final ArrayList<GameObject> events = universe.eventManager.getUpdate(baseRevision);

		final ListNode<DataMap> entityNodes = SendableDataConverter.toSendableData(entities, sendableProvider);
		final ListNode<DataMap> eventNodes = SendableDataConverter.toSendableData(events, sendableProvider);
		final ListNode<DataMap> scoreNodes = SendableDataConverter.toSendableData(getScoreUpdate(baseRevision), sendableProvider);

		final DataMap updateData = sendableProvider.getDataMap();
		final MapEntry<Value<Long>> revEntry = new MapEntry<Value<Long>>(StringConstants.UPDATE_REVISION, sendableProvider.getValue(universe.getRevision()));
		final MapEntry<ListNode<DataMap>> entEntry = new MapEntry<ListNode<DataMap>>(StringConstants.UPDATE_ENTITIES, entityNodes);
		final MapEntry<ListNode<DataMap>> evtEntry = new MapEntry<ListNode<DataMap>>(StringConstants.UPDATE_GAMEEVENTS, eventNodes);
		final MapEntry<ListNode<DataMap>> scrEntry = new MapEntry<ListNode<DataMap>>(StringConstants.UPDATE_SCORES, scoreNodes);

		updateData.add(revEntry);
		updateData.add(entEntry);
		updateData.add(evtEntry);
		updateData.add(scrEntry);


		final NewSendable updateResponse = sendable.createResponse(SendableType.GETUPDATE_OK);
		updateResponse.data = updateData;
		return updateResponse;
	}

	private List<Score> getScoreUpdate(final long baseRev) {
		final Iterable<Score> allScores = universe.scoreManager.getScores();
		final List<Score> update = new LinkedList<Score>();
		for (final Score score: allScores) {
			if (score.getRevision() > baseRev || score.hasChanged()) {
				update.add(score);
				if (score.hasChanged()) {
					score.updateRevision(universe.getRevision());
				}
			}
		}
		return update;
	}

}
