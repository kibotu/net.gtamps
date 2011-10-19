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

public class ControlCenter implements Runnable, IMessageHandler {

	public static final ControlCenter instance = new ControlCenter();
	
	public final BlockingQueue<Message> inbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Message> outbox = new LinkedBlockingQueue<Message>();
	public final BlockingQueue<Sendable> responsebox = new LinkedBlockingQueue<Sendable>();
	
	private boolean run = true;
	private Map<Integer, IGame> gameThreads = new HashMap<Integer, IGame>();
	
	private IGame game;
	
	private ControlCenter() {
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
		responsebox.drainTo(workingCopy);
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
			case REGISTER:
				handleRegister(session, request);
				break;
			case LOGIN:
				handleLogin(session, request);
				break;
			case JOIN:
			case LEAVE:
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
				break;
		}
	}
	
	private void handleSession(Sendable s) {
		
	}
	
	private void handleRegister(Session s, Sendable request) {
	}

	
	private void handleLogin(Session s, Sendable request) {
		User debugUser = new User(1, "testUser"); 
		s.setUser(debugUser);
	}


	private void handlePlayingRequest(Session s, Sendable request) {
		
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
