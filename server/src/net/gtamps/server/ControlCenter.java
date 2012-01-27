package net.gtamps.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.Game;
import net.gtamps.game.IGame;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.GameData;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewSendable;
import net.gtamps.shared.serializer.communication.SendableCacheFactory;
import net.gtamps.shared.serializer.communication.SendableProvider;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.StringConstants;
import net.gtamps.shared.serializer.communication.data.DataMap;
import net.gtamps.shared.serializer.communication.data.MapEntry;
import net.gtamps.shared.serializer.communication.data.Value;

public final class ControlCenter implements Runnable, IMessageHandler {
	private static final LogType TAG = LogType.SERVER;
	private static final long TARGET_CYCLE_TIME = 20;

	public static final ControlCenter instance = new ControlCenter();

	public final ConcurrentHashMap<String, Integer> updateRequestInterval = new ConcurrentHashMap<String, Integer>();
	public final ConcurrentHashMap<String, Integer> lastRevisionNumber = new ConcurrentHashMap<String, Integer>();

	public final BlockingQueue<NewMessage> inbox = new LinkedBlockingQueue<NewMessage>();
	public final BlockingQueue<NewMessage> outbox = new LinkedBlockingQueue<NewMessage>();
	public final BlockingQueue<NewSendable> responsebox = new LinkedBlockingQueue<NewSendable>();

	private final Map<Long, IGame> gameThreads = new HashMap<Long, IGame>();
	private final SendableProvider sendableProvider = new SendableProvider(new SendableCacheFactory());

	private boolean run = true;
	private IGame game; //tmp

	private ControlCenter() {
		createGame(null); // tmp
		new Thread(this, "ControlCenter").start();
	}

	@Override
	public void run() {
		long markTime = 0;
		long cycleTime = 0;
		while (run) {
			markTime = System.nanoTime();
			{
				processInbox();
				processResponsebox();
				processOutbox();
			}
			cycleTime = (System.nanoTime() - markTime) / 1000000;
			try {
				Thread.sleep(Math.max(TARGET_CYCLE_TIME - cycleTime, 0));
			} catch (final InterruptedException e) {
				// do nothing;
			}
		}
	}

