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
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableType;
import net.gtamps.shared.communication.data.AuthentificationData;
import net.gtamps.shared.communication.data.StringData;

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
			final Session session = SessionManager.instance.getSessionForMessage(msg);
			final String sessid = session.getId();
			for (final Sendable i : msg.sendables) {
					i.sessionId = sessid;
					handleSendable(session, i);
			}
		}
		workingCopy.clear();
	}
	
	private void processResponsebox() {
		final List<Sendable> workingCopy = new LinkedList<Sendable>();
		//responsebox.drainTo(workingCopy);
		responsebox.drainTo(workingCopy);
		for (final Sendable response : workingCopy) {
			sendInMessage(response);
		}
		workingCopy.clear();
		game.drainResponseQueue(workingCopy);
		for (final Sendable response : workingCopy) {
				sendInMessage(response);
		}
	}
	
	private void processOutbox() {
		
	}
	
	private void handleSendable(final Session session, final Sendable request) {
		switch (request.type) {
			case SESSION:
				handleSession(request);
				break;
			case REGISTER:
				handleRegister(session, request);
				break;
			case LOGIN:
				handleLogin(session, request);
				break;
			case JOIN:
			case LEAVE:
				handleAuthenticatedRequest(session, request);
				break;
			case GETMAPDATA:
			case GETPLAYER:
			case GETUPDATE:
			case ACCELERATE:
			case DECELERATE:
			case HANDBRAKE:
			case ENTEREXIT:
			case LEFT:
			case RIGHT:
			case SHOOT:
			case SUICIDE:
				handlePlayingRequest(session, request);
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
	
	private void handleRegister(final Session s, final Sendable request) {
		final AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.REGISTER_BAD));
			return;
		}
		handleResponse(request.createResponse(SendableType.REGISTER_OK));
	}

	
	private void handleLogin(final Session s, final Sendable request) {
		if (s.isAuthenticated()) {
			handleResponse(request.createResponse(SendableType.LOGIN_OK));
			return;
		}
		final AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.LOGIN_BAD));
			return;
		}
		final User debugUser = new User(1, adata.username);
		s.setUser(debugUser);
		handleResponse(request.createResponse(SendableType.LOGIN_OK));
	}

	private void handleAuthenticatedRequest(final Session s, final Sendable request) {
		if (!s.isAuthenticated()) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(s, request);
	}
	

	private void handlePlayingRequest(final Session s, final Sendable request) {
		if (!s.isAuthenticated() || !s.isPlaying()) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		game.handleSendable(s, request);
	}
	
	private void sendInMessage(final Sendable r) {
		final Session s = SessionManager.instance.getSessionById(r.sessionId);
		final Message msg = new Message();
		msg.setSessionId(s.getId());
		msg.addSendable(r);
		s.getConnection().send(msg);
	}
	
	private IGame createGame(final String mapname) {
//		IGame game = new GameThread();
//		if (game != null) {
//			this.gameThreads.put(game.getId(), game);
//		}
//		return game;
		game = new Game();
		return game;
	}
	

	
	
}
