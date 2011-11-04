package net.gtamps.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.Game;
import net.gtamps.game.IGame;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.serializer.communication.Message;
import net.gtamps.shared.serializer.communication.Sendable;
import net.gtamps.shared.serializer.communication.SendableType;
import net.gtamps.shared.serializer.communication.data.AuthentificationData;
import net.gtamps.shared.serializer.communication.data.StringData;

public class ControlCenter implements Runnable, IMessageHandler {
	private static final LogType TAG = LogType.SERVER;
	private static final long TARGET_CYCLE_TIME = 20;
	
	public static final ControlCenter instance = new ControlCenter();
	
	public final BlockingQueue<Message> inbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Message> outbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Sendable> responsebox = new LinkedBlockingQueue<Sendable>();
	
	private final boolean run = true;
	private final Map<Integer, IGame> gameThreads = new HashMap<Integer, IGame>();
	
	private IGame game; //tmp
	
	private ControlCenter() {
		createGame(null); // tmp
		new Thread(this, "ControlCenter").start();
	}
	
	@Override
	public void run() {
		long markTime = 0;
		long cycleTime = 0;
		while(run) {
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
	 * @see net.gtamps.server.IMessageHandler#receiveMessage(net.gtamps.server.Connection, net.gtamps.shared.communication.Message)
	 */
	@Override
	public void receiveMessage(final Connection<?> c, final Message msg) {
		if (msg != null) {
			Logger.getInstance().log(TAG, msg.toString());
			final String sessionId = SessionManager.instance.getSessionForMessage(msg, c);
			msg.setSessionId(sessionId);
			inbox.add(msg);
		}
	}
	
	public void handleResponse(final Sendable response) {
		if (response != null) {
			responsebox.add(response);
		}
	}
	
	@Deprecated
	public void restart() {
		game.hardstop();
		createGame(null);
	}
	
	private void processInbox() {
		final List<Message> workingCopy = new LinkedList<Message>();
		inbox.drainTo(workingCopy);
		for (final Message msg : workingCopy) {
			final String msgSessid = msg.getSessionId();
			for (final Sendable i : msg.sendables) {
					assert msgSessid.equals(i.sessionId);
					handleSendable(i);
			}
		}
		workingCopy.clear();
	}
	
	private void processResponsebox() {
		final List<Sendable> workingCopy = new LinkedList<Sendable>();
		//responsebox.drainTo(workingCopy);
		responsebox.drainTo(workingCopy);
		game.drainResponseQueue(workingCopy);
		for (final Sendable response : workingCopy) {
			sendInMessage(response);
		}
		workingCopy.clear();
	}
	
	private void processOutbox() {
		
	}
	
	private void handleSendable(final Sendable request) {
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
			case GETMAPDATA:
			case GETPLAYER:
			case GETUPDATE:
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
	
	private void handleSession(final Sendable s) {
		final Sendable response = s.createResponse(SendableType.SESSION_OK);
		response.data = new StringData(s.sessionId);
		handleResponse(response);
	}
	
	private void handleRegister(final Sendable request) {
		final AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.REGISTER_BAD));
			return;
		}
		// TODO database; get real uid
		Sendable response = null;
		try {
		SessionManager.instance.authenticateSession(request.sessionId, new User(99, adata.username));
		response = request.createResponse(SendableType.REGISTER_OK);
		} catch (final IllegalStateException e) {
			response = request.createResponse(SendableType.REGISTER_BAD);
		}
		handleResponse(response);
 	}

	
	private void handleLogin(final Sendable request) {
		if (SessionManager.instance.isAuthenticated(request.sessionId)) {
			handleResponse(request.createResponse(SendableType.LOGIN_OK));
			return;
		}
		final AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.LOGIN_BAD));
			return;
		}
		// TODO database; get real uid
		final User debugUser = new User(1, adata.username);
		Sendable response = null;
		try {
			SessionManager.instance.authenticateSession(request.sessionId, debugUser);
			response = request.createResponse(SendableType.LOGIN_OK);
		} catch (final IllegalStateException e) {
			// TODO log or something?
			response = request.createResponse(SendableType.LOGIN_BAD);
		} finally {
			handleResponse(response);
		}
	}

	private void handleAuthenticatedRequest(final Sendable request) {
		if (!SessionManager.instance.isAuthenticated(request.sessionId)) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(request);
	}
	

	private void handlePlayingRequest(final Sendable request) {
		if (!SessionManager.instance.isPlaying(request.sessionId)) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(request);
	}
	

	private void sendInMessage(final Sendable response) {
		final Message msg = new Message();
		msg.setSessionId(response.sessionId);
		msg.addSendable(response);
		SessionManager.instance.sendMessage(msg);
	}

	
	private IGame createGame(final String mapname) {
		game = new Game();
		return game;
	}
	
}
