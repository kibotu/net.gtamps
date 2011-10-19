package net.gtamps.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.gtamps.game.Game;
import net.gtamps.game.IGame;
import net.gtamps.shared.communication.Message;
import net.gtamps.shared.communication.Sendable;
import net.gtamps.shared.communication.SendableType;
import net.gtamps.shared.communication.data.AuthentificationData;
import net.gtamps.shared.communication.data.StringData;

public class ControlCenter implements Runnable, IMessageHandler {

	public static final ControlCenter instance = new ControlCenter();
	
	public final BlockingQueue<Message> inbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Message> outbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Sendable> responsebox = new LinkedBlockingQueue<Sendable>();
	
	private boolean run = true;
	private Map<Integer, IGame> gameThreads = new HashMap<Integer, IGame>();
	
	private IGame game;
	
	private ControlCenter() {
		this.createGame(null);
		new Thread(this, "ControlCenter").start();
	}
	
	public void run() {
		while(run) {
			processInbox();
			processResponsebox();
			processOutbox();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.gtamps.server.IMessageHandler#receiveMessage(net.gtamps.server.Connection, net.gtamps.shared.communication.Message)
	 */
	@Override
	public void receiveMessage(Connection<?> c, Message msg) {
		if (msg != null) {
			inbox.add(msg);
		}
	}
	
	public void handleResponse(Sendable response) {
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
		List<Message> workingCopy = new LinkedList<Message>();
		inbox.drainTo(workingCopy);
		for (Message msg : workingCopy) {
			Session session = SessionManager.instance.getSessionForMessage(msg);
			for (Sendable i : msg.sendables) {
					handleSendable(session, i);
			}
		}
		workingCopy.clear();
	}
	
	private void processResponsebox() {
		List<Sendable> workingCopy = new LinkedList<Sendable>();
		//responsebox.drainTo(workingCopy);
		this.responsebox.drainTo(workingCopy);
		for (Sendable response : workingCopy) {
			sendInMessage(response);
		}
		workingCopy.clear();
		this.game.drainResponseQueue(workingCopy);
		for (Sendable response : workingCopy) {
				sendInMessage(response);
		}
	}
	
	private void processOutbox() {
		
	}
	
	private void handleSendable(Session session, Sendable request) {
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
				handlePlayingRequest(session, request);
				break;
			default:
				handleResponse(request.createResponse(SendableType.BAD_SENDABLE));
				break;
		}
	}
	
	private void handleSession(Sendable s) {
		Sendable response = s.createResponse(SendableType.SESSION_OK);
		response.data = new StringData(s.sessionId); 
		handleResponse(response);
	}
	
	private void handleRegister(Session s, Sendable request) {
		AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.REGISTER_BAD));
			return;
		}
		handleResponse(request.createResponse(SendableType.REGISTER_OK));
	}

	
	private void handleLogin(Session s, Sendable request) {
		if (s.isAuthenticated()) {
			handleResponse(request.createResponse(SendableType.LOGIN_OK));
			return;
		}
		AuthentificationData adata = (AuthentificationData) request.data;
		if (adata == null || adata.username == null || adata.username.length() == 0 ||
				adata.password == null || adata.password.length() == 0) {
			handleResponse(request.createResponse(SendableType.LOGIN_BAD));
			return;
		}
		User debugUser = new User(1, adata.username); 
		s.setUser(debugUser);
		handleResponse(request.createResponse(SendableType.LOGIN_OK));
	}

	private void handleAuthenticatedRequest(Session s, Sendable request) {
		if (!s.isAuthenticated()) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		this.game.handleSendable(s, request);
	}
	

	private void handlePlayingRequest(Session s, Sendable request) {
		if (!s.isAuthenticated() || !s.isPlaying()) {
			handleResponse(request.createResponse(request.type.getNeedResponse()));
			return;
		}
		this.game.handleSendable(s, request);
	}
	
	private void sendInMessage(Sendable r) {
		Session s = SessionManager.instance.getSessionById(r.sessionId);
		Message msg = new Message();
		msg.setSessionId(s.getId());
		msg.addSendable(r);
		s.getConnection().send(msg);
	}
	
	private IGame createGame(String mapname) {
//		IGame game = new GameThread();
//		if (game != null) {
//			this.gameThreads.put(game.getId(), game);
//		}
//		return game;
		game = new Game();
		return game;
	}
	

	
	
}