	/* (non-Javadoc)
	 */
	@Override
	public void receiveMessage(final Connection<?> c, final NewMessage msg) {
		if (msg != null) {
			GUILogger.getInstance().log(TAG, msg.toString());
			String sessionId;
			try {
				sessionId = SessionManager.instance.getSessionForMessage(msg, c);
			} catch (final ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			msg.setSessionId(sessionId);
			inbox.add(msg);
		}
	}

	public void handleResponse(final NewSendable response) {
		if (response != null) {
			responsebox.add(response);
		}
	}

	public void shutdown() {
		run = false;
	}

	@Deprecated
	public void restart() {
		game.hardstop();
		createGame(null);
	}

	private void processInbox() {
		final List<NewMessage> workingCopy = new LinkedList<NewMessage>();
		inbox.drainTo(workingCopy);
		for (final NewMessage msg : workingCopy) {
			final String msgSessid = msg.getSessionId();
			for (final NewSendable i : msg.sendables) {
				assert msgSessid.equals(i.sessionId);
				handleSendable(i);
			}
		}
		workingCopy.clear();
	}

	private void processResponsebox() {
		final List<NewSendable> workingCopy = new LinkedList<NewSendable>();
		//responsebox.drainTo(workingCopy);
		responsebox.drainTo(workingCopy);
		game.drainResponseQueue(workingCopy);
		for (final NewSendable response : workingCopy) {
			sendInMessage(response);
		}
		workingCopy.clear();
	}

	private void processOutbox() {

	}

	private void handleSendable(final NewSendable request) {
		switch (request.type) {
			case SESSION:
				handleSession(request);
				break;
			case REGISTER:
				handleRegister(request);
				break;
			case LOGIN:
				handleLogin(request);
				break;
			case JOIN:
			case LEAVE:
				handleAuthenticatedRequest(request);
				break;
			case GETUPDATE:
				//FIXME easy way out, just throw some packages away.
				//needs more delicate handling later
				//			updateRequestThrottler.putIfAbsent(request.sessionId, 0);
				//			final int requestCounter = updateRequestThrottler.get(request.sessionId);
				//			if(requestCounter%KEEP_EVERY_NTH_UPDATE_REQUEST==0) {
				handlePlayingRequest(request);
				//				System.out.println("ansering update request");
				//			}
				//			System.out.println("throwing away update request");
				//			updateRequestThrottler.replace(request.sessionId, requestCounter+1);
				break;
			case GETMAPDATA:
			case GETTILEMAP:
			case GETPLAYER:
			case ACTION_ACCELERATE:
			case ACTION_DECELERATE:
			case ACTION_HANDBRAKE:
			case ACTION_ENTEREXIT:
			case ACTION_LEFT:
			case ACTION_RIGHT:
			case ACTION_SHOOT:
			case ACTION_SUICIDE:
				handlePlayingRequest(request);
				break;
			default:
				handleResponse(request.createResponse(SendableType.BAD_SENDABLE));
				break;
		}
	}

	private void handleSession(final NewSendable s) {
		final NewSendable response = s.createResponse(SendableType.SESSION_OK);
		final Value<String> sessionId = new Value<String>(s.sessionId);

		final MapEntry<Value<String>> sessionIdEntry = new MapEntry<Value<String>>
		(StringConstants.SESSION_ID, sessionId);
		final DataMap sessionData = new DataMap();
		sessionData.add(sessionIdEntry);
		response.data = sessionData;

		handleResponse(response);
	}

	private void handleRegister(final NewSendable request) {
		final DataMap adata =  (DataMap) request.data;

		final String username = ((Value<String>)(adata.get(StringConstants.AUTH_USERNAME))).get();
		final String password = ((Value<String>)(adata.get(StringConstants.AUTH_PASSWORD))).get();

		if (username == null || username.length() == 0 ||
				password == null || password.length() == 0) {
			handleResponse(request.createResponse(SendableType.REGISTER_BAD));
			return;
		}
		NewSendable response = null;
		try {
			final User user = Authenticator.instance.Register(username, password);
			SessionManager.instance.authenticateSession(request.sessionId, user);
			response = request.createResponse(SendableType.REGISTER_OK);
		} catch (final IllegalStateException e) {
			response = request.createResponse(SendableType.REGISTER_BAD);

			final Value<String> errorMessage = new Value<String>(e.getMessage());
			final MapEntry<Value<String>> errorMessageEntry = new MapEntry<Value<String>>
			(StringConstants.ERROR_MESSAGE, errorMessage);
			final DataMap errorData = new DataMap();
			errorData.add(errorMessageEntry);

			response.data = errorData;
		} catch (final ServerException e) {
			response = request.createResponse(SendableType.REGISTER_BAD);

			final Value<String> errorMessage = new Value<String>(e.getMessage());
			final MapEntry<Value<String>> errorMessageEntry = new MapEntry<Value<String>>
			(StringConstants.ERROR_MESSAGE, errorMessage);
			final DataMap errorData = new DataMap();
			errorData.add(errorMessageEntry);

			response.data = errorData;

		}
		handleResponse(response);
	}


	private void handleLogin(final NewSendable request) {
		if (SessionManager.instance.isAuthenticated(request.sessionId)) {
			handleResponse(request.createResponse(SendableType.LOGIN_OK));
			return;
		}
		final DataMap adata =  (DataMap) request.data;

		final String username = ((Value<String>)(adata.get(StringConstants.AUTH_USERNAME))).get();
		final String password = ((Value<String>)(adata.get(StringConstants.AUTH_PASSWORD))).get();

		if (username == null || username.length() == 0 ||
				password == null || password.length() == 0) {
			handleResponse(request.createResponse(SendableType.LOGIN_BAD));
			return;
		}
		NewSendable response = null;
		try {
			final User user = Authenticator.instance.Login(username, password);
			SessionManager.instance.authenticateSession(request.sessionId, user);
			response = request.createResponse(SendableType.LOGIN_OK);
		} catch (final IllegalStateException e) {
			response = request.createResponse(SendableType.LOGIN_BAD);

			final Value<String> errorMessage = new Value<String>(e.getMessage());
			final MapEntry<Value<String>> errorMessageEntry = new MapEntry<Value<String>>
			(StringConstants.ERROR_MESSAGE, errorMessage);
			final DataMap errorData = new DataMap();
			errorData.add(errorMessageEntry);

			response.data = errorData;
		} catch (final ServerException e) {
			response = request.createResponse(SendableType.LOGIN_BAD);

			final Value<String> errorMessage = new Value<String>(e.getMessage());
			final MapEntry<Value<String>> errorMessageEntry = new MapEntry<Value<String>>
			(StringConstants.ERROR_MESSAGE, errorMessage);
			final DataMap errorData = new DataMap();
			errorData.add(errorMessageEntry);

			response.data = errorData;

		}
		handleResponse(response);
	}

	private void handleAuthenticatedRequest(final NewSendable request) {
		if (!SessionManager.instance.isAuthenticated(request.sessionId)) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(request);
	}


	private void handlePlayingRequest(final NewSendable request) {
		if (!SessionManager.instance.isPlaying(request.sessionId)) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(request);
	}


	private void sendInMessage(final NewSendable response) {
		final NewMessage msg = new NewMessage();
		msg.setSessionId(response.sessionId);
		msg.sendables = msg.sendables.append(sendableProvider.getListNode(response));
		SessionManager.instance.sendMessage(msg);
	}


	private IGame createGame(final String mapname) {
		if (game != null) {
			game.hardstop();
		}
		game = new Game();
		gameThreads.put(game.getId(), game);
		return game;
	}

	public List<GameData> getGames() {
		return Collections.singletonList(getGameDataFromGame(game));
	}

	private GameData getGameDataFromGame(final IGame gameThread) {
		return new GameData(
				"SampleGameName",
				"sampleHash" + Long.toHexString(gameThread.getId()),
				-1,
				20,
				1234,
				"../img/pingpong.png");
	}

}
